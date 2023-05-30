package banking;

public class ErrorMessage implements java.io.Serializable {
    public String message;

    public ErrorMessage(String msg) {
        this.message = msg;
    }
}
