package enums;

public enum Path {

    ROOT_PATH("03-hw-asynccipherandsignature/src/main/resources");

    private final String value;

    Path(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
