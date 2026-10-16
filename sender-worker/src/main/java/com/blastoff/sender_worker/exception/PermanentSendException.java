package com.blastoff.sender_worker.exception;

/**
 * Describes the exception to be thrown when a permanent failure is encountered,
 * i.e., a mail id that doesn't exist.
 */
public class PermanentSendException extends RuntimeException{
    public PermanentSendException(String message){
        super(message);
    }
}
