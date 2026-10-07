package com.blastoff.campaign_service.messaging;

public record CampaignMessage(
    String messageId,
    String campaignId,
    String recipient,
    String subject,
    String body
) {
}
