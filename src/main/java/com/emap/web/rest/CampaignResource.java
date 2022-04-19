package com.emap.web.rest;

import com.emap.domain.Campaign;
import com.emap.repository.CampaignRepository;
import com.emap.web.rest.errors.BadRequestAlertException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tech.jhipster.web.util.HeaderUtil;
import tech.jhipster.web.util.PaginationUtil;
import tech.jhipster.web.util.ResponseUtil;

/**
 * REST controller for managing {@link com.emap.domain.Campaign}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CampaignResource {

    private final Logger log = LoggerFactory.getLogger(CampaignResource.class);

    private static final String ENTITY_NAME = "campaign";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CampaignRepository campaignRepository;

    public CampaignResource(CampaignRepository campaignRepository) {
        this.campaignRepository = campaignRepository;
    }

    /**
     * {@code POST  /campaigns} : Create a new campaign.
     *
     * @param campaign the campaign to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new campaign, or with status {@code 400 (Bad Request)} if the campaign has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/campaigns")
    public ResponseEntity<Campaign> createCampaign(@Valid @RequestBody Campaign campaign) throws URISyntaxException {
        log.debug("REST request to save Campaign : {}", campaign);
        if (campaign.getId() != null) {
            throw new BadRequestAlertException("A new campaign cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Campaign result = campaignRepository.save(campaign);
        return ResponseEntity
            .created(new URI("/api/campaigns/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /campaigns/:id} : Updates an existing campaign.
     *
     * @param id the id of the campaign to save.
     * @param campaign the campaign to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaign,
     * or with status {@code 400 (Bad Request)} if the campaign is not valid,
     * or with status {@code 500 (Internal Server Error)} if the campaign couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/campaigns/{id}")
    public ResponseEntity<Campaign> updateCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Campaign campaign
    ) throws URISyntaxException {
        log.debug("REST request to update Campaign : {}, {}", id, campaign);
        if (campaign.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaign.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!campaignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Campaign result = campaignRepository.save(campaign);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campaign.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /campaigns/:id} : Partial updates given fields of an existing campaign, field will ignore if it is null
     *
     * @param id the id of the campaign to save.
     * @param campaign the campaign to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated campaign,
     * or with status {@code 400 (Bad Request)} if the campaign is not valid,
     * or with status {@code 404 (Not Found)} if the campaign is not found,
     * or with status {@code 500 (Internal Server Error)} if the campaign couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/campaigns/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Campaign> partialUpdateCampaign(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Campaign campaign
    ) throws URISyntaxException {
        log.debug("REST request to partial update Campaign partially : {}, {}", id, campaign);
        if (campaign.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, campaign.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!campaignRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Campaign> result = campaignRepository
            .findById(campaign.getId())
            .map(existingCampaign -> {
                if (campaign.getSourceType() != null) {
                    existingCampaign.setSourceType(campaign.getSourceType());
                }
                if (campaign.getMcCampaingnId() != null) {
                    existingCampaign.setMcCampaingnId(campaign.getMcCampaingnId());
                }
                if (campaign.getTmlCampaignId() != null) {
                    existingCampaign.setTmlCampaignId(campaign.getTmlCampaignId());
                }
                if (campaign.getIcon() != null) {
                    existingCampaign.setIcon(campaign.getIcon());
                }
                if (campaign.getColor() != null) {
                    existingCampaign.setColor(campaign.getColor());
                }
                if (campaign.getCreateDate() != null) {
                    existingCampaign.setCreateDate(campaign.getCreateDate());
                }
                if (campaign.getCreateUid() != null) {
                    existingCampaign.setCreateUid(campaign.getCreateUid());
                }
                if (campaign.getLastUpdate() != null) {
                    existingCampaign.setLastUpdate(campaign.getLastUpdate());
                }
                if (campaign.getLastUpdateUid() != null) {
                    existingCampaign.setLastUpdateUid(campaign.getLastUpdateUid());
                }

                return existingCampaign;
            })
            .map(campaignRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, campaign.getId().toString())
        );
    }

    /**
     * {@code GET  /campaigns} : get all the campaigns.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of campaigns in body.
     */
    @GetMapping("/campaigns")
    public ResponseEntity<List<Campaign>> getAllCampaigns(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Campaigns");
        Page<Campaign> page = campaignRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /campaigns/:id} : get the "id" campaign.
     *
     * @param id the id of the campaign to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the campaign, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/campaigns/{id}")
    public ResponseEntity<Campaign> getCampaign(@PathVariable Long id) {
        log.debug("REST request to get Campaign : {}", id);
        Optional<Campaign> campaign = campaignRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(campaign);
    }

    /**
     * {@code DELETE  /campaigns/:id} : delete the "id" campaign.
     *
     * @param id the id of the campaign to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/campaigns/{id}")
    public ResponseEntity<Void> deleteCampaign(@PathVariable Long id) {
        log.debug("REST request to delete Campaign : {}", id);
        campaignRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
