package com.blastoff.campaign_service.config;

import org.apache.kafka.clients.admin.NewTopic;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.config.TopicBuilder;

/**
 * Defines the topic used for campaign messages.
 */
@Configuration
public class KafkaTopicConfig {

    /**
     * Creates the Kafka topic used for campaign messages.
     *
     * @param name The name of the kafka topic.
     *
     * @return The definition for the topic
     */
    @Bean
    public NewTopic campaignMessagesTopic(@Value("${app.topics.campaign-messages}") String name) {
        return TopicBuilder.name(name)
                           .partitions(6)
                           .replicas(1)
                           .build();
    }
}