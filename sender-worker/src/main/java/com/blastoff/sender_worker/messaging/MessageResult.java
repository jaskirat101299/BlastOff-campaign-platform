package com.blastoff.sender_worker.messaging;

/**
 * Describes the message result, whether it has been successfully delivered.
 *
 * @param messageId The identifier of the message.
 * @param campaignId The unique identifier of the campaign.
 * @param recipient The recipient of the message.
 * @param status The status of the message, whether it was delivered or not.
 * @param reason The reason for the failure of message delivery.
 */
public record MessageResult(
    String messageId,
    String campaignId,
    String recipient,
    Status status,
    String reason) {

    /**
     * Describes the status of the message.
     */
    public enum Status {SENT, FAILED}

    /**
     * The result of the campaign message if it was successfully delivered.
     *
     * @param message The details of the campaign message.
     *
     * @return The result of the successfully delivered message.
     */
    public static MessageResult sent(final CampaignMessage message) {
        return new MessageResult(message.messageId()
            , message.campaignId()
            , message.recipient()
            , Status.SENT
            , null);
    }

    /**
     * The result of the campaign message, failed to be delivered.
     *
     * @param message The details of the campaign message.
     * @param reason The reason for the failure of message delivery.
     *
     * @return The details of the failed message.
     */
    public static MessageResult failed(final CampaignMessage message
        , final String reason) {
        return new MessageResult(message.messageId()
            , message.campaignId()
            , message.recipient()
            , Status.FAILED
            , reason);
    }
}