package com.emap.web.rest;

import com.emap.domain.WarningMessage;
import com.emap.repository.WarningMessageRepository;
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
 * REST controller for managing {@link com.emap.domain.WarningMessage}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class WarningMessageResource {

    private final Logger log = LoggerFactory.getLogger(WarningMessageResource.class);

    private static final String ENTITY_NAME = "warningMessage";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final WarningMessageRepository warningMessageRepository;

    public WarningMessageResource(WarningMessageRepository warningMessageRepository) {
        this.warningMessageRepository = warningMessageRepository;
    }

    /**
     * {@code POST  /warning-messages} : Create a new warningMessage.
     *
     * @param warningMessage the warningMessage to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new warningMessage, or with status {@code 400 (Bad Request)} if the warningMessage has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/warning-messages")
    public ResponseEntity<WarningMessage> createWarningMessage(@Valid @RequestBody WarningMessage warningMessage)
        throws URISyntaxException {
        log.debug("REST request to save WarningMessage : {}", warningMessage);
        if (warningMessage.getId() != null) {
            throw new BadRequestAlertException("A new warningMessage cannot already have an ID", ENTITY_NAME, "idexists");
        }
        WarningMessage result = warningMessageRepository.save(warningMessage);
        return ResponseEntity
            .created(new URI("/api/warning-messages/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /warning-messages/:id} : Updates an existing warningMessage.
     *
     * @param id the id of the warningMessage to save.
     * @param warningMessage the warningMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warningMessage,
     * or with status {@code 400 (Bad Request)} if the warningMessage is not valid,
     * or with status {@code 500 (Internal Server Error)} if the warningMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/warning-messages/{id}")
    public ResponseEntity<WarningMessage> updateWarningMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody WarningMessage warningMessage
    ) throws URISyntaxException {
        log.debug("REST request to update WarningMessage : {}, {}", id, warningMessage);
        if (warningMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warningMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warningMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        WarningMessage result = warningMessageRepository.save(warningMessage);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warningMessage.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /warning-messages/:id} : Partial updates given fields of an existing warningMessage, field will ignore if it is null
     *
     * @param id the id of the warningMessage to save.
     * @param warningMessage the warningMessage to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated warningMessage,
     * or with status {@code 400 (Bad Request)} if the warningMessage is not valid,
     * or with status {@code 404 (Not Found)} if the warningMessage is not found,
     * or with status {@code 500 (Internal Server Error)} if the warningMessage couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/warning-messages/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<WarningMessage> partialUpdateWarningMessage(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody WarningMessage warningMessage
    ) throws URISyntaxException {
        log.debug("REST request to partial update WarningMessage partially : {}, {}", id, warningMessage);
        if (warningMessage.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, warningMessage.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!warningMessageRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<WarningMessage> result = warningMessageRepository
            .findById(warningMessage.getId())
            .map(existingWarningMessage -> {
                if (warningMessage.getMcUserId() != null) {
                    existingWarningMessage.setMcUserId(warningMessage.getMcUserId());
                }
                if (warningMessage.getWarningDistance() != null) {
                    existingWarningMessage.setWarningDistance(warningMessage.getWarningDistance());
                }
                if (warningMessage.getShowWarningCircle() != null) {
                    existingWarningMessage.setShowWarningCircle(warningMessage.getShowWarningCircle());
                }
                if (warningMessage.getShowWarningMessage() != null) {
                    existingWarningMessage.setShowWarningMessage(warningMessage.getShowWarningMessage());
                }
                if (warningMessage.getWarningMessage() != null) {
                    existingWarningMessage.setWarningMessage(warningMessage.getWarningMessage());
                }
                if (warningMessage.getSendWarningMessageToMc() != null) {
                    existingWarningMessage.setSendWarningMessageToMc(warningMessage.getSendWarningMessageToMc());
                }
                if (warningMessage.getCreateDate() != null) {
                    existingWarningMessage.setCreateDate(warningMessage.getCreateDate());
                }
                if (warningMessage.getCreateUid() != null) {
                    existingWarningMessage.setCreateUid(warningMessage.getCreateUid());
                }
                if (warningMessage.getLastUpdate() != null) {
                    existingWarningMessage.setLastUpdate(warningMessage.getLastUpdate());
                }
                if (warningMessage.getLastUpdateUid() != null) {
                    existingWarningMessage.setLastUpdateUid(warningMessage.getLastUpdateUid());
                }

                return existingWarningMessage;
            })
            .map(warningMessageRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, warningMessage.getId().toString())
        );
    }

    /**
     * {@code GET  /warning-messages} : get all the warningMessages.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of warningMessages in body.
     */
    @GetMapping("/warning-messages")
    public ResponseEntity<List<WarningMessage>> getAllWarningMessages(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of WarningMessages");
        Page<WarningMessage> page = warningMessageRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /warning-messages/:id} : get the "id" warningMessage.
     *
     * @param id the id of the warningMessage to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the warningMessage, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/warning-messages/{id}")
    public ResponseEntity<WarningMessage> getWarningMessage(@PathVariable Long id) {
        log.debug("REST request to get WarningMessage : {}", id);
        Optional<WarningMessage> warningMessage = warningMessageRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(warningMessage);
    }

    /**
     * {@code DELETE  /warning-messages/:id} : delete the "id" warningMessage.
     *
     * @param id the id of the warningMessage to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/warning-messages/{id}")
    public ResponseEntity<Void> deleteWarningMessage(@PathVariable Long id) {
        log.debug("REST request to delete WarningMessage : {}", id);
        warningMessageRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
