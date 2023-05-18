package enums;

public enum Path {

    ROOT_PATH("05-hw-keystore/src/main/resources/keystores/");

    private final String value;

    Path(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
