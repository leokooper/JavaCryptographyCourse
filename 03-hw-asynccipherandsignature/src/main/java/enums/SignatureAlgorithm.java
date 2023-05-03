package enums;

public enum SignatureAlgorithm {

    SHA_256_WITH_RSA("SHA256withRSA");

    private final String value;

    SignatureAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
