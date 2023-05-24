package ru.leonchenko.graduationproject.controller;

import lombok.AllArgsConstructor;
import lombok.SneakyThrows;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import ru.leonchenko.graduationproject.enums.Keystore;
import ru.leonchenko.graduationproject.model.CipherRq;
import ru.leonchenko.graduationproject.model.CipherRs;
import ru.leonchenko.graduationproject.model.DecipherRq;
import ru.leonchenko.graduationproject.model.DecipherRs;
import ru.leonchenko.graduationproject.service.CodecService;
import java.security.*;
import java.util.Base64;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static ru.leonchenko.graduationproject.enums.Constants.KEY_ALIAS;
import static ru.leonchenko.graduationproject.utils.Utils.createTempDirectory;

@org.springframework.stereotype.Controller
@AllArgsConstructor
public class Controller {

    private CodecService service;

    @GetMapping("/")
    public String hello(Model model, RedirectAttributes attributes) {
        model.addAttribute("cipherRq", new CipherRq());
        model.addAttribute("decipherRq", new DecipherRq());
        return "generate";
    }

    @PostMapping("/")
    @SneakyThrows
    public String cipher(CipherRq cipherRq, Model model, RedirectAttributes attributes) {

        var temp = createTempDirectory() + "/";

        List<String> list = Stream.of(Keystore.values())
                .map(Keystore::getValue)
                .collect(Collectors.toList());

        var keyStoreType = service.getRandomKeystore(
                cipherRq.getKeyWord(),
                cipherRq.getRandomType(),
                list);

        var keyStore = service.generateKeystoreWithKeys(temp, keyStoreType, cipherRq.getPassword());

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(KEY_ALIAS, cipherRq.getPassword().toCharArray());
        PublicKey publicKey = keyStore.getCertificate(KEY_ALIAS).getPublicKey();

        var cipherData = service.dataCoder(cipherRq.getKeyWord(), publicKey);

        var signature = service.signWithSignature(new KeyPair(publicKey, privateKey), cipherData);

        var keys = String.join(",", Collections.list(keyStore.aliases()));

        var rs = CipherRs
                .builder()
                .keystoreType(keyStoreType)
                .keyName(keys)
                .cipherWord(Base64.getEncoder().encodeToString(cipherData))
                .signature(Base64.getEncoder().encodeToString(signature))
                .keystorePath(temp + "keystore." + keyStoreType.toLowerCase())
                .build();

        attributes.addFlashAttribute("keystoreType", rs.getKeystoreType());
        attributes.addFlashAttribute("keyName", rs.getKeyName());
        attributes.addFlashAttribute("cipherWord", rs.getCipherWord());
        attributes.addFlashAttribute("signature", rs.getSignature());
        attributes.addFlashAttribute("keystorePath", rs.getKeystorePath());

        return "redirect:/";
    }

    @SneakyThrows
    @PostMapping("/decipher")
    public String uploadFile(DecipherRq rq, Model model, RedirectAttributes attributes) {

        var data = Base64.getDecoder().decode(rq.getCodedWord());
        var signature = Base64.getDecoder().decode(rq.getSign());

        var keyStore = service.loadKeystore(rq.getKeystorePath(), rq.getKeystorePassword());

        PrivateKey privateKey = (PrivateKey) keyStore.getKey(rq.getKeyAlias(), rq.getKeystorePassword().toCharArray());
        PublicKey publicKey = keyStore.getCertificate(rq.getKeyAlias()).getPublicKey();

        var decodedData = service.dateDecoder(data, privateKey);

        var signFlag = service.verifySignature(new KeyPair(publicKey, privateKey), data, signature);

        var rs = DecipherRs
                .builder()
                .decodedWord(new String(decodedData))
                .isSigned(signFlag)
                .build();

        attributes.addFlashAttribute("decodedWord", rs.getDecodedWord());
        attributes.addFlashAttribute("isSigned", rs.getIsSigned());

        return "redirect:/";
    }

}
