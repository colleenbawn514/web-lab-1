package app.exception;

public class PlaylistNotFoundException extends Exception {
    public PlaylistNotFoundException(String message) {
        super(message);
    }
}
