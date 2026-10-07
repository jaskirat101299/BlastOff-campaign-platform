package com.blastoff.campaign_service.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

import java.util.List;

/**
 * Request for creating a campaign message.
 *
 * @param name The name of the campaign.
 * @param subject The subject of the campaign message.
 * @param body The content of the message.
 * @param recipients The total number of campaigns targeted by this campaign.
 * @param recipientCount The total recipient count, in the given range.
 */
public record CreateCampaignRequest(
    @NotBlank String name,
    @NotBlank String subject,
    @NotBlank String body,
    List<String> recipients,
    @Min(0) @Max(100000) Integer recipientCount) {
}