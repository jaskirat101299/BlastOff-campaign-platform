package com.blastoff.sender_worker.service;

import com.blastoff.sender_worker.messaging.CampaignMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

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
        log.info("Email sent to {} (subject: {})"
            , message.recipient()
            , message.subject());
    }
}
