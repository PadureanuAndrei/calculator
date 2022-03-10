package calculator.exceptions;

public class InvalidExpressionException extends RuntimeException {
    public InvalidExpressionException(Exception e) {
        super("Expression is invalid", e);
    }

    public InvalidExpressionException() {
        super("Expression is invalid");
    }
}
