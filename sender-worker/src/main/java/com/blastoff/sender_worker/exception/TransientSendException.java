package com.blastoff.sender_worker.exception;

/**
 * Describes the exception to be thrown when a temporary failure is encountered,
 * i.e., a provider time out.
 */
public class TransientSendException extends RuntimeException {
    public TransientSendException(String message) {
        super(message);
    }
}
