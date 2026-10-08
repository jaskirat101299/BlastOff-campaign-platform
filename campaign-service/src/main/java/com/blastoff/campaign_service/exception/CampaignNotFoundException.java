package com.blastoff.campaign_service.exception;

import java.util.UUID;

/**
 * Exception thrown when the campaign with the given identifier is not found.
 */
public class CampaignNotFoundException extends RuntimeException {
    public CampaignNotFoundException(UUID id) {
        super("Campaign not found: " + id);
    }
}