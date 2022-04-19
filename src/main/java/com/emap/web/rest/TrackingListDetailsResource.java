package com.emap.web.rest;

import com.emap.domain.TrackingListDetails;
import com.emap.repository.TrackingListDetailsRepository;
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
 * REST controller for managing {@link com.emap.domain.TrackingListDetails}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrackingListDetailsResource {

    private final Logger log = LoggerFactory.getLogger(TrackingListDetailsResource.class);

    private static final String ENTITY_NAME = "trackingListDetails";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackingListDetailsRepository trackingListDetailsRepository;

    public TrackingListDetailsResource(TrackingListDetailsRepository trackingListDetailsRepository) {
        this.trackingListDetailsRepository = trackingListDetailsRepository;
    }

    /**
     * {@code POST  /tracking-list-details} : Create a new trackingListDetails.
     *
     * @param trackingListDetails the trackingListDetails to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackingListDetails, or with status {@code 400 (Bad Request)} if the trackingListDetails has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tracking-list-details")
    public ResponseEntity<TrackingListDetails> createTrackingListDetails(@Valid @RequestBody TrackingListDetails trackingListDetails)
        throws URISyntaxException {
        log.debug("REST request to save TrackingListDetails : {}", trackingListDetails);
        if (trackingListDetails.getId() != null) {
            throw new BadRequestAlertException("A new trackingListDetails cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackingListDetails result = trackingListDetailsRepository.save(trackingListDetails);
        return ResponseEntity
            .created(new URI("/api/tracking-list-details/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tracking-list-details/:id} : Updates an existing trackingListDetails.
     *
     * @param id the id of the trackingListDetails to save.
     * @param trackingListDetails the trackingListDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingListDetails,
     * or with status {@code 400 (Bad Request)} if the trackingListDetails is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackingListDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tracking-list-details/{id}")
    public ResponseEntity<TrackingListDetails> updateTrackingListDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrackingListDetails trackingListDetails
    ) throws URISyntaxException {
        log.debug("REST request to update TrackingListDetails : {}, {}", id, trackingListDetails);
        if (trackingListDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingListDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingListDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackingListDetails result = trackingListDetailsRepository.save(trackingListDetails);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingListDetails.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tracking-list-details/:id} : Partial updates given fields of an existing trackingListDetails, field will ignore if it is null
     *
     * @param id the id of the trackingListDetails to save.
     * @param trackingListDetails the trackingListDetails to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingListDetails,
     * or with status {@code 400 (Bad Request)} if the trackingListDetails is not valid,
     * or with status {@code 404 (Not Found)} if the trackingListDetails is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackingListDetails couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tracking-list-details/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackingListDetails> partialUpdateTrackingListDetails(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrackingListDetails trackingListDetails
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrackingListDetails partially : {}, {}", id, trackingListDetails);
        if (trackingListDetails.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingListDetails.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingListDetailsRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackingListDetails> result = trackingListDetailsRepository
            .findById(trackingListDetails.getId())
            .map(existingTrackingListDetails -> {
                if (trackingListDetails.getCreateDate() != null) {
                    existingTrackingListDetails.setCreateDate(trackingListDetails.getCreateDate());
                }
                if (trackingListDetails.getCreateUid() != null) {
                    existingTrackingListDetails.setCreateUid(trackingListDetails.getCreateUid());
                }
                if (trackingListDetails.getLastUpdate() != null) {
                    existingTrackingListDetails.setLastUpdate(trackingListDetails.getLastUpdate());
                }
                if (trackingListDetails.getLastUpdateUid() != null) {
                    existingTrackingListDetails.setLastUpdateUid(trackingListDetails.getLastUpdateUid());
                }

                return existingTrackingListDetails;
            })
            .map(trackingListDetailsRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingListDetails.getId().toString())
        );
    }

    /**
     * {@code GET  /tracking-list-details} : get all the trackingListDetails.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackingListDetails in body.
     */
    @GetMapping("/tracking-list-details")
    public ResponseEntity<List<TrackingListDetails>> getAllTrackingListDetails(
        @org.springdoc.api.annotations.ParameterObject Pageable pageable
    ) {
        log.debug("REST request to get a page of TrackingListDetails");
        Page<TrackingListDetails> page = trackingListDetailsRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tracking-list-details/:id} : get the "id" trackingListDetails.
     *
     * @param id the id of the trackingListDetails to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackingListDetails, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tracking-list-details/{id}")
    public ResponseEntity<TrackingListDetails> getTrackingListDetails(@PathVariable Long id) {
        log.debug("REST request to get TrackingListDetails : {}", id);
        Optional<TrackingListDetails> trackingListDetails = trackingListDetailsRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trackingListDetails);
    }

    /**
     * {@code DELETE  /tracking-list-details/:id} : delete the "id" trackingListDetails.
     *
     * @param id the id of the trackingListDetails to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tracking-list-details/{id}")
    public ResponseEntity<Void> deleteTrackingListDetails(@PathVariable Long id) {
        log.debug("REST request to delete TrackingListDetails : {}", id);
        trackingListDetailsRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
