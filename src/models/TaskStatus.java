package models;

public enum TaskStatus {
    NEW("Новая"),
    IN_PROGRESS("В процессе"),
    DONE("Завершенная");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}