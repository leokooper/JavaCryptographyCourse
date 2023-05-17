package enums;

public enum SignatureAlgorithm {

    PBKDF_2_WITH_HMAC_SHA_256("PBKDF2WithHmacSHA256");

    private final String value;

    SignatureAlgorithm(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
