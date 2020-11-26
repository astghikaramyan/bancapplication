package am.bdgexame.bancapplication.model;

public enum Type {
    DEPOSIT("DEPOSIT"),
    WITHDRAWAL("WITHDRAWAL");
    String type;
    Type(String type){
        this.type = type;
    }
}
