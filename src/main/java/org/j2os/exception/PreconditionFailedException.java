package org.j2os.exception;

public class PreconditionFailedException extends RuntimeException {
    public PreconditionFailedException(String message){
        super(message);
    }
}
