package models;

public enum TaskType {

    EPIC("Эпик"),

    SUB_TASK("Подзадача"),

    TASK("Задача");
    private String typeDescription;

    TaskType(String typeDescription){
        this.typeDescription = typeDescription;
    }
}
