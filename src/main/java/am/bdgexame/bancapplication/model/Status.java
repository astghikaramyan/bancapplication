package am.bdgexame.bancapplication.model;

public enum Status {
    PENDING("PENDING"),
    CANCELED("CANCELED"),
    ACCEPTED("ACCEPTED");
    String status;

    Status(String s) {
        this.status = s;
    }
}
