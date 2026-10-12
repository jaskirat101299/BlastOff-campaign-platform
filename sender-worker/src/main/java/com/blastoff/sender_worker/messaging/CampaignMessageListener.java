package com.blastoff.sender_worker.messaging;

import com.blastoff.sender_worker.service.MockEmailSender;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignMessageListener {
    private final MockEmailSender mockEmailSender;

    /**
     * Handles a campaign message received from Kafka.
     *
     * <p>
     * Logs the Kafka partition, offset, and key associated with the received
     * record, then delegates the message to {@link MockEmailSender} for
     * processing.
     * </p>
     *
     * @param record The record containing the details of the campaign.
     */
    @KafkaListener(topics = "${app.topics.campaign-messages}")
    public void onMessage(final ConsumerRecord<String, CampaignMessage> record) {
        log.info("Received partition={} offset={} key={}"
            , record.partition()
            , record.offset()
            , record.key());

        mockEmailSender.send(record.value());
    }
}
