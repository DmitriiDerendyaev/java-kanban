package models;

public enum TaskType {

    EPIC("����"),

    SUB_TASK("���������"),

    TASK("������");
    private String typeDescription;

    TaskType(String typeDescription){
        this.typeDescription = typeDescription;
    }
}
