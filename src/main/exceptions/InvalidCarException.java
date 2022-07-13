package main.exceptions;

public class InvalidCarException extends Exception{
    private String message;
    public InvalidCarException(String message) {
        super(message);
    }
}
