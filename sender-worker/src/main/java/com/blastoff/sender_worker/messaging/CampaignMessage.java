package com.blastoff.sender_worker.messaging;

/**
 * Represents a message containing the information required to send an email
 * for a campaign recipient.
 *
 * <p>
 * This record is the sender worker's representation of a campaign message
 * consumed from Kafka. Each message represents one email to one recipient.
 * </p>
 *
 * @param messageId The unique identifier of the message.
 * @param campaignId The unique identifier of the campaign to which the message
 * belongs.
 * @param recipient The recipient of the message.
 * @param subject The subject of the campaign message.
 * @param body The body content of the message.
 */
public record CampaignMessage(
    String messageId,
    String campaignId,
    String recipient,
    String subject,
    String body) {
}