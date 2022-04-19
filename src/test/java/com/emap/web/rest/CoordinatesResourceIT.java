package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.Coordinates;
import com.emap.repository.CoordinatesRepository;
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
 * Integration tests for the {@link CoordinatesResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoordinatesResourceIT {

    private static final String DEFAULT_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MC_CAMPAINGN_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_CAMPAINGN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TML_CAMPAIGN_ID = "AAAAAAAAAA";
    private static final String UPDATED_TML_CAMPAIGN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_LAT = "AAAAAAAAAA";
    private static final String UPDATED_LAT = "BBBBBBBBBB";

    private static final String DEFAULT_LNG = "AAAAAAAAAA";
    private static final String UPDATED_LNG = "BBBBBBBBBB";

    private static final Double DEFAULT_RADIUS = 1D;
    private static final Double UPDATED_RADIUS = 2D;

    private static final Integer DEFAULT_OPEN_ANGLE = 1;
    private static final Integer UPDATED_OPEN_ANGLE = 2;

    private static final Integer DEFAULT_DIRECTIONAL_ANGLE = 1;
    private static final Integer UPDATED_DIRECTIONAL_ANGLE = 2;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coordinates";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoordinatesRepository coordinatesRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoordinatesMockMvc;

    private Coordinates coordinates;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinates createEntity(EntityManager em) {
        Coordinates coordinates = new Coordinates()
            .sourceType(DEFAULT_SOURCE_TYPE)
            .mcCampaingnId(DEFAULT_MC_CAMPAINGN_ID)
            .tmlCampaignId(DEFAULT_TML_CAMPAIGN_ID)
            .lat(DEFAULT_LAT)
            .lng(DEFAULT_LNG)
            .radius(DEFAULT_RADIUS)
            .openAngle(DEFAULT_OPEN_ANGLE)
            .directionalAngle(DEFAULT_DIRECTIONAL_ANGLE)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return coordinates;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Coordinates createUpdatedEntity(EntityManager em) {
        Coordinates coordinates = new Coordinates()
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .radius(UPDATED_RADIUS)
            .openAngle(UPDATED_OPEN_ANGLE)
            .directionalAngle(UPDATED_DIRECTIONAL_ANGLE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return coordinates;
    }

    @BeforeEach
    public void initTest() {
        coordinates = createEntity(em);
    }

    @Test
    @Transactional
    void createCoordinates() throws Exception {
        int databaseSizeBeforeCreate = coordinatesRepository.findAll().size();
        // Create the Coordinates
        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isCreated());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeCreate + 1);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testCoordinates.getMcCampaingnId()).isEqualTo(DEFAULT_MC_CAMPAINGN_ID);
        assertThat(testCoordinates.getTmlCampaignId()).isEqualTo(DEFAULT_TML_CAMPAIGN_ID);
        assertThat(testCoordinates.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testCoordinates.getLng()).isEqualTo(DEFAULT_LNG);
        assertThat(testCoordinates.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testCoordinates.getOpenAngle()).isEqualTo(DEFAULT_OPEN_ANGLE);
        assertThat(testCoordinates.getDirectionalAngle()).isEqualTo(DEFAULT_DIRECTIONAL_ANGLE);
        assertThat(testCoordinates.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCoordinates.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testCoordinates.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCoordinates.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createCoordinatesWithExistingId() throws Exception {
        // Create the Coordinates with an existing ID
        coordinates.setId(1L);

        int databaseSizeBeforeCreate = coordinatesRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setSourceType(null);

        // Create the Coordinates, which fails.

        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setCreateDate(null);

        // Create the Coordinates, which fails.

        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setCreateUid(null);

        // Create the Coordinates, which fails.

        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setLastUpdate(null);

        // Create the Coordinates, which fails.

        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesRepository.findAll().size();
        // set the field null
        coordinates.setLastUpdateUid(null);

        // Create the Coordinates, which fails.

        restCoordinatesMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isBadRequest());

        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        // Get all the coordinatesList
        restCoordinatesMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinates.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].mcCampaingnId").value(hasItem(DEFAULT_MC_CAMPAINGN_ID)))
            .andExpect(jsonPath("$.[*].tmlCampaignId").value(hasItem(DEFAULT_TML_CAMPAIGN_ID)))
            .andExpect(jsonPath("$.[*].lat").value(hasItem(DEFAULT_LAT)))
            .andExpect(jsonPath("$.[*].lng").value(hasItem(DEFAULT_LNG)))
            .andExpect(jsonPath("$.[*].radius").value(hasItem(DEFAULT_RADIUS.doubleValue())))
            .andExpect(jsonPath("$.[*].openAngle").value(hasItem(DEFAULT_OPEN_ANGLE)))
            .andExpect(jsonPath("$.[*].directionalAngle").value(hasItem(DEFAULT_DIRECTIONAL_ANGLE)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        // Get the coordinates
        restCoordinatesMockMvc
            .perform(get(ENTITY_API_URL_ID, coordinates.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coordinates.getId().intValue()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE))
            .andExpect(jsonPath("$.mcCampaingnId").value(DEFAULT_MC_CAMPAINGN_ID))
            .andExpect(jsonPath("$.tmlCampaignId").value(DEFAULT_TML_CAMPAIGN_ID))
            .andExpect(jsonPath("$.lat").value(DEFAULT_LAT))
            .andExpect(jsonPath("$.lng").value(DEFAULT_LNG))
            .andExpect(jsonPath("$.radius").value(DEFAULT_RADIUS.doubleValue()))
            .andExpect(jsonPath("$.openAngle").value(DEFAULT_OPEN_ANGLE))
            .andExpect(jsonPath("$.directionalAngle").value(DEFAULT_DIRECTIONAL_ANGLE))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingCoordinates() throws Exception {
        // Get the coordinates
        restCoordinatesMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates
        Coordinates updatedCoordinates = coordinatesRepository.findById(coordinates.getId()).get();
        // Disconnect from session so that the updates on updatedCoordinates are not directly saved in db
        em.detach(updatedCoordinates);
        updatedCoordinates
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .radius(UPDATED_RADIUS)
            .openAngle(UPDATED_OPEN_ANGLE)
            .directionalAngle(UPDATED_DIRECTIONAL_ANGLE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoordinates.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoordinates))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testCoordinates.getMcCampaingnId()).isEqualTo(UPDATED_MC_CAMPAINGN_ID);
        assertThat(testCoordinates.getTmlCampaignId()).isEqualTo(UPDATED_TML_CAMPAIGN_ID);
        assertThat(testCoordinates.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCoordinates.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testCoordinates.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testCoordinates.getOpenAngle()).isEqualTo(UPDATED_OPEN_ANGLE);
        assertThat(testCoordinates.getDirectionalAngle()).isEqualTo(UPDATED_DIRECTIONAL_ANGLE);
        assertThat(testCoordinates.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCoordinates.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinates.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCoordinates.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinates.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinates)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoordinatesWithPatch() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates using partial update
        Coordinates partialUpdatedCoordinates = new Coordinates();
        partialUpdatedCoordinates.setId(coordinates.getId());

        partialUpdatedCoordinates.lng(UPDATED_LNG).createUid(UPDATED_CREATE_UID);

        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinates))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testCoordinates.getMcCampaingnId()).isEqualTo(DEFAULT_MC_CAMPAINGN_ID);
        assertThat(testCoordinates.getTmlCampaignId()).isEqualTo(DEFAULT_TML_CAMPAIGN_ID);
        assertThat(testCoordinates.getLat()).isEqualTo(DEFAULT_LAT);
        assertThat(testCoordinates.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testCoordinates.getRadius()).isEqualTo(DEFAULT_RADIUS);
        assertThat(testCoordinates.getOpenAngle()).isEqualTo(DEFAULT_OPEN_ANGLE);
        assertThat(testCoordinates.getDirectionalAngle()).isEqualTo(DEFAULT_DIRECTIONAL_ANGLE);
        assertThat(testCoordinates.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCoordinates.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinates.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCoordinates.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateCoordinatesWithPatch() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();

        // Update the coordinates using partial update
        Coordinates partialUpdatedCoordinates = new Coordinates();
        partialUpdatedCoordinates.setId(coordinates.getId());

        partialUpdatedCoordinates
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcCampaingnId(UPDATED_MC_CAMPAINGN_ID)
            .tmlCampaignId(UPDATED_TML_CAMPAIGN_ID)
            .lat(UPDATED_LAT)
            .lng(UPDATED_LNG)
            .radius(UPDATED_RADIUS)
            .openAngle(UPDATED_OPEN_ANGLE)
            .directionalAngle(UPDATED_DIRECTIONAL_ANGLE)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinates))
            )
            .andExpect(status().isOk());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
        Coordinates testCoordinates = coordinatesList.get(coordinatesList.size() - 1);
        assertThat(testCoordinates.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testCoordinates.getMcCampaingnId()).isEqualTo(UPDATED_MC_CAMPAINGN_ID);
        assertThat(testCoordinates.getTmlCampaignId()).isEqualTo(UPDATED_TML_CAMPAIGN_ID);
        assertThat(testCoordinates.getLat()).isEqualTo(UPDATED_LAT);
        assertThat(testCoordinates.getLng()).isEqualTo(UPDATED_LNG);
        assertThat(testCoordinates.getRadius()).isEqualTo(UPDATED_RADIUS);
        assertThat(testCoordinates.getOpenAngle()).isEqualTo(UPDATED_OPEN_ANGLE);
        assertThat(testCoordinates.getDirectionalAngle()).isEqualTo(UPDATED_DIRECTIONAL_ANGLE);
        assertThat(testCoordinates.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCoordinates.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinates.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCoordinates.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coordinates.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinates))
            )
            .andExpect(status().isBadRequest());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoordinates() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesRepository.findAll().size();
        coordinates.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(coordinates))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the Coordinates in the database
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoordinates() throws Exception {
        // Initialize the database
        coordinatesRepository.saveAndFlush(coordinates);

        int databaseSizeBeforeDelete = coordinatesRepository.findAll().size();

        // Delete the coordinates
        restCoordinatesMockMvc
            .perform(delete(ENTITY_API_URL_ID, coordinates.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Coordinates> coordinatesList = coordinatesRepository.findAll();
        assertThat(coordinatesList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
