package dev.paragon.quests.utilities.base64;

import org.jetbrains.annotations.NotNull;

public class Base64ConvertException extends RuntimeException {

    private static final long serialVersionUID = 1L;
    private final Exception exception;

    public Base64ConvertException(Exception exception) {
        super(exception.getClass().getSimpleName() + ": " + exception.getMessage());
        this.exception = exception;
    }

    public @NotNull Exception getException() {
        return exception;
    }
}
