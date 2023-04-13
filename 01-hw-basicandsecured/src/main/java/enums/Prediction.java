package enums;

public enum Prediction {

    BASIC("У вас сегодня будет удача в дела!"),
    SECURE("Сегодня хороший день для саморазвития!");

    private final String value;

    Prediction(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
