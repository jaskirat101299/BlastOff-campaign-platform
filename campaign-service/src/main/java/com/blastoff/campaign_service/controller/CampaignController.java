package com.blastoff.campaign_service.controller;

import com.blastoff.campaign_service.dto.CreateCampaignRequest;
import com.blastoff.campaign_service.dto.CreateCampaignResponse;
import com.blastoff.campaign_service.service.CampaignService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/campaign")
@RequiredArgsConstructor
public class CampaignController {
    private final CampaignService campaignService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public CreateCampaignResponse createCampaign(@Valid @RequestBody final CreateCampaignRequest request) {
        return campaignService.createCampaign(request);
    }

    @GetMapping("/{id}")
    public CreateCampaignResponse getCampaignById(@PathVariable final UUID id) {
        return campaignService.getCampaignById(id);
    }

    @GetMapping
    public List<CreateCampaignResponse> getAllCampaigns() {
        return campaignService.getAllCampaigns();
    }
}
