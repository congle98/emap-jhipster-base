package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.Target;
import com.emap.repository.TargetRepository;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link TargetResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TargetResourceIT {

    private static final String DEFAULT_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MC_CAMPAINGN_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_CAMPAINGN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TML_CAMPAIGN_ID = "AAAAAAAAAA";
    private static final String UPDATED_TML_CAMPAIGN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ICON = "AAAAAAAAAA";
    private static final String UPDATED_ICON = "BBBBBBBBBB";

    private static final String DEFAULT_COLOR = "AAAAAAAAAA";
    private static final String UPDATED_COLOR = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/targets";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TargetRepository targetRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTargetMockMvc;

    private Target target;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Target createEntity(EntityManager em) {
        Target target = new Target()
            .sourceType(DEFAULT_SOURCE_TYPE)
            .mcCampaingnId(DEFAULT_MC_CAMPAINGN_ID)
            .tmlCampaignId(DEFAULT_TML_CAMPAIGN_ID)
            .icon(DEFAULT_ICON)
            .color(DEFAULT_COLOR)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return target;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Target createUpdatedEntity(EntityManager em) {
        Target target = new Target()
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return target;
    }

    @BeforeEach
    public void initTest() {
        target = createEntity(em);
    }

    @Test
    @Transactional
    void createTarget() throws Exception {
        int databaseSizeBeforeCreate = targetRepository.findAll().size();
        // Create the Target
        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isCreated());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeCreate + 1);
        Target testTarget = targetList.get(targetList.size() - 1);
        assertThat(testTarget.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testTarget.getMcCampaingnId()).isEqualTo(DEFAULT_MC_CAMPAINGN_ID);
        assertThat(testTarget.getTmlCampaignId()).isEqualTo(DEFAULT_TML_CAMPAIGN_ID);
        assertThat(testTarget.getIcon()).isEqualTo(DEFAULT_ICON);
        assertThat(testTarget.getColor()).isEqualTo(DEFAULT_COLOR);
        assertThat(testTarget.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTarget.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTarget.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testTarget.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createTargetWithExistingId() throws Exception {
        // Create the Target with an existing ID
        target.setId(1L);

        int databaseSizeBeforeCreate = targetRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetRepository.findAll().size();
        // set the field null
        target.setSourceType(null);

        // Create the Target, which fails.

        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetRepository.findAll().size();
        // set the field null
        target.setCreateDate(null);

        // Create the Target, which fails.

        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetRepository.findAll().size();
        // set the field null
        target.setCreateUid(null);

        // Create the Target, which fails.

        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetRepository.findAll().size();
        // set the field null
        target.setLastUpdate(null);

        // Create the Target, which fails.

        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = targetRepository.findAll().size();
        // set the field null
        target.setLastUpdateUid(null);

        // Create the Target, which fails.

        restTargetMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isBadRequest());

        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTargets() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        // Get all the targetList
        restTargetMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(target.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].mcCampaingnId").value(hasItem(DEFAULT_MC_CAMPAINGN_ID)))
            .andExpect(jsonPath("$.[*].tmlCampaignId").value(hasItem(DEFAULT_TML_CAMPAIGN_ID)))
            .andExpect(jsonPath("$.[*].icon").value(hasItem(DEFAULT_ICON)))
            .andExpect(jsonPath("$.[*].color").value(hasItem(DEFAULT_COLOR)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getTarget() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        // Get the target
        restTargetMockMvc
            .perform(get(ENTITY_API_URL_ID, target.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(target.getId().intValue()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE))
            .andExpect(jsonPath("$.mcCampaingnId").value(DEFAULT_MC_CAMPAINGN_ID))
            .andExpect(jsonPath("$.tmlCampaignId").value(DEFAULT_TML_CAMPAIGN_ID))
            .andExpect(jsonPath("$.icon").value(DEFAULT_ICON))
            .andExpect(jsonPath("$.color").value(DEFAULT_COLOR))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingTarget() throws Exception {
        // Get the target
        restTargetMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTarget() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        int databaseSizeBeforeUpdate = targetRepository.findAll().size();

        // Update the target
        Target updatedTarget = targetRepository.findById(target.getId()).get();
        // Disconnect from session so that the updates on updatedTarget are not directly saved in db
        em.detach(updatedTarget);
        updatedTarget
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTarget.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTarget))
            )
            .andExpect(status().isOk());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
        Target testTarget = targetList.get(targetList.size() - 1);
        assertThat(testTarget.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testTarget.getMcCampaingnId()).isEqualTo(UPDATED_MC_CAMPAINGN_ID);
        assertThat(testTarget.getTmlCampaignId()).isEqualTo(UPDATED_TML_CAMPAIGN_ID);
        assertThat(testTarget.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTarget.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTarget.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTarget.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTarget.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTarget.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, target.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(target))
            )
            .andExpect(status().isBadRequest());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(target))
            )
            .andExpect(status().isBadRequest());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTargetWithPatch() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        int databaseSizeBeforeUpdate = targetRepository.findAll().size();

        // Update the target using partial update
        Target partialUpdatedTarget = new Target();
        partialUpdatedTarget.setId(target.getId());

        partialUpdatedTarget
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarget))
            )
            .andExpect(status().isOk());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
        Target testTarget = targetList.get(targetList.size() - 1);
        assertThat(testTarget.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testTarget.getMcCampaingnId()).isEqualTo(UPDATED_MC_CAMPAINGN_ID);
        assertThat(testTarget.getTmlCampaignId()).isEqualTo(UPDATED_TML_CAMPAIGN_ID);
        assertThat(testTarget.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTarget.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTarget.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTarget.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTarget.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testTarget.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateTargetWithPatch() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        int databaseSizeBeforeUpdate = targetRepository.findAll().size();

        // Update the target using partial update
        Target partialUpdatedTarget = new Target();
        partialUpdatedTarget.setId(target.getId());

        partialUpdatedTarget
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .icon(UPDATED_ICON)
            .color(UPDATED_COLOR)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTarget.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTarget))
            )
            .andExpect(status().isOk());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
        Target testTarget = targetList.get(targetList.size() - 1);
        assertThat(testTarget.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testTarget.getMcCampaingnId()).isEqualTo(UPDATED_MC_CAMPAINGN_ID);
        assertThat(testTarget.getTmlCampaignId()).isEqualTo(UPDATED_TML_CAMPAIGN_ID);
        assertThat(testTarget.getIcon()).isEqualTo(UPDATED_ICON);
        assertThat(testTarget.getColor()).isEqualTo(UPDATED_COLOR);
        assertThat(testTarget.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTarget.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTarget.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTarget.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, target.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(target))
            )
            .andExpect(status().isBadRequest());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(target))
            )
            .andExpect(status().isBadRequest());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTarget() throws Exception {
        int databaseSizeBeforeUpdate = targetRepository.findAll().size();
        target.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTargetMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(target)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Target in the database
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTarget() throws Exception {
        // Initialize the database
        targetRepository.saveAndFlush(target);

        int databaseSizeBeforeDelete = targetRepository.findAll().size();

        // Delete the target
        restTargetMockMvc
            .perform(delete(ENTITY_API_URL_ID, target.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Target> targetList = targetRepository.findAll();
        assertThat(targetList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
