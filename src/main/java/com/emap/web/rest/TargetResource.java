package com.emap.web.rest;

import com.emap.domain.Target;
import com.emap.repository.TargetRepository;
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
 * REST controller for managing {@link com.emap.domain.Target}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class TargetResource {

    private final Logger log = LoggerFactory.getLogger(TargetResource.class);

    private static final String ENTITY_NAME = "target";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final TargetRepository targetRepository;

    public TargetResource(TargetRepository targetRepository) {
        this.targetRepository = targetRepository;
    }

    /**
     * {@code POST  /targets} : Create a new target.
     *
     * @param target the target to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new target, or with status {@code 400 (Bad Request)} if the target has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/targets")
    public ResponseEntity<Target> createTarget(@Valid @RequestBody Target target) throws URISyntaxException {
        log.debug("REST request to save Target : {}", target);
        if (target.getId() != null) {
            throw new BadRequestAlertException("A new target cannot already have an ID", ENTITY_NAME, "idexists");
        }
        Target result = targetRepository.save(target);
        return ResponseEntity
            .created(new URI("/api/targets/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /targets/:id} : Updates an existing target.
     *
     * @param id the id of the target to save.
     * @param target the target to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated target,
     * or with status {@code 400 (Bad Request)} if the target is not valid,
     * or with status {@code 500 (Internal Server Error)} if the target couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/targets/{id}")
    public ResponseEntity<Target> updateTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody Target target
    ) throws URISyntaxException {
        log.debug("REST request to update Target : {}, {}", id, target);
        if (target.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, target.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Target result = targetRepository.save(target);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, target.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /targets/:id} : Partial updates given fields of an existing target, field will ignore if it is null
     *
     * @param id the id of the target to save.
     * @param target the target to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated target,
     * or with status {@code 400 (Bad Request)} if the target is not valid,
     * or with status {@code 404 (Not Found)} if the target is not found,
     * or with status {@code 500 (Internal Server Error)} if the target couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/targets/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<Target> partialUpdateTarget(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody Target target
    ) throws URISyntaxException {
        log.debug("REST request to partial update Target partially : {}, {}", id, target);
        if (target.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, target.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!targetRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<Target> result = targetRepository
            .findById(target.getId())
            .map(existingTarget -> {
                if (target.getSourceType() != null) {
                    existingTarget.setSourceType(target.getSourceType());
                }
                if (target.getMcCampaingnId() != null) {
                    existingTarget.setMcCampaingnId(target.getMcCampaingnId());
                }
                if (target.getTmlCampaignId() != null) {
                    existingTarget.setTmlCampaignId(target.getTmlCampaignId());
                }
                if (target.getIcon() != null) {
                    existingTarget.setIcon(target.getIcon());
                }
                if (target.getColor() != null) {
                    existingTarget.setColor(target.getColor());
                }
                if (target.getCreateDate() != null) {
                    existingTarget.setCreateDate(target.getCreateDate());
                }
                if (target.getCreateUid() != null) {
                    existingTarget.setCreateUid(target.getCreateUid());
                }
                if (target.getLastUpdate() != null) {
                    existingTarget.setLastUpdate(target.getLastUpdate());
                }
                if (target.getLastUpdateUid() != null) {
                    existingTarget.setLastUpdateUid(target.getLastUpdateUid());
                }

                return existingTarget;
            })
            .map(targetRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, target.getId().toString())
        );
    }

    /**
     * {@code GET  /targets} : get all the targets.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of targets in body.
     */
    @GetMapping("/targets")
    public ResponseEntity<List<Target>> getAllTargets(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of Targets");
        Page<Target> page = targetRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /targets/:id} : get the "id" target.
     *
     * @param id the id of the target to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the target, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/targets/{id}")
    public ResponseEntity<Target> getTarget(@PathVariable Long id) {
        log.debug("REST request to get Target : {}", id);
        Optional<Target> target = targetRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(target);
    }

    /**
     * {@code DELETE  /targets/:id} : delete the "id" target.
     *
     * @param id the id of the target to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/targets/{id}")
    public ResponseEntity<Void> deleteTarget(@PathVariable Long id) {
        log.debug("REST request to delete Target : {}", id);
        targetRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
