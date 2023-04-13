package utils;

import org.junit.Test;

import java.util.List;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static utils.Utils.getSecurityAnswer;

public class UtilsTest {

    @Test
    public void when_securityType_is_none_default_switch_param_return(){
        String response = getSecurityAnswer("Ivan", "none", List.of("Hello", "World"));
        assertNotNull(response);
        assertTrue(response.contains("Выберите в следующий раз Basic или Secured"));
    }

    @Test
    public void when_securityType_is_basic_or_secured_will_not_return_default_parameter(){
        List<String> params = List.of("Hello", "World");
        String response = getSecurityAnswer("Ivan", "Basic", params);
        assertNotNull(response);
        assertTrue(response.contains(params.get(0)) || response.contains(params.get(1)));
    }
}