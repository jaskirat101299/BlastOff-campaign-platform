package com.blastoff.sender_worker.config;

import com.blastoff.sender_worker.exception.PermanentSendException;
import com.blastoff.sender_worker.messaging.CampaignMessage;
import com.blastoff.sender_worker.messaging.ResultPublisher;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.common.TopicPartition;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.listener.ConsumerRecordRecoverer;
import org.springframework.kafka.listener.DeadLetterPublishingRecoverer;
import org.springframework.kafka.listener.DefaultErrorHandler;
import org.springframework.kafka.support.ExponentialBackOffWithMaxRetries;

/**
 * Configuration class for handling Kafka consumer processing failures.
 * <p>
 * Configures retry behavior with exponential backoff and recovery logic
 * for messages that cannot be processed successfully. Failed messages are
 * published to a dead-letter topic, and campaign message failures are
 * reported through the result publisher.
 * </p>
 */
@Configuration
@Slf4j
public class KafkaErrorHandler {

    /**
     * Creates and configures the Kafka error handler.
     *
     * @param template Kafka template  used to publish failed records to the
     * dead-letter topic.
     * @param resultPublisher Publisher used to report campaign message failures.
     * @param dltTopic The dead-letter topic name.
     * @param maxAttempts The maximum number of retry attempts to make the
     * recovery.
     * @param initialIntervalMs The initial delay between retry attempts,
     * in milliseconds.
     * @param multiplier The multiplier used to increase the delay between
     * retries.
     *
     * @return The configured bean.
     */
    @Bean
    public DefaultErrorHandler errorHandler(
        KafkaTemplate<String, Object> template,
        ResultPublisher resultPublisher,
        @Value("${app.topics.campaign-messages-dlt}") String dltTopic,
        @Value("${app.retry.max-attempts}") int maxAttempts,
        @Value("${app.retry.initial-interval-ms}") long initialIntervalMs,
        @Value("${app.retry.multiplier}") double multiplier) {
        final var toDlt = new DeadLetterPublishingRecoverer(template
            , (record, ex) -> new TopicPartition(dltTopic, record.partition()));

        ConsumerRecordRecoverer recoverer = (record, ex) -> {
            toDlt.accept(record, ex);

            if (record.value() instanceof CampaignMessage message) {
                resultPublisher.publishFailed(message, rootMessage(ex));
            }
            log.error("Giving up on {} after failure: {}", record.key(), rootMessage(ex));
        };

        final var backOff = new ExponentialBackOffWithMaxRetries(maxAttempts);
        backOff.setInitialInterval(initialIntervalMs);
        backOff.setMultiplier(multiplier);

        DefaultErrorHandler handler = new DefaultErrorHandler(recoverer, backOff);
        handler.addNotRetryableExceptions(PermanentSendException.class);
        return handler;
    }

    /**
     * Extracts the message from the immediate cause of an exception, if present.
     * <p>
     * If the exception has no cause, its own message is returned.
     * </p>
     *
     * @param ex The exception encountered during message processing.
     *
     * @return The message of the immediate cause, or the original exception
     * message when no cause exists
     */
    private static String rootMessage(Exception ex) {
        Throwable cause = ex.getCause() != null ? ex.getCause() : ex;
        return cause.getMessage();
    }
}