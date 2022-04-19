package com.emap.web.rest;

import com.emap.domain.WarningRule;
import com.emap.repository.WarningRuleRepository;
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
 * REST controller for managing {@link com.emap.domain.WarningRule}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WarningRuleResource {

    private final Logger log = LoggerFactory.getLogger(WarningRuleResource.class);

    private static final String ENTITY_NAME = "warningRule";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarningRuleRepository warningRuleRepository;

    public WarningRuleResource(WarningRuleRepository warningRuleRepository) {
        this.warningRuleRepository = warningRuleRepository;
    }

    /**
     * {@code POST  /warning-rules} : Create a new warningRule.
     *
     * @param warningRule the warningRule to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warningRule, or with status {@code 400 (Bad Request)} if the warningRule has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/warning-rules")
    public ResponseEntity<WarningRule> createWarningRule(@Valid @RequestBody WarningRule warningRule) throws URISyntaxException {
        log.debug("REST request to save WarningRule : {}", warningRule);
        if (warningRule.getId() != null) {
            throw new BadRequestAlertException("A new warningRule cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WarningRule result = warningRuleRepository.save(warningRule);
        return ResponseEntity
            .created(new URI("/api/warning-rules/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /warning-rules/:id} : Updates an existing warningRule.
     *
     * @param id the id of the warningRule to save.
     * @param warningRule the warningRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warningRule,
     * or with status {@code 400 (Bad Request)} if the warningRule is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warningRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/warning-rules/{id}")
    public ResponseEntity<WarningRule> updateWarningRule(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WarningRule warningRule
    ) throws URISyntaxException {
        log.debug("REST request to update WarningRule : {}, {}", id, warningRule);
        if (warningRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warningRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warningRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WarningRule result = warningRuleRepository.save(warningRule);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warningRule.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /warning-rules/:id} : Partial updates given fields of an existing warningRule, field will ignore if it is null
     *
     * @param id the id of the warningRule to save.
     * @param warningRule the warningRule to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warningRule,
     * or with status {@code 400 (Bad Request)} if the warningRule is not valid,
     * or with status {@code 404 (Not Found)} if the warningRule is not found,
     * or with status {@code 500 (Internal Server Error)} if the warningRule couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/warning-rules/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WarningRule> partialUpdateWarningRule(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WarningRule warningRule
    ) throws URISyntaxException {
        log.debug("REST request to partial update WarningRule partially : {}, {}", id, warningRule);
        if (warningRule.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warningRule.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warningRuleRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WarningRule> result = warningRuleRepository
            .findById(warningRule.getId())
            .map(existingWarningRule -> {
                if (warningRule.getName() != null) {
                    existingWarningRule.setName(warningRule.getName());
                }
                if (warningRule.getMcUserId() != null) {
                    existingWarningRule.setMcUserId(warningRule.getMcUserId());
                }
                if (warningRule.getDelayCheck() != null) {
                    existingWarningRule.setDelayCheck(warningRule.getDelayCheck());
                }
                if (warningRule.getDelayCheckUnit() != null) {
                    existingWarningRule.setDelayCheckUnit(warningRule.getDelayCheckUnit());
                }
                if (warningRule.getConditionType() != null) {
                    existingWarningRule.setConditionType(warningRule.getConditionType());
                }
                if (warningRule.getIncludeMcCampaignId() != null) {
                    existingWarningRule.setIncludeMcCampaignId(warningRule.getIncludeMcCampaignId());
                }
                if (warningRule.getIncludeMcTargetId() != null) {
                    existingWarningRule.setIncludeMcTargetId(warningRule.getIncludeMcTargetId());
                }
                if (warningRule.getWarningDistance() != null) {
                    existingWarningRule.setWarningDistance(warningRule.getWarningDistance());
                }
                if (warningRule.getShowWarningCircle() != null) {
                    existingWarningRule.setShowWarningCircle(warningRule.getShowWarningCircle());
                }
                if (warningRule.getShowWarningMessage() != null) {
                    existingWarningRule.setShowWarningMessage(warningRule.getShowWarningMessage());
                }
                if (warningRule.getWarningMessage() != null) {
                    existingWarningRule.setWarningMessage(warningRule.getWarningMessage());
                }
                if (warningRule.getSendWarningMessageToMc() != null) {
                    existingWarningRule.setSendWarningMessageToMc(warningRule.getSendWarningMessageToMc());
                }
                if (warningRule.getStatus() != null) {
                    existingWarningRule.setStatus(warningRule.getStatus());
                }
                if (warningRule.getCreateDate() != null) {
                    existingWarningRule.setCreateDate(warningRule.getCreateDate());
                }
                if (warningRule.getCreateUid() != null) {
                    existingWarningRule.setCreateUid(warningRule.getCreateUid());
                }
                if (warningRule.getLastUpdate() != null) {
                    existingWarningRule.setLastUpdate(warningRule.getLastUpdate());
                }
                if (warningRule.getLastUpdateUid() != null) {
                    existingWarningRule.setLastUpdateUid(warningRule.getLastUpdateUid());
                }

                return existingWarningRule;
            })
            .map(warningRuleRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warningRule.getId().toString())
        );
    }

    /**
     * {@code GET  /warning-rules} : get all the warningRules.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warningRules in body.
     */
    @GetMapping("/warning-rules")
    public ResponseEntity<List<WarningRule>> getAllWarningRules(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WarningRules");
        Page<WarningRule> page = warningRuleRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warning-rules/:id} : get the "id" warningRule.
     *
     * @param id the id of the warningRule to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warningRule, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/warning-rules/{id}")
    public ResponseEntity<WarningRule> getWarningRule(@PathVariable Long id) {
        log.debug("REST request to get WarningRule : {}", id);
        Optional<WarningRule> warningRule = warningRuleRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(warningRule);
    }

    /**
     * {@code DELETE  /warning-rules/:id} : delete the "id" warningRule.
     *
     * @param id the id of the warningRule to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/warning-rules/{id}")
    public ResponseEntity<Void> deleteWarningRule(@PathVariable Long id) {
        log.debug("REST request to delete WarningRule : {}", id);
        warningRuleRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
