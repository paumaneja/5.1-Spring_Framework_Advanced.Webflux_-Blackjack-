package cat.itacademy.s05.t01.n01.exception;

public class DeckEmptyException extends RuntimeException {
    public DeckEmptyException(String message) {
        super(message);
    }
}
