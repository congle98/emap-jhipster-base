package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.Target;
import com.emap.domain.TrackingList;
import com.emap.domain.TrackingListDetails;
import com.emap.repository.TrackingListDetailsRepository;
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
 * Integration tests for the {@link TrackingListDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackingListDetailsResourceIT {

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tracking-list-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackingListDetailsRepository trackingListDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackingListDetailsMockMvc;

    private TrackingListDetails trackingListDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackingListDetails createEntity(EntityManager em) {
        TrackingListDetails trackingListDetails = new TrackingListDetails()
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        // Add required entity
        TrackingList trackingList;
        if (TestUtil.findAll(em, TrackingList.class).isEmpty()) {
            trackingList = TrackingListResourceIT.createEntity(em);
            em.persist(trackingList);
            em.flush();
        } else {
            trackingList = TestUtil.findAll(em, TrackingList.class).get(0);
        }
        trackingListDetails.setTrackingList(trackingList);
        // Add required entity
        Target target;
        if (TestUtil.findAll(em, Target.class).isEmpty()) {
            target = TargetResourceIT.createEntity(em);
            em.persist(target);
            em.flush();
        } else {
            target = TestUtil.findAll(em, Target.class).get(0);
        }
        trackingListDetails.setMcTarget(target);
        return trackingListDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackingListDetails createUpdatedEntity(EntityManager em) {
        TrackingListDetails trackingListDetails = new TrackingListDetails()
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        // Add required entity
        TrackingList trackingList;
        if (TestUtil.findAll(em, TrackingList.class).isEmpty()) {
            trackingList = TrackingListResourceIT.createUpdatedEntity(em);
            em.persist(trackingList);
            em.flush();
        } else {
            trackingList = TestUtil.findAll(em, TrackingList.class).get(0);
        }
        trackingListDetails.setTrackingList(trackingList);
        // Add required entity
        Target target;
        if (TestUtil.findAll(em, Target.class).isEmpty()) {
            target = TargetResourceIT.createUpdatedEntity(em);
            em.persist(target);
            em.flush();
        } else {
            target = TestUtil.findAll(em, Target.class).get(0);
        }
        trackingListDetails.setMcTarget(target);
        return trackingListDetails;
    }

    @BeforeEach
    public void initTest() {
        trackingListDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createTrackingListDetails() throws Exception {
        int databaseSizeBeforeCreate = trackingListDetailsRepository.findAll().size();
        // Create the TrackingListDetails
        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isCreated());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        TrackingListDetails testTrackingListDetails = trackingListDetailsList.get(trackingListDetailsList.size() - 1);
        assertThat(testTrackingListDetails.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTrackingListDetails.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTrackingListDetails.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testTrackingListDetails.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createTrackingListDetailsWithExistingId() throws Exception {
        // Create the TrackingListDetails with an existing ID
        trackingListDetails.setId(1L);

        int databaseSizeBeforeCreate = trackingListDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListDetailsRepository.findAll().size();
        // set the field null
        trackingListDetails.setCreateDate(null);

        // Create the TrackingListDetails, which fails.

        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListDetailsRepository.findAll().size();
        // set the field null
        trackingListDetails.setCreateUid(null);

        // Create the TrackingListDetails, which fails.

        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListDetailsRepository.findAll().size();
        // set the field null
        trackingListDetails.setLastUpdate(null);

        // Create the TrackingListDetails, which fails.

        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListDetailsRepository.findAll().size();
        // set the field null
        trackingListDetails.setLastUpdateUid(null);

        // Create the TrackingListDetails, which fails.

        restTrackingListDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrackingListDetails() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        // Get all the trackingListDetailsList
        restTrackingListDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackingListDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getTrackingListDetails() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        // Get the trackingListDetails
        restTrackingListDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, trackingListDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trackingListDetails.getId().intValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingTrackingListDetails() throws Exception {
        // Get the trackingListDetails
        restTrackingListDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrackingListDetails() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();

        // Update the trackingListDetails
        TrackingListDetails updatedTrackingListDetails = trackingListDetailsRepository.findById(trackingListDetails.getId()).get();
        // Disconnect from session so that the updates on updatedTrackingListDetails are not directly saved in db
        em.detach(updatedTrackingListDetails);
        updatedTrackingListDetails
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTrackingListDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrackingListDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrackingListDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackingListDetails testTrackingListDetails = trackingListDetailsList.get(trackingListDetailsList.size() - 1);
        assertThat(testTrackingListDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrackingListDetails.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTrackingListDetails.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTrackingListDetails.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackingListDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrackingListDetailsWithPatch() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();

        // Update the trackingListDetails using partial update
        TrackingListDetails partialUpdatedTrackingListDetails = new TrackingListDetails();
        partialUpdatedTrackingListDetails.setId(trackingListDetails.getId());

        partialUpdatedTrackingListDetails.createDate(UPDATED_CREATE_DATE);

        restTrackingListDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackingListDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackingListDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackingListDetails testTrackingListDetails = trackingListDetailsList.get(trackingListDetailsList.size() - 1);
        assertThat(testTrackingListDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrackingListDetails.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTrackingListDetails.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testTrackingListDetails.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateTrackingListDetailsWithPatch() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();

        // Update the trackingListDetails using partial update
        TrackingListDetails partialUpdatedTrackingListDetails = new TrackingListDetails();
        partialUpdatedTrackingListDetails.setId(trackingListDetails.getId());

        partialUpdatedTrackingListDetails
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTrackingListDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackingListDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackingListDetails))
            )
            .andExpect(status().isOk());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
        TrackingListDetails testTrackingListDetails = trackingListDetailsList.get(trackingListDetailsList.size() - 1);
        assertThat(testTrackingListDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrackingListDetails.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTrackingListDetails.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTrackingListDetails.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackingListDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrackingListDetails() throws Exception {
        int databaseSizeBeforeUpdate = trackingListDetailsRepository.findAll().size();
        trackingListDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackingListDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackingListDetails in the database
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrackingListDetails() throws Exception {
        // Initialize the database
        trackingListDetailsRepository.saveAndFlush(trackingListDetails);

        int databaseSizeBeforeDelete = trackingListDetailsRepository.findAll().size();

        // Delete the trackingListDetails
        restTrackingListDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, trackingListDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrackingListDetails> trackingListDetailsList = trackingListDetailsRepository.findAll();
        assertThat(trackingListDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
