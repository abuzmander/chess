package exceptions;

import response.Error;

public class ResponseException extends Exception{
    final private int statusCode;
    final private Error errMessage;

    public ResponseException(int statusCode, Error message) {
        super(message.message());
        this.statusCode = statusCode;
        this.errMessage = message;

    }

    public int StatusCode() {
        return statusCode;
    }

    public Error ErrorMessage() {
        return errMessage;
    }
}
