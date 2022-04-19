package com.emap.web.rest;

import com.emap.domain.ConfigSetting;
import com.emap.repository.ConfigSettingRepository;
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
 * REST controller for managing {@link com.emap.domain.ConfigSetting}.
 */
@RestController
@RequestMapping("/api")
@Transactional
public class ConfigSettingResource {

    private final Logger log = LoggerFactory.getLogger(ConfigSettingResource.class);

    private static final String ENTITY_NAME = "configSetting";

    @Value("${jhipster.clientApp.name}")
    private String applicationName;

    private final ConfigSettingRepository configSettingRepository;

    public ConfigSettingResource(ConfigSettingRepository configSettingRepository) {
        this.configSettingRepository = configSettingRepository;
    }

    /**
     * {@code POST  /config-settings} : Create a new configSetting.
     *
     * @param configSetting the configSetting to create.
     * @return the {@link ResponseEntity} with status {@code 201 (Created)} and with body the new configSetting, or with status {@code 400 (Bad Request)} if the configSetting has already an ID.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PostMapping("/config-settings")
    public ResponseEntity<ConfigSetting> createConfigSetting(@Valid @RequestBody ConfigSetting configSetting) throws URISyntaxException {
        log.debug("REST request to save ConfigSetting : {}", configSetting);
        if (configSetting.getId() != null) {
            throw new BadRequestAlertException("A new configSetting cannot already have an ID", ENTITY_NAME, "idexists");
        }
        ConfigSetting result = configSettingRepository.save(configSetting);
        return ResponseEntity
            .created(new URI("/api/config-settings/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert(applicationName, true, ENTITY_NAME, result.getId().toString()))
            .body(result);
    }

    /**
     * {@code PUT  /config-settings/:id} : Updates an existing configSetting.
     *
     * @param id the id of the configSetting to save.
     * @param configSetting the configSetting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configSetting,
     * or with status {@code 400 (Bad Request)} if the configSetting is not valid,
     * or with status {@code 500 (Internal Server Error)} if the configSetting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PutMapping("/config-settings/{id}")
    public ResponseEntity<ConfigSetting> updateConfigSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @Valid @RequestBody ConfigSetting configSetting
    ) throws URISyntaxException {
        log.debug("REST request to update ConfigSetting : {}, {}", id, configSetting);
        if (configSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configSetting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        ConfigSetting result = configSettingRepository.save(configSetting);
        return ResponseEntity
            .ok()
            .headers(HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configSetting.getId().toString()))
            .body(result);
    }

    /**
     * {@code PATCH  /config-settings/:id} : Partial updates given fields of an existing configSetting, field will ignore if it is null
     *
     * @param id the id of the configSetting to save.
     * @param configSetting the configSetting to update.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the updated configSetting,
     * or with status {@code 400 (Bad Request)} if the configSetting is not valid,
     * or with status {@code 404 (Not Found)} if the configSetting is not found,
     * or with status {@code 500 (Internal Server Error)} if the configSetting couldn't be updated.
     * @throws URISyntaxException if the Location URI syntax is incorrect.
     */
    @PatchMapping(value = "/config-settings/{id}", consumes = { "application/json", "application/merge-patch+json" })
    public ResponseEntity<ConfigSetting> partialUpdateConfigSetting(
        @PathVariable(value = "id", required = false) final Long id,
        @NotNull @RequestBody ConfigSetting configSetting
    ) throws URISyntaxException {
        log.debug("REST request to partial update ConfigSetting partially : {}, {}", id, configSetting);
        if (configSetting.getId() == null) {
            throw new BadRequestAlertException("Invalid id", ENTITY_NAME, "idnull");
        }
        if (!Objects.equals(id, configSetting.getId())) {
            throw new BadRequestAlertException("Invalid ID", ENTITY_NAME, "idinvalid");
        }

        if (!configSettingRepository.existsById(id)) {
            throw new BadRequestAlertException("Entity not found", ENTITY_NAME, "idnotfound");
        }

        Optional<ConfigSetting> result = configSettingRepository
            .findById(configSetting.getId())
            .map(existingConfigSetting -> {
                if (configSetting.getSourceType() != null) {
                    existingConfigSetting.setSourceType(configSetting.getSourceType());
                }
                if (configSetting.getMcUserId() != null) {
                    existingConfigSetting.setMcUserId(configSetting.getMcUserId());
                }
                if (configSetting.getTmlUserId() != null) {
                    existingConfigSetting.setTmlUserId(configSetting.getTmlUserId());
                }
                if (configSetting.getVmSysDefaultModeConf() != null) {
                    existingConfigSetting.setVmSysDefaultModeConf(configSetting.getVmSysDefaultModeConf());
                }
                if (configSetting.getVmSysSyncCycleConf() != null) {
                    existingConfigSetting.setVmSysSyncCycleConf(configSetting.getVmSysSyncCycleConf());
                }
                if (configSetting.getVmSysSyncCycleUnitConf() != null) {
                    existingConfigSetting.setVmSysSyncCycleUnitConf(configSetting.getVmSysSyncCycleUnitConf());
                }
                if (configSetting.getVmSysTargetDisplayNameConf() != null) {
                    existingConfigSetting.setVmSysTargetDisplayNameConf(configSetting.getVmSysTargetDisplayNameConf());
                }
                if (configSetting.getVmLiveDefaultModeConf() != null) {
                    existingConfigSetting.setVmLiveDefaultModeConf(configSetting.getVmLiveDefaultModeConf());
                }
                if (configSetting.getVmLiveDefaultTimerangeConf() != null) {
                    existingConfigSetting.setVmLiveDefaultTimerangeConf(configSetting.getVmLiveDefaultTimerangeConf());
                }
                if (configSetting.getVmLivePositionCycleConf() != null) {
                    existingConfigSetting.setVmLivePositionCycleConf(configSetting.getVmLivePositionCycleConf());
                }
                if (configSetting.getVmLivePositionCycleUnitConf() != null) {
                    existingConfigSetting.setVmLivePositionCycleUnitConf(configSetting.getVmLivePositionCycleUnitConf());
                }
                if (configSetting.getVmLiveTrackingAmplitudeConf() != null) {
                    existingConfigSetting.setVmLiveTrackingAmplitudeConf(configSetting.getVmLiveTrackingAmplitudeConf());
                }
                if (configSetting.getVmLiveTrackingAmplitudeUnitConf() != null) {
                    existingConfigSetting.setVmLiveTrackingAmplitudeUnitConf(configSetting.getVmLiveTrackingAmplitudeUnitConf());
                }
                if (configSetting.getSarSysSyncCycleConf() != null) {
                    existingConfigSetting.setSarSysSyncCycleConf(configSetting.getSarSysSyncCycleConf());
                }
                if (configSetting.getSarSysSyncCycleUnitConf() != null) {
                    existingConfigSetting.setSarSysSyncCycleUnitConf(configSetting.getSarSysSyncCycleUnitConf());
                }
                if (configSetting.getSarSysObjectDisplayName01Conf() != null) {
                    existingConfigSetting.setSarSysObjectDisplayName01Conf(configSetting.getSarSysObjectDisplayName01Conf());
                }
                if (configSetting.getSarSysObjectDisplayName02Conf() != null) {
                    existingConfigSetting.setSarSysObjectDisplayName02Conf(configSetting.getSarSysObjectDisplayName02Conf());
                }
                if (configSetting.getCreateDate() != null) {
                    existingConfigSetting.setCreateDate(configSetting.getCreateDate());
                }
                if (configSetting.getCreateUid() != null) {
                    existingConfigSetting.setCreateUid(configSetting.getCreateUid());
                }
                if (configSetting.getLastUpdate() != null) {
                    existingConfigSetting.setLastUpdate(configSetting.getLastUpdate());
                }
                if (configSetting.getLastUpdateUid() != null) {
                    existingConfigSetting.setLastUpdateUid(configSetting.getLastUpdateUid());
                }

                return existingConfigSetting;
            })
            .map(configSettingRepository::save);

        return ResponseUtil.wrapOrNotFound(
            result,
            HeaderUtil.createEntityUpdateAlert(applicationName, true, ENTITY_NAME, configSetting.getId().toString())
        );
    }

    /**
     * {@code GET  /config-settings} : get all the configSettings.
     *
     * @param pageable the pagination information.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and the list of configSettings in body.
     */
    @GetMapping("/config-settings")
    public ResponseEntity<List<ConfigSetting>> getAllConfigSettings(@org.springdoc.api.annotations.ParameterObject Pageable pageable) {
        log.debug("REST request to get a page of ConfigSettings");
        Page<ConfigSetting> page = configSettingRepository.findAll(pageable);
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(ServletUriComponentsBuilder.fromCurrentRequest(), page);
        return ResponseEntity.ok().headers(headers).body(page.getContent());
    }

    /**
     * {@code GET  /config-settings/:id} : get the "id" configSetting.
     *
     * @param id the id of the configSetting to retrieve.
     * @return the {@link ResponseEntity} with status {@code 200 (OK)} and with body the configSetting, or with status {@code 404 (Not Found)}.
     */
    @GetMapping("/config-settings/{id}")
    public ResponseEntity<ConfigSetting> getConfigSetting(@PathVariable Long id) {
        log.debug("REST request to get ConfigSetting : {}", id);
        Optional<ConfigSetting> configSetting = configSettingRepository.findById(id);
        return ResponseUtil.wrapOrNotFound(configSetting);
    }

    /**
     * {@code DELETE  /config-settings/:id} : delete the "id" configSetting.
     *
     * @param id the id of the configSetting to delete.
     * @return the {@link ResponseEntity} with status {@code 204 (NO_CONTENT)}.
     */
    @DeleteMapping("/config-settings/{id}")
    public ResponseEntity<Void> deleteConfigSetting(@PathVariable Long id) {
        log.debug("REST request to delete ConfigSetting : {}", id);
        configSettingRepository.deleteById(id);
        return ResponseEntity
            .noContent()
            .headers(HeaderUtil.createEntityDeletionAlert(applicationName, true, ENTITY_NAME, id.toString()))
            .build();
    }
}
