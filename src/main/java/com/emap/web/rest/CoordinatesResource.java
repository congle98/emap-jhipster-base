package com.emap.web.rest;

import com.emap.domain.Coordinates;
import com.emap.repository.CoordinatesRepository;
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
 * REST controller for managing {@link com.emap.domain.Coordinates}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class CoordinatesResource {

    private final Logger log = LoggerFactory.getLogger(CoordinatesResource.class);

    private static final String ENTITY_NAME = "coordinates";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final CoordinatesRepository coordinatesRepository;

    public CoordinatesResource(CoordinatesRepository coordinatesRepository) {
        this.coordinatesRepository = coordinatesRepository;
    }

    /**
     * {@code POST  /coordinates} : Create a new coordinates.
     *
     * @param coordinates the coordinates to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new coordinates, or with status {@code 400 (Bad Request)} if the coordinates has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/coordinates")
    public ResponseEntity<Coordinates> createCoordinates(@Valid @RequestBody Coordinates coordinates) throws URISyntaxException {
        log.debug("REST request to save Coordinates : {}", coordinates);
        if (coordinates.getId() != null) {
            throw new BadRequestAlertException("A new coordinates cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Coordinates result = coordinatesRepository.save(coordinates);
        return ResponseEntity
            .created(new URI("/api/coordinates/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /coordinates/:id} : Updates an existing coordinates.
     *
     * @param id the id of the coordinates to save.
     * @param coordinates the coordinates to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coordinates,
     * or with status {@code 400 (Bad Request)} if the coordinates is not valid,
     * or with status {@code 500 (Internal Server Error)} if the coordinates couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/coordinates/{id}")
    public ResponseEntity<Coordinates> updateCoordinates(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Coordinates coordinates
    ) throws URISyntaxException {
        log.debug("REST request to update Coordinates : {}, {}", id, coordinates);
        if (coordinates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coordinates.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coordinatesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Coordinates result = coordinatesRepository.save(coordinates);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coordinates.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /coordinates/:id} : Partial updates given fields of an existing coordinates, field will ignore if it is null
     *
     * @param id the id of the coordinates to save.
     * @param coordinates the coordinates to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated coordinates,
     * or with status {@code 400 (Bad Request)} if the coordinates is not valid,
     * or with status {@code 404 (Not Found)} if the coordinates is not found,
     * or with status {@code 500 (Internal Server Error)} if the coordinates couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/coordinates/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Coordinates> partialUpdateCoordinates(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Coordinates coordinates
    ) throws URISyntaxException {
        log.debug("REST request to partial update Coordinates partially : {}, {}", id, coordinates);
        if (coordinates.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, coordinates.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!coordinatesRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Coordinates> result = coordinatesRepository
            .findById(coordinates.getId())
            .map(existingCoordinates -> {
                if (coordinates.getSourceType() != null) {
                    existingCoordinates.setSourceType(coordinates.getSourceType());
                }
                if (coordinates.getMcCampaingnId() != null) {
                    existingCoordinates.setMcCampaingnId(coordinates.getMcCampaingnId());
                }
                if (coordinates.getTmlCampaignId() != null) {
                    existingCoordinates.setTmlCampaignId(coordinates.getTmlCampaignId());
                }
                if (coordinates.getLat() != null) {
                    existingCoordinates.setLat(coordinates.getLat());
                }
                if (coordinates.getLng() != null) {
                    existingCoordinates.setLng(coordinates.getLng());
                }
                if (coordinates.getRadius() != null) {
                    existingCoordinates.setRadius(coordinates.getRadius());
                }
                if (coordinates.getOpenAngle() != null) {
                    existingCoordinates.setOpenAngle(coordinates.getOpenAngle());
                }
                if (coordinates.getDirectionalAngle() != null) {
                    existingCoordinates.setDirectionalAngle(coordinates.getDirectionalAngle());
                }
                if (coordinates.getCreateDate() != null) {
                    existingCoordinates.setCreateDate(coordinates.getCreateDate());
                }
                if (coordinates.getCreateUid() != null) {
                    existingCoordinates.setCreateUid(coordinates.getCreateUid());
                }
                if (coordinates.getLastUpdate() != null) {
                    existingCoordinates.setLastUpdate(coordinates.getLastUpdate());
                }
                if (coordinates.getLastUpdateUid() != null) {
                    existingCoordinates.setLastUpdateUid(coordinates.getLastUpdateUid());
                }

                return existingCoordinates;
            })
            .map(coordinatesRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, coordinates.getId().toString())
        );
    }

    /**
     * {@code GET  /coordinates} : get all the coordinates.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of coordinates in body.
     */
    @GetMapping("/coordinates")
    public ResponseEntity<List<Coordinates>> getAllCoordinates(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Coordinates");
        Page<Coordinates> page = coordinatesRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /coordinates/:id} : get the "id" coordinates.
     *
     * @param id the id of the coordinates to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the coordinates, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/coordinates/{id}")
    public ResponseEntity<Coordinates> getCoordinates(@PathVariable Long id) {
        log.debug("REST request to get Coordinates : {}", id);
        Optional<Coordinates> coordinates = coordinatesRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(coordinates);
    }

    /**
     * {@code DELETE  /coordinates/:id} : delete the "id" coordinates.
     *
     * @param id the id of the coordinates to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/coordinates/{id}")
    public ResponseEntity<Void> deleteCoordinates(@PathVariable Long id) {
        log.debug("REST request to delete Coordinates : {}", id);
        coordinatesRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
