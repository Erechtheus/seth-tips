package de.dfki.nlp.domain.exceptions;

public class PayloadException extends BaseException {

    public PayloadException(String message) {
        // FORMAT_ERROR
        super(message, Errors.FORMAT_ERROR.errorCode);
    }
}
