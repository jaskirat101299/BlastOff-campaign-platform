package com.blastoff.sender_worker.service;

import com.blastoff.sender_worker.exception.PermanentSendException;
import com.blastoff.sender_worker.exception.TransientSendException;
import com.blastoff.sender_worker.messaging.CampaignMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.concurrent.ThreadLocalRandom;

/**
 * Simulates sending emails for campaign messages.
 *
 * <p>
 * This service represents the email sending component of the sender worker.
 * Instead of communicating with a real email provider, it waits for the
 * configured delay and then logs that the email was sent.
 * </p>
 *
 */
@Service
@Slf4j
public class MockEmailSender {
    @Value("${app.send-delay-ms}")
    private long sendDelayMs;

    @Value("${app.mock.transient-failure-rate}")
    private double transientFailureRate;

    @Value("${app.mock.permanent-failure-percent}")
    private int permanentFailurePercent;

    /**
     * Simulating method for sending an email for the campaign message.
     *
     * @param message The message containing the detail of campaign and
     * recipient it is to be sent to.
     */
    public void send(final CampaignMessage message) {
        try {
            Thread.sleep(sendDelayMs);
        }
        catch (InterruptedException e) {
            Thread.currentThread().interrupt();

            throw new IllegalStateException("Interrupted while sending", e);
        }

        if (isBadAddress(message.recipient())) {
            log.warn("Permanent failure for {}", message.recipient());
            throw new PermanentSendException("Mailbox doesn't exist: "
                                                 + message.recipient());
        }

        if (ThreadLocalRandom.current().nextDouble() < transientFailureRate) {
            log.warn("Temporary failure for {}", message.recipient());
            throw new TransientSendException("Provider timeout");
        }

        log.info("Email sent to {} (subject: {})"
            , message.recipient()
            , message.subject());
    }

    /**
     * Determines whether the recipient email should be treated as invalid.
     *
     * @param recipient The email address of the recipient.
     *
     * @return Whether the recipient email should be treated as invalid.
     */
    private boolean isBadAddress(final String recipient) {
        return Math.abs(recipient.hashCode() % 100) < permanentFailurePercent;
    }
}
