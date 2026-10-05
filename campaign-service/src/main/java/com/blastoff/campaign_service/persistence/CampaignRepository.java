package com.blastoff.campaign_service.persistence;

import com.blastoff.campaign_service.domain.Campaign;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * Contract for data access operations on {@link Campaign}.
 */
public interface CampaignRepository extends JpaRepository<Campaign, UUID> {
    List<Campaign> findAllByOrderByCreatedAtDesc();
}
