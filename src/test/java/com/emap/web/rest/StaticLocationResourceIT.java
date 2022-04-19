package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.StaticLocation;
import com.emap.repository.StaticLocationRepository;
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
 * Integration tests for the {@link StaticLocationResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class StaticLocationResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MC_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_ADDRESS = "AAAAAAAAAA";
    private static final String UPDATED_ADDRESS = "BBBBBBBBBB";

    private static final String DEFAULT_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LNG = "AAAAAAAAAA";
    private static final String UPDATED_LNG = "BBBBBBBBBB";

    private static final Boolean DEFAULT_STATUS = false;
    private static final Boolean UPDATED_STATUS = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/static-locations";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private StaticLocationRepository staticLocationRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restStaticLocationMockMvc;

    private StaticLocation staticLocation;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticLocation createEntity(EntityManager em) {
        StaticLocation staticLocation = new StaticLocation()
            .name(DEFAULT_NAME)
            .mcUserId(DEFAULT_MC_USER_ID)
            .address(DEFAULT_ADDRESS)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return staticLocation;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static StaticLocation createUpdatedEntity(EntityManager em) {
        StaticLocation staticLocation = new StaticLocation()
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .address(UPDATED_ADDRESS)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return staticLocation;
    }

    @BeforeEach
    public void initTest() {
        staticLocation = createEntity(em);
    }

    @Test
    @Transactional
    void createStaticLocation() throws Exception {
        int databaseSizeBeforeCreate = staticLocationRepository.findAll().size();
        // Create the StaticLocation
        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isCreated());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeCreate + 1);
        StaticLocation testStaticLocation = staticLocationList.get(staticLocationList.size() - 1);
        assertThat(testStaticLocation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaticLocation.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testStaticLocation.getAddress()).isEqualTo(DEFAULT_ADDRESS);
        assertThat(testStaticLocation.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testStaticLocation.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testStaticLocation.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testStaticLocation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStaticLocation.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testStaticLocation.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testStaticLocation.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createStaticLocationWithExistingId() throws Exception {
        // Create the StaticLocation with an existing ID
        staticLocation.setId(1L);

        int databaseSizeBeforeCreate = staticLocationRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setName(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMcUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setMcUserId(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkAddressIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setAddress(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLatIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setLat(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLngIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setLng(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setStatus(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setCreateDate(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setCreateUid(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setLastUpdate(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = staticLocationRepository.findAll().size();
        // set the field null
        staticLocation.setLastUpdateUid(null);

        // Create the StaticLocation, which fails.

        restStaticLocationMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllStaticLocations() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        // Get all the staticLocationList
        restStaticLocationMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(staticLocation.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcUserId").value(hasItem(DEFAULT_MC_USER_ID)))
            .andExpect(jsonPath("$.[*].address").value(hasItem(DEFAULT_ADDRESS)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG)))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getStaticLocation() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        // Get the staticLocation
        restStaticLocationMockMvc
            .perform(get(ENTITY_API_URL_ID, staticLocation.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(staticLocation.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mcUserId").value(DEFAULT_MC_USER_ID))
            .andExpect(jsonPath("$.address").value(DEFAULT_ADDRESS))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingStaticLocation() throws Exception {
        // Get the staticLocation
        restStaticLocationMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewStaticLocation() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();

        // Update the staticLocation
        StaticLocation updatedStaticLocation = staticLocationRepository.findById(staticLocation.getId()).get();
        // Disconnect from session so that the updates on updatedStaticLocation are not directly saved in db
        em.detach(updatedStaticLocation);
        updatedStaticLocation
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .address(UPDATED_ADDRESS)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restStaticLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedStaticLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedStaticLocation))
            )
            .andExpect(status().isOk());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
        StaticLocation testStaticLocation = staticLocationList.get(staticLocationList.size() - 1);
        assertThat(testStaticLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaticLocation.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testStaticLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaticLocation.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testStaticLocation.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testStaticLocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStaticLocation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStaticLocation.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testStaticLocation.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testStaticLocation.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, staticLocation.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(staticLocation)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateStaticLocationWithPatch() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();

        // Update the staticLocation using partial update
        StaticLocation partialUpdatedStaticLocation = new StaticLocation();
        partialUpdatedStaticLocation.setId(staticLocation.getId());

        partialUpdatedStaticLocation
            .mcUserId(UPDATED_MC_USER_ID)
            .address(UPDATED_ADDRESS)
            .lat(UPDATED_LAT)
            .status(UPDATED_STATUS)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restStaticLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaticLocation))
            )
            .andExpect(status().isOk());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
        StaticLocation testStaticLocation = staticLocationList.get(staticLocationList.size() - 1);
        assertThat(testStaticLocation.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testStaticLocation.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testStaticLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaticLocation.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testStaticLocation.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testStaticLocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStaticLocation.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testStaticLocation.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testStaticLocation.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testStaticLocation.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateStaticLocationWithPatch() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();

        // Update the staticLocation using partial update
        StaticLocation partialUpdatedStaticLocation = new StaticLocation();
        partialUpdatedStaticLocation.setId(staticLocation.getId());

        partialUpdatedStaticLocation
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .address(UPDATED_ADDRESS)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restStaticLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedStaticLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedStaticLocation))
            )
            .andExpect(status().isOk());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
        StaticLocation testStaticLocation = staticLocationList.get(staticLocationList.size() - 1);
        assertThat(testStaticLocation.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testStaticLocation.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testStaticLocation.getAddress()).isEqualTo(UPDATED_ADDRESS);
        assertThat(testStaticLocation.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testStaticLocation.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testStaticLocation.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testStaticLocation.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testStaticLocation.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testStaticLocation.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testStaticLocation.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, staticLocation.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isBadRequest());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamStaticLocation() throws Exception {
        int databaseSizeBeforeUpdate = staticLocationRepository.findAll().size();
        staticLocation.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restStaticLocationMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(staticLocation))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the StaticLocation in the database
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteStaticLocation() throws Exception {
        // Initialize the database
        staticLocationRepository.saveAndFlush(staticLocation);

        int databaseSizeBeforeDelete = staticLocationRepository.findAll().size();

        // Delete the staticLocation
        restStaticLocationMockMvc
            .perform(delete(ENTITY_API_URL_ID, staticLocation.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<StaticLocation> staticLocationList = staticLocationRepository.findAll();
        assertThat(staticLocationList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
