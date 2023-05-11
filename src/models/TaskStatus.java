package models;

public enum TaskStatus {
    NEW("�����"),
    IN_PROGRESS("� ��������"),
    DONE("�����������");

    private final String description;

    TaskStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}