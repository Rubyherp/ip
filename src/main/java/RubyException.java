/**
 * Represents a user-facing error that Ruby can recover from.
 */
public class RubyException extends Exception {
    public RubyException(String message) {
        super(message);
    }
}
