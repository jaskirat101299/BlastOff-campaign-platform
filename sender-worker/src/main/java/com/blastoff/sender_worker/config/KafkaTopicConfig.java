package com.blastoff.sender_worker.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Configuration class for defining Kafka topics used by the sender worker.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Defines Kafka topic for publishing message processing results.
     *
     * @param name The name of the Kafka topic.
     *
     * @return Instance representing the message results topic.
     */
    @Bean
    public NewTopic messageResultsTopic(@Value("${app.topics.message-results}") final String name) {
        return TopicBuilder.name(name).partitions(6)
                           .replicas(1)
                           .build();
    }

    /**
     * Defines the Kafka dead-letter topic for the campaign messages that
     * cannot be successfully processed.
     *
     * <p>
     * This dead letter topic has been given 6 partions on purpose. By default,
     * a failed message is published to the same partition number it came from,
     * so DLT needs as many partitions as the source topic.
     * </P>
     *
     * @param name The name of the Kafka topic.
     *
     * @return Instance representing the campaign messages dead-letter topic
     *
     */
    @Bean
    public NewTopic deadLetterTopic(@Value("${app.topics.campaign-messages-dlt}") final String name) {
        return TopicBuilder.name(name).partitions(6)
                           .replicas(1)
                           .build();
    }
}