package com.blastoff.sender_worker.messaging;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

/**
 * Publishes the message delivery results to the kafka.
 *
 * <p>
 * Successful message deliveries and failed delivery attempts are
 * published with their corresponding result details.
 * </p>
 */
@Component
@RequiredArgsConstructor
public class ResultPublisher {
    private final KafkaTemplate<String, Object> template;

    @Value("${app.topics.message-results}")
    private String topic;

    /**
     * Publishes a result indicating that the specified campaign message
     * was sent successfully.
     *
     * @param message The campaign message to be sent.
     */
    public void publishSent(CampaignMessage message) {
        publish(MessageResult.sent(message));
    }

    /**
     * Publishes a result indicating that the specified campaign message
     * failed to send.
     *
     * @param message The campaign message to be sent.
     * @param reason The reason for the delivery failure.
     */
    public void publishFailed(CampaignMessage message, String reason) {
        publish(MessageResult.failed(message, reason));
    }

    /**
     * Sends the specified message result to the configured Kafka topic,
     * using the campaign ID as the Kafka message key.
     *
     * @param result the message result to publish
     */
    private void publish(MessageResult result) {
        template.send(topic, result.campaignId(), result);
    }
}