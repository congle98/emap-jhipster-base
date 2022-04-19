package com.emap.web.rest;

import com.emap.domain.StaticLocation;
import com.emap.repository.StaticLocationRepository;
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
 * REST controller for managing {@link com.emap.domain.StaticLocation}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class StaticLocationResource {

    private final Logger log = LoggerFactory.getLogger(StaticLocationResource.class);

    private static final String ENTITY_NAME = "staticLocation";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final StaticLocationRepository staticLocationRepository;

    public StaticLocationResource(StaticLocationRepository staticLocationRepository) {
        this.staticLocationRepository = staticLocationRepository;
    }

    /**
     * {@code POST  /static-locations} : Create a new staticLocation.
     *
     * @param staticLocation the staticLocation to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new staticLocation, or with status {@code 400 (Bad Request)} if the staticLocation has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/static-locations")
    public ResponseEntity<StaticLocation> createStaticLocation(@Valid @RequestBody StaticLocation staticLocation)
        throws URISyntaxException {
        log.debug("REST request to save StaticLocation : {}", staticLocation);
        if (staticLocation.getId() != null) {
            throw new BadRequestAlertException("A new staticLocation cannot already have an ID", ENTITY_NAME, "idexists");
        }
        StaticLocation result = staticLocationRepository.save(staticLocation);
        return ResponseEntity
            .created(new URI("/api/static-locations/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /static-locations/:id} : Updates an existing staticLocation.
     *
     * @param id the id of the staticLocation to save.
     * @param staticLocation the staticLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticLocation,
     * or with status {@code 400 (Bad Request)} if the staticLocation is not valid,
     * or with status {@code 500 (Internal Server Error)} if the staticLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/static-locations/{id}")
    public ResponseEntity<StaticLocation> updateStaticLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody StaticLocation staticLocation
    ) throws URISyntaxException {
        log.debug("REST request to update StaticLocation : {}, {}", id, staticLocation);
        if (staticLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        StaticLocation result = staticLocationRepository.save(staticLocation);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticLocation.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /static-locations/:id} : Partial updates given fields of an existing staticLocation, field will ignore if it is null
     *
     * @param id the id of the staticLocation to save.
     * @param staticLocation the staticLocation to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated staticLocation,
     * or with status {@code 400 (Bad Request)} if the staticLocation is not valid,
     * or with status {@code 404 (Not Found)} if the staticLocation is not found,
     * or with status {@code 500 (Internal Server Error)} if the staticLocation couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/static-locations/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<StaticLocation> partialUpdateStaticLocation(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody StaticLocation staticLocation
    ) throws URISyntaxException {
        log.debug("REST request to partial update StaticLocation partially : {}, {}", id, staticLocation);
        if (staticLocation.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, staticLocation.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!staticLocationRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<StaticLocation> result = staticLocationRepository
            .findById(staticLocation.getId())
            .map(existingStaticLocation -> {
                if (staticLocation.getName() != null) {
                    existingStaticLocation.setName(staticLocation.getName());
                }
                if (staticLocation.getMcUserId() != null) {
                    existingStaticLocation.setMcUserId(staticLocation.getMcUserId());
                }
                if (staticLocation.getAddress() != null) {
                    existingStaticLocation.setAddress(staticLocation.getAddress());
                }
                if (staticLocation.getLat() != null) {
                    existingStaticLocation.setLat(staticLocation.getLat());
                }
                if (staticLocation.getLng() != null) {
                    existingStaticLocation.setLng(staticLocation.getLng());
                }
                if (staticLocation.getStatus() != null) {
                    existingStaticLocation.setStatus(staticLocation.getStatus());
                }
                if (staticLocation.getCreateDate() != null) {
                    existingStaticLocation.setCreateDate(staticLocation.getCreateDate());
                }
                if (staticLocation.getCreateUid() != null) {
                    existingStaticLocation.setCreateUid(staticLocation.getCreateUid());
                }
                if (staticLocation.getLastUpdate() != null) {
                    existingStaticLocation.setLastUpdate(staticLocation.getLastUpdate());
                }
                if (staticLocation.getLastUpdateUid() != null) {
                    existingStaticLocation.setLastUpdateUid(staticLocation.getLastUpdateUid());
                }

                return existingStaticLocation;
            })
            .map(staticLocationRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, staticLocation.getId().toString())
        );
    }

    /**
     * {@code GET  /static-locations} : get all the staticLocations.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of staticLocations in body.
     */
    @GetMapping("/static-locations")
    public ResponseEntity<List<StaticLocation>> getAllStaticLocations(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of StaticLocations");
        Page<StaticLocation> page = staticLocationRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /static-locations/:id} : get the "id" staticLocation.
     *
     * @param id the id of the staticLocation to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the staticLocation, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/static-locations/{id}")
    public ResponseEntity<StaticLocation> getStaticLocation(@PathVariable Long id) {
        log.debug("REST request to get StaticLocation : {}", id);
        Optional<StaticLocation> staticLocation = staticLocationRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(staticLocation);
    }

    /**
     * {@code DELETE  /static-locations/:id} : delete the "id" staticLocation.
     *
     * @param id the id of the staticLocation to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/static-locations/{id}")
    public ResponseEntity<Void> deleteStaticLocation(@PathVariable Long id) {
        log.debug("REST request to delete StaticLocation : {}", id);
        staticLocationRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
