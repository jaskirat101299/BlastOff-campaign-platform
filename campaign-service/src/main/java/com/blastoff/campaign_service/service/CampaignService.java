package com.blastoff.campaign_service.service;

import com.blastoff.campaign_service.domain.Campaign;
import com.blastoff.campaign_service.domain.Status;
import com.blastoff.campaign_service.dto.CreateCampaignRequest;
import com.blastoff.campaign_service.dto.CreateCampaignResponse;
import com.blastoff.campaign_service.exception.CampaignNotFoundException;
import com.blastoff.campaign_service.messaging.CampaignProducer;
import com.blastoff.campaign_service.persistence.CampaignRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.IntStream;

@Service
@RequiredArgsConstructor
public class CampaignService {
    private final CampaignRepository campaignRepository;

    private final CampaignProducer producer;

    /**
     * Creates a new campaign.
     *
     * @param request The request containing details of the campaign to be
     * created.
     *
     * @return The details of the newly created campaign.
     */
    public CreateCampaignResponse createCampaign(final CreateCampaignRequest request) {
        // Gets the valid email addresses.
        final var recipients = resolveRecipients(request);

        if (recipients.isEmpty()) {
            throw new IllegalArgumentException("Provide recipients or a "
                                                   + "recipient count greater than 0");
        }

        var campaign = new Campaign();
        campaign.setBody(request.body());
        campaign.setName(request.name());
        campaign.setSubject(request.subject());
        campaign.setStatus(Status.CREATED);
        campaign.setTotalRecipients(recipients.size());

        campaign = campaignRepository.save(campaign);

        producer.publish(campaign, recipients);

        campaign.setStatus(Status.RUNNING);

        campaign = campaignRepository.save(campaign);

        return map(campaign);

    }

    /**
     * Gets all the campaigns stored in the system.
     *
     * @return All the campaigns stored in the system.
     */
    public List<CreateCampaignResponse> getAllCampaigns() {
        return campaignRepository.findAllByOrderByCreatedAtDesc().stream()
                                 .map(this::map)
                                 .toList();
    }

    /**
     * Gets a campaign whose id has been provided.
     *
     * @param id The unique identifier of the campaign.
     *
     * @return A campaign.
     */
    public CreateCampaignResponse getCampaignById(final UUID id) {
        return campaignRepository.findById(id)
                                 .map(this::map)
                                 .orElseThrow(() -> new CampaignNotFoundException(id));
    }

    /**
     * Maps the Campaign entity to the response DTO.
     *
     * @param campaign The campaign whose response has to be converted.
     *
     * @return The response DTO.
     */
    private CreateCampaignResponse map(final Campaign campaign) {
        return new CreateCampaignResponse(campaign.getId()
            , campaign.getName()
            , campaign.getSubject()
            , campaign.getStatus()
            , campaign.getTotalRecipients()
            , campaign.getCreatedAt());
    }

    /**
     * Resolves the recipients for a campaign creation request.
     *
     * <p>
     * If the request contains recipients, their values are trimmed and
     * duplicate recipients are removed. If no recipients are provided, a list
     * of example recipients is generated based on the requested recipient count.
     * </p>
     *
     * @param request The campaign creation request.
     *
     * @return A list of recipient email address.
     */
    private List<String> resolveRecipients(final CreateCampaignRequest request) {
        if (request.recipients() != null && !request.recipients().isEmpty()) {
            return request.recipients().stream()
                          .map(String::trim)
                          .distinct()
                          .toList();
        }

        var count = request.recipientCount() == null ? 0 : request.recipientCount();

        return IntStream.rangeClosed(1, count)
                        .mapToObj(i -> "user" + i + "@example.com").toList();
    }
}
