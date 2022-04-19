package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.TrackingList;
import com.emap.repository.TrackingListRepository;
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
 * Integration tests for the {@link TrackingListResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class TrackingListResourceIT {

    private static final String DEFAULT_MC_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_TYPE = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/tracking-lists";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private TrackingListRepository trackingListRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restTrackingListMockMvc;

    private TrackingList trackingList;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackingList createEntity(EntityManager em) {
        TrackingList trackingList = new TrackingList()
            .mcUserId(DEFAULT_MC_USER_ID)
            .type(DEFAULT_TYPE)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return trackingList;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static TrackingList createUpdatedEntity(EntityManager em) {
        TrackingList trackingList = new TrackingList()
            .mcUserId(UPDATED_MC_USER_ID)
            .type(UPDATED_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return trackingList;
    }

    @BeforeEach
    public void initTest() {
        trackingList = createEntity(em);
    }

    @Test
    @Transactional
    void createTrackingList() throws Exception {
        int databaseSizeBeforeCreate = trackingListRepository.findAll().size();
        // Create the TrackingList
        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isCreated());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeCreate + 1);
        TrackingList testTrackingList = trackingListList.get(trackingListList.size() - 1);
        assertThat(testTrackingList.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testTrackingList.getType()).isEqualTo(DEFAULT_TYPE);
        assertThat(testTrackingList.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTrackingList.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTrackingList.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testTrackingList.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createTrackingListWithExistingId() throws Exception {
        // Create the TrackingList with an existing ID
        trackingList.setId(1L);

        int databaseSizeBeforeCreate = trackingListRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMcUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setMcUserId(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setType(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setCreateDate(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setCreateUid(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setLastUpdate(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = trackingListRepository.findAll().size();
        // set the field null
        trackingList.setLastUpdateUid(null);

        // Create the TrackingList, which fails.

        restTrackingListMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isBadRequest());

        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllTrackingLists() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        // Get all the trackingListList
        restTrackingListMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(trackingList.getId().intValue())))
            .andExpect(jsonPath("$.[*].mcUserId").value(hasItem(DEFAULT_MC_USER_ID)))
            .andExpect(jsonPath("$.[*].type").value(hasItem(DEFAULT_TYPE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getTrackingList() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        // Get the trackingList
        restTrackingListMockMvc
            .perform(get(ENTITY_API_URL_ID, trackingList.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(trackingList.getId().intValue()))
            .andExpect(jsonPath("$.mcUserId").value(DEFAULT_MC_USER_ID))
            .andExpect(jsonPath("$.type").value(DEFAULT_TYPE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingTrackingList() throws Exception {
        // Get the trackingList
        restTrackingListMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewTrackingList() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();

        // Update the trackingList
        TrackingList updatedTrackingList = trackingListRepository.findById(trackingList.getId()).get();
        // Disconnect from session so that the updates on updatedTrackingList are not directly saved in db
        em.detach(updatedTrackingList);
        updatedTrackingList
            .mcUserId(UPDATED_MC_USER_ID)
            .type(UPDATED_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTrackingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedTrackingList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedTrackingList))
            )
            .andExpect(status().isOk());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
        TrackingList testTrackingList = trackingListList.get(trackingListList.size() - 1);
        assertThat(testTrackingList.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testTrackingList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTrackingList.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrackingList.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTrackingList.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTrackingList.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, trackingList.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(trackingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(trackingList)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateTrackingListWithPatch() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();

        // Update the trackingList using partial update
        TrackingList partialUpdatedTrackingList = new TrackingList();
        partialUpdatedTrackingList.setId(trackingList.getId());

        partialUpdatedTrackingList.type(UPDATED_TYPE).lastUpdate(UPDATED_LAST_UPDATE);

        restTrackingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackingList))
            )
            .andExpect(status().isOk());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
        TrackingList testTrackingList = trackingListList.get(trackingListList.size() - 1);
        assertThat(testTrackingList.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testTrackingList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTrackingList.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testTrackingList.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testTrackingList.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTrackingList.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateTrackingListWithPatch() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();

        // Update the trackingList using partial update
        TrackingList partialUpdatedTrackingList = new TrackingList();
        partialUpdatedTrackingList.setId(trackingList.getId());

        partialUpdatedTrackingList
            .mcUserId(UPDATED_MC_USER_ID)
            .type(UPDATED_TYPE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restTrackingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedTrackingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedTrackingList))
            )
            .andExpect(status().isOk());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
        TrackingList testTrackingList = trackingListList.get(trackingListList.size() - 1);
        assertThat(testTrackingList.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testTrackingList.getType()).isEqualTo(UPDATED_TYPE);
        assertThat(testTrackingList.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testTrackingList.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testTrackingList.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testTrackingList.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, trackingList.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(trackingList))
            )
            .andExpect(status().isBadRequest());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamTrackingList() throws Exception {
        int databaseSizeBeforeUpdate = trackingListRepository.findAll().size();
        trackingList.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restTrackingListMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(trackingList))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the TrackingList in the database
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteTrackingList() throws Exception {
        // Initialize the database
        trackingListRepository.saveAndFlush(trackingList);

        int databaseSizeBeforeDelete = trackingListRepository.findAll().size();

        // Delete the trackingList
        restTrackingListMockMvc
            .perform(delete(ENTITY_API_URL_ID, trackingList.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<TrackingList> trackingListList = trackingListRepository.findAll();
        assertThat(trackingListList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
