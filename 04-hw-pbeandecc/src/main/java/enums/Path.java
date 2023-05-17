package enums;

public enum Path {

    ROOT_PATH("04-hw-pbeandecc/src/main/resources");

    private final String value;

    Path(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
