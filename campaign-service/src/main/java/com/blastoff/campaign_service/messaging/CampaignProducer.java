package com.blastoff.campaign_service.messaging;

import com.blastoff.campaign_service.domain.Campaign;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * The Kafka producer for publishing the campaign messages to the configured
 * topic.
 * <p>Each recipient receives a separate {@link CampaignMessage} containing
 * the campaign details and a unique message identifier.</p>
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class CampaignProducer {
    private final KafkaTemplate<String, CampaignMessage> template;

    @Value("${app.topics.campaign-message}")
    private String topic;

    /**
     * Publishes a campaign message for each recipient to the configured topic.
     *
     * @param campaign The campaign whose details are to be included in the
     * message.
     * @param recipients The recipients to whom campaign should be published.
     */
    public void publish(final Campaign campaign
        , final List<String> recipients) {
        final var campaignId = campaign.getId().toString();

        for (final var recipient : recipients) {
            final var messageId = campaignId + ":" + recipient;

            final var msg = new CampaignMessage(messageId
                , campaignId
                , recipient
                , campaign.getSubject()
                , campaign.getBody());

            template.send(topic, messageId, msg);
        }

        template.flush();
        log.info("Published {} messages for campaign {}", recipients.size()
            , campaignId);

    }
}
