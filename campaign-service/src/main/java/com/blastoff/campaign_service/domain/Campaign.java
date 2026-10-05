package com.blastoff.campaign_service.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

/**
 * Represents a campaign message.
 */
@Entity
@Table(name = "campaigns")
@Getter
@Setter
@NoArgsConstructor
public class Campaign {
    /**
     * The unique identifier of the campaign.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    /**
     * The content of the message.
     */
    @Column(nullable = false, length = 5000)
    private String body;

    /**
     * The timestamp indicating when the campaign was created.
     */
    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    /**
     * The name of the campaign.
     */
    @Column(nullable = false)
    private String name;

    /**
     * The current status of the campaign.
     */
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private Status status;

    /**
     * The subject of the campaign message.
     */
    @Column(nullable = false)
    private String subject;

    /**
     * The total number of campaigns targeted by this campaign.
     */
    @Column(nullable = false)
    private int totalRecipients;

    /**
     * Initializes the campaign creation timestamp before the entity
     * is persisted for the first time.
     */
    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
    }
}