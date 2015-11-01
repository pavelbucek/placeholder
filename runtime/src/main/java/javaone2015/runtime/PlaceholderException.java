package javaone2015.runtime;

/**
 * @author Michal Gajdos
 * @author Pavel Bucek
 */
public class PlaceholderException extends RuntimeException {

    public PlaceholderException() {
    }

    public PlaceholderException(final String message) {
        super(message);
    }

    public PlaceholderException(final String message, final Throwable cause) {
        super(message, cause);
    }

    public PlaceholderException(final Throwable cause) {
        super(cause);
    }

    public PlaceholderException(final String message,
                                final Throwable cause,
                                final boolean enableSuppression,
                                final boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
