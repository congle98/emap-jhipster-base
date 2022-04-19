package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.Coordinates;
import com.emap.domain.CoordinatesDetails;
import com.emap.domain.Target;
import com.emap.repository.CoordinatesDetailsRepository;
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
 * Integration tests for the {@link CoordinatesDetailsResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class CoordinatesDetailsResourceIT {

    private static final Integer DEFAULT_SIGNAL_CONNECTION_STRENGTH = 1;
    private static final Integer UPDATED_SIGNAL_CONNECTION_STRENGTH = 2;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/coordinates-details";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private CoordinatesDetailsRepository coordinatesDetailsRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restCoordinatesDetailsMockMvc;

    private CoordinatesDetails coordinatesDetails;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoordinatesDetails createEntity(EntityManager em) {
        CoordinatesDetails coordinatesDetails = new CoordinatesDetails()
            .signalConnectionStrength(DEFAULT_SIGNAL_CONNECTION_STRENGTH)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        // Add required entity
        Coordinates coordinates;
        if (TestUtil.findAll(em, Coordinates.class).isEmpty()) {
            coordinates = CoordinatesResourceIT.createEntity(em);
            em.persist(coordinates);
            em.flush();
        } else {
            coordinates = TestUtil.findAll(em, Coordinates.class).get(0);
        }
        coordinatesDetails.setCoordinate(coordinates);
        // Add required entity
        Target target;
        if (TestUtil.findAll(em, Target.class).isEmpty()) {
            target = TargetResourceIT.createEntity(em);
            em.persist(target);
            em.flush();
        } else {
            target = TestUtil.findAll(em, Target.class).get(0);
        }
        coordinatesDetails.setObject(target);
        return coordinatesDetails;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static CoordinatesDetails createUpdatedEntity(EntityManager em) {
        CoordinatesDetails coordinatesDetails = new CoordinatesDetails()
            .signalConnectionStrength(UPDATED_SIGNAL_CONNECTION_STRENGTH)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        // Add required entity
        Coordinates coordinates;
        if (TestUtil.findAll(em, Coordinates.class).isEmpty()) {
            coordinates = CoordinatesResourceIT.createUpdatedEntity(em);
            em.persist(coordinates);
            em.flush();
        } else {
            coordinates = TestUtil.findAll(em, Coordinates.class).get(0);
        }
        coordinatesDetails.setCoordinate(coordinates);
        // Add required entity
        Target target;
        if (TestUtil.findAll(em, Target.class).isEmpty()) {
            target = TargetResourceIT.createUpdatedEntity(em);
            em.persist(target);
            em.flush();
        } else {
            target = TestUtil.findAll(em, Target.class).get(0);
        }
        coordinatesDetails.setObject(target);
        return coordinatesDetails;
    }

    @BeforeEach
    public void initTest() {
        coordinatesDetails = createEntity(em);
    }

    @Test
    @Transactional
    void createCoordinatesDetails() throws Exception {
        int databaseSizeBeforeCreate = coordinatesDetailsRepository.findAll().size();
        // Create the CoordinatesDetails
        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isCreated());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeCreate + 1);
        CoordinatesDetails testCoordinatesDetails = coordinatesDetailsList.get(coordinatesDetailsList.size() - 1);
        assertThat(testCoordinatesDetails.getSignalConnectionStrength()).isEqualTo(DEFAULT_SIGNAL_CONNECTION_STRENGTH);
        assertThat(testCoordinatesDetails.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testCoordinatesDetails.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testCoordinatesDetails.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testCoordinatesDetails.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createCoordinatesDetailsWithExistingId() throws Exception {
        // Create the CoordinatesDetails with an existing ID
        coordinatesDetails.setId(1L);

        int databaseSizeBeforeCreate = coordinatesDetailsRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesDetailsRepository.findAll().size();
        // set the field null
        coordinatesDetails.setCreateDate(null);

        // Create the CoordinatesDetails, which fails.

        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesDetailsRepository.findAll().size();
        // set the field null
        coordinatesDetails.setCreateUid(null);

        // Create the CoordinatesDetails, which fails.

        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesDetailsRepository.findAll().size();
        // set the field null
        coordinatesDetails.setLastUpdate(null);

        // Create the CoordinatesDetails, which fails.

        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = coordinatesDetailsRepository.findAll().size();
        // set the field null
        coordinatesDetails.setLastUpdateUid(null);

        // Create the CoordinatesDetails, which fails.

        restCoordinatesDetailsMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllCoordinatesDetails() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        // Get all the coordinatesDetailsList
        restCoordinatesDetailsMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(coordinatesDetails.getId().intValue())))
            .andExpect(jsonPath("$.[*].signalConnectionStrength").value(hasItem(DEFAULT_SIGNAL_CONNECTION_STRENGTH)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getCoordinatesDetails() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        // Get the coordinatesDetails
        restCoordinatesDetailsMockMvc
            .perform(get(ENTITY_API_URL_ID, coordinatesDetails.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(coordinatesDetails.getId().intValue()))
            .andExpect(jsonPath("$.signalConnectionStrength").value(DEFAULT_SIGNAL_CONNECTION_STRENGTH))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingCoordinatesDetails() throws Exception {
        // Get the coordinatesDetails
        restCoordinatesDetailsMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewCoordinatesDetails() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();

        // Update the coordinatesDetails
        CoordinatesDetails updatedCoordinatesDetails = coordinatesDetailsRepository.findById(coordinatesDetails.getId()).get();
        // Disconnect from session so that the updates on updatedCoordinatesDetails are not directly saved in db
        em.detach(updatedCoordinatesDetails);
        updatedCoordinatesDetails
            .signalConnectionStrength(UPDATED_SIGNAL_CONNECTION_STRENGTH)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restCoordinatesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedCoordinatesDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedCoordinatesDetails))
            )
            .andExpect(status().isOk());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
        CoordinatesDetails testCoordinatesDetails = coordinatesDetailsList.get(coordinatesDetailsList.size() - 1);
        assertThat(testCoordinatesDetails.getSignalConnectionStrength()).isEqualTo(UPDATED_SIGNAL_CONNECTION_STRENGTH);
        assertThat(testCoordinatesDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCoordinatesDetails.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinatesDetails.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCoordinatesDetails.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, coordinatesDetails.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateCoordinatesDetailsWithPatch() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();

        // Update the coordinatesDetails using partial update
        CoordinatesDetails partialUpdatedCoordinatesDetails = new CoordinatesDetails();
        partialUpdatedCoordinatesDetails.setId(coordinatesDetails.getId());

        partialUpdatedCoordinatesDetails.createDate(UPDATED_CREATE_DATE).createUid(UPDATED_CREATE_UID).lastUpdate(UPDATED_LAST_UPDATE);

        restCoordinatesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinatesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinatesDetails))
            )
            .andExpect(status().isOk());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
        CoordinatesDetails testCoordinatesDetails = coordinatesDetailsList.get(coordinatesDetailsList.size() - 1);
        assertThat(testCoordinatesDetails.getSignalConnectionStrength()).isEqualTo(DEFAULT_SIGNAL_CONNECTION_STRENGTH);
        assertThat(testCoordinatesDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCoordinatesDetails.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinatesDetails.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCoordinatesDetails.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateCoordinatesDetailsWithPatch() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();

        // Update the coordinatesDetails using partial update
        CoordinatesDetails partialUpdatedCoordinatesDetails = new CoordinatesDetails();
        partialUpdatedCoordinatesDetails.setId(coordinatesDetails.getId());

        partialUpdatedCoordinatesDetails
            .signalConnectionStrength(UPDATED_SIGNAL_CONNECTION_STRENGTH)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restCoordinatesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedCoordinatesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedCoordinatesDetails))
            )
            .andExpect(status().isOk());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
        CoordinatesDetails testCoordinatesDetails = coordinatesDetailsList.get(coordinatesDetailsList.size() - 1);
        assertThat(testCoordinatesDetails.getSignalConnectionStrength()).isEqualTo(UPDATED_SIGNAL_CONNECTION_STRENGTH);
        assertThat(testCoordinatesDetails.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testCoordinatesDetails.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testCoordinatesDetails.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testCoordinatesDetails.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, coordinatesDetails.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isBadRequest());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamCoordinatesDetails() throws Exception {
        int databaseSizeBeforeUpdate = coordinatesDetailsRepository.findAll().size();
        coordinatesDetails.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restCoordinatesDetailsMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(coordinatesDetails))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the CoordinatesDetails in the database
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteCoordinatesDetails() throws Exception {
        // Initialize the database
        coordinatesDetailsRepository.saveAndFlush(coordinatesDetails);

        int databaseSizeBeforeDelete = coordinatesDetailsRepository.findAll().size();

        // Delete the coordinatesDetails
        restCoordinatesDetailsMockMvc
            .perform(delete(ENTITY_API_URL_ID, coordinatesDetails.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<CoordinatesDetails> coordinatesDetailsList = coordinatesDetailsRepository.findAll();
        assertThat(coordinatesDetailsList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
