package com.emap.web.rest;

import com.emap.domain.TrackingList;
import com.emap.repository.TrackingListRepository;
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
 * REST controller for managing {@link com.emap.domain.TrackingList}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TrackingListResource {

    private final Logger log = LoggerFactory.getLogger(TrackingListResource.class);

    private static final String ENTITY_NAME = "trackingList";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TrackingListRepository trackingListRepository;

    public TrackingListResource(TrackingListRepository trackingListRepository) {
        this.trackingListRepository = trackingListRepository;
    }

    /**
     * {@code POST  /tracking-lists} : Create a new trackingList.
     *
     * @param trackingList the trackingList to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new trackingList, or with status {@code 400 (Bad Request)} if the trackingList has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/tracking-lists")
    public ResponseEntity<TrackingList> createTrackingList(@Valid @RequestBody TrackingList trackingList) throws URISyntaxException {
        log.debug("REST request to save TrackingList : {}", trackingList);
        if (trackingList.getId() != null) {
            throw new BadRequestAlertException("A new trackingList cannot already have an ID", ENTITY_NAME, "idexists");
        }
        TrackingList result = trackingListRepository.save(trackingList);
        return ResponseEntity
            .created(new URI("/api/tracking-lists/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /tracking-lists/:id} : Updates an existing trackingList.
     *
     * @param id the id of the trackingList to save.
     * @param trackingList the trackingList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingList,
     * or with status {@code 400 (Bad Request)} if the trackingList is not valid,
     * or with status {@code 500 (Internal Server Error)} if the trackingList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/tracking-lists/{id}")
    public ResponseEntity<TrackingList> updateTrackingList(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody TrackingList trackingList
    ) throws URISyntaxException {
        log.debug("REST request to update TrackingList : {}, {}", id, trackingList);
        if (trackingList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        TrackingList result = trackingListRepository.save(trackingList);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingList.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /tracking-lists/:id} : Partial updates given fields of an existing trackingList, field will ignore if it is null
     *
     * @param id the id of the trackingList to save.
     * @param trackingList the trackingList to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated trackingList,
     * or with status {@code 400 (Bad Request)} if the trackingList is not valid,
     * or with status {@code 404 (Not Found)} if the trackingList is not found,
     * or with status {@code 500 (Internal Server Error)} if the trackingList couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/tracking-lists/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<TrackingList> partialUpdateTrackingList(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody TrackingList trackingList
    ) throws URISyntaxException {
        log.debug("REST request to partial update TrackingList partially : {}, {}", id, trackingList);
        if (trackingList.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, trackingList.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!trackingListRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<TrackingList> result = trackingListRepository
            .findById(trackingList.getId())
            .map(existingTrackingList -> {
                if (trackingList.getMcUserId() != null) {
                    existingTrackingList.setMcUserId(trackingList.getMcUserId());
                }
                if (trackingList.getType() != null) {
                    existingTrackingList.setType(trackingList.getType());
                }
                if (trackingList.getCreateDate() != null) {
                    existingTrackingList.setCreateDate(trackingList.getCreateDate());
                }
                if (trackingList.getCreateUid() != null) {
                    existingTrackingList.setCreateUid(trackingList.getCreateUid());
                }
                if (trackingList.getLastUpdate() != null) {
                    existingTrackingList.setLastUpdate(trackingList.getLastUpdate());
                }
                if (trackingList.getLastUpdateUid() != null) {
                    existingTrackingList.setLastUpdateUid(trackingList.getLastUpdateUid());
                }

                return existingTrackingList;
            })
            .map(trackingListRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, trackingList.getId().toString())
        );
    }

    /**
     * {@code GET  /tracking-lists} : get all the trackingLists.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of trackingLists in body.
     */
    @GetMapping("/tracking-lists")
    public ResponseEntity<List<TrackingList>> getAllTrackingLists(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of TrackingLists");
        Page<TrackingList> page = trackingListRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /tracking-lists/:id} : get the "id" trackingList.
     *
     * @param id the id of the trackingList to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the trackingList, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/tracking-lists/{id}")
    public ResponseEntity<TrackingList> getTrackingList(@PathVariable Long id) {
        log.debug("REST request to get TrackingList : {}", id);
        Optional<TrackingList> trackingList = trackingListRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(trackingList);
    }

    /**
     * {@code DELETE  /tracking-lists/:id} : delete the "id" trackingList.
     *
     * @param id the id of the trackingList to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/tracking-lists/{id}")
    public ResponseEntity<Void> deleteTrackingList(@PathVariable Long id) {
        log.debug("REST request to delete TrackingList : {}", id);
        trackingListRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
