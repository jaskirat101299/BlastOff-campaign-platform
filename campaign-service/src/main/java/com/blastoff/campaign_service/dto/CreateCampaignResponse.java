package com.blastoff.campaign_service.dto;

import com.blastoff.campaign_service.domain.Status;

import java.time.Instant;
import java.util.UUID;

/**
 * Response for creating a campaign message.
 *
 * @param id The unique identifier of the campaign.
 * @param name The name of the campaign.
 * @param subject The subject of the campaign message.
 * @param status The current status of the campaign.
 * @param totalRecipients The total number of campaigns targeted by this
 * campaign.
 * @param createdAt The timestamp at which the campaign was created,
 */
public record CreateCampaignResponse(
    UUID id,
    String name,
    String subject,
    Status status,
    int totalRecipients,
    Instant createdAt) {
}