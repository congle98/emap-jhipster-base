package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.WarningMessage;
import com.emap.domain.WarningRule;
import com.emap.repository.WarningMessageRepository;
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
 * Integration tests for the {@link WarningMessageResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarningMessageResourceIT {

    private static final String DEFAULT_MC_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_WARNING_DISTANCE = "AAAAAAAAAA";
    private static final String UPDATED_WARNING_DISTANCE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SHOW_WARNING_CIRCLE = false;
    private static final Boolean UPDATED_SHOW_WARNING_CIRCLE = true;

    private static final Boolean DEFAULT_SHOW_WARNING_MESSAGE = false;
    private static final Boolean UPDATED_SHOW_WARNING_MESSAGE = true;

    private static final String DEFAULT_WARNING_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_WARNING_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEND_WARNING_MESSAGE_TO_MC = false;
    private static final Boolean UPDATED_SEND_WARNING_MESSAGE_TO_MC = true;

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/warning-messages";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WarningMessageRepository warningMessageRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarningMessageMockMvc;

    private WarningMessage warningMessage;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarningMessage createEntity(EntityManager em) {
        WarningMessage warningMessage = new WarningMessage()
            .mcUserId(DEFAULT_MC_USER_ID)
            .warningDistance(DEFAULT_WARNING_DISTANCE)
            .showWarningCircle(DEFAULT_SHOW_WARNING_CIRCLE)
            .showWarningMessage(DEFAULT_SHOW_WARNING_MESSAGE)
            .warningMessage(DEFAULT_WARNING_MESSAGE)
            .sendWarningMessageToMc(DEFAULT_SEND_WARNING_MESSAGE_TO_MC)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        // Add required entity
        WarningRule warningRule;
        if (TestUtil.findAll(em, WarningRule.class).isEmpty()) {
            warningRule = WarningRuleResourceIT.createEntity(em);
            em.persist(warningRule);
            em.flush();
        } else {
            warningRule = TestUtil.findAll(em, WarningRule.class).get(0);
        }
        warningMessage.setWarningRule(warningRule);
        return warningMessage;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarningMessage createUpdatedEntity(EntityManager em) {
        WarningMessage warningMessage = new WarningMessage()
            .mcUserId(UPDATED_MC_USER_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        // Add required entity
        WarningRule warningRule;
        if (TestUtil.findAll(em, WarningRule.class).isEmpty()) {
            warningRule = WarningRuleResourceIT.createUpdatedEntity(em);
            em.persist(warningRule);
            em.flush();
        } else {
            warningRule = TestUtil.findAll(em, WarningRule.class).get(0);
        }
        warningMessage.setWarningRule(warningRule);
        return warningMessage;
    }

    @BeforeEach
    public void initTest() {
        warningMessage = createEntity(em);
    }

    @Test
    @Transactional
    void createWarningMessage() throws Exception {
        int databaseSizeBeforeCreate = warningMessageRepository.findAll().size();
        // Create the WarningMessage
        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isCreated());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeCreate + 1);
        WarningMessage testWarningMessage = warningMessageList.get(warningMessageList.size() - 1);
        assertThat(testWarningMessage.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testWarningMessage.getWarningDistance()).isEqualTo(DEFAULT_WARNING_DISTANCE);
        assertThat(testWarningMessage.getShowWarningCircle()).isEqualTo(DEFAULT_SHOW_WARNING_CIRCLE);
        assertThat(testWarningMessage.getShowWarningMessage()).isEqualTo(DEFAULT_SHOW_WARNING_MESSAGE);
        assertThat(testWarningMessage.getWarningMessage()).isEqualTo(DEFAULT_WARNING_MESSAGE);
        assertThat(testWarningMessage.getSendWarningMessageToMc()).isEqualTo(DEFAULT_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningMessage.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWarningMessage.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testWarningMessage.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testWarningMessage.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createWarningMessageWithExistingId() throws Exception {
        // Create the WarningMessage with an existing ID
        warningMessage.setId(1L);

        int databaseSizeBeforeCreate = warningMessageRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkMcUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setMcUserId(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWarningDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setWarningDistance(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShowWarningCircleIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setShowWarningCircle(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShowWarningMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setShowWarningMessage(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSendWarningMessageToMcIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setSendWarningMessageToMc(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setCreateDate(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setCreateUid(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setLastUpdate(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningMessageRepository.findAll().size();
        // set the field null
        warningMessage.setLastUpdateUid(null);

        // Create the WarningMessage, which fails.

        restWarningMessageMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarningMessages() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        // Get all the warningMessageList
        restWarningMessageMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warningMessage.getId().intValue())))
            .andExpect(jsonPath("$.[*].mcUserId").value(hasItem(DEFAULT_MC_USER_ID)))
            .andExpect(jsonPath("$.[*].warningDistance").value(hasItem(DEFAULT_WARNING_DISTANCE)))
            .andExpect(jsonPath("$.[*].showWarningCircle").value(hasItem(DEFAULT_SHOW_WARNING_CIRCLE.booleanValue())))
            .andExpect(jsonPath("$.[*].showWarningMessage").value(hasItem(DEFAULT_SHOW_WARNING_MESSAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].warningMessage").value(hasItem(DEFAULT_WARNING_MESSAGE)))
            .andExpect(jsonPath("$.[*].sendWarningMessageToMc").value(hasItem(DEFAULT_SEND_WARNING_MESSAGE_TO_MC.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getWarningMessage() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        // Get the warningMessage
        restWarningMessageMockMvc
            .perform(get(ENTITY_API_URL_ID, warningMessage.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warningMessage.getId().intValue()))
            .andExpect(jsonPath("$.mcUserId").value(DEFAULT_MC_USER_ID))
            .andExpect(jsonPath("$.warningDistance").value(DEFAULT_WARNING_DISTANCE))
            .andExpect(jsonPath("$.showWarningCircle").value(DEFAULT_SHOW_WARNING_CIRCLE.booleanValue()))
            .andExpect(jsonPath("$.showWarningMessage").value(DEFAULT_SHOW_WARNING_MESSAGE.booleanValue()))
            .andExpect(jsonPath("$.warningMessage").value(DEFAULT_WARNING_MESSAGE))
            .andExpect(jsonPath("$.sendWarningMessageToMc").value(DEFAULT_SEND_WARNING_MESSAGE_TO_MC.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingWarningMessage() throws Exception {
        // Get the warningMessage
        restWarningMessageMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarningMessage() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();

        // Update the warningMessage
        WarningMessage updatedWarningMessage = warningMessageRepository.findById(warningMessage.getId()).get();
        // Disconnect from session so that the updates on updatedWarningMessage are not directly saved in db
        em.detach(updatedWarningMessage);
        updatedWarningMessage
            .mcUserId(UPDATED_MC_USER_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restWarningMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarningMessage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarningMessage))
            )
            .andExpect(status().isOk());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
        WarningMessage testWarningMessage = warningMessageList.get(warningMessageList.size() - 1);
        assertThat(testWarningMessage.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningMessage.getWarningDistance()).isEqualTo(UPDATED_WARNING_DISTANCE);
        assertThat(testWarningMessage.getShowWarningCircle()).isEqualTo(UPDATED_SHOW_WARNING_CIRCLE);
        assertThat(testWarningMessage.getShowWarningMessage()).isEqualTo(UPDATED_SHOW_WARNING_MESSAGE);
        assertThat(testWarningMessage.getWarningMessage()).isEqualTo(UPDATED_WARNING_MESSAGE);
        assertThat(testWarningMessage.getSendWarningMessageToMc()).isEqualTo(UPDATED_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningMessage.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarningMessage.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testWarningMessage.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningMessage.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warningMessage.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningMessage)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarningMessageWithPatch() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();

        // Update the warningMessage using partial update
        WarningMessage partialUpdatedWarningMessage = new WarningMessage();
        partialUpdatedWarningMessage.setId(warningMessage.getId());

        partialUpdatedWarningMessage
            .mcUserId(UPDATED_MC_USER_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restWarningMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarningMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarningMessage))
            )
            .andExpect(status().isOk());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
        WarningMessage testWarningMessage = warningMessageList.get(warningMessageList.size() - 1);
        assertThat(testWarningMessage.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningMessage.getWarningDistance()).isEqualTo(UPDATED_WARNING_DISTANCE);
        assertThat(testWarningMessage.getShowWarningCircle()).isEqualTo(UPDATED_SHOW_WARNING_CIRCLE);
        assertThat(testWarningMessage.getShowWarningMessage()).isEqualTo(DEFAULT_SHOW_WARNING_MESSAGE);
        assertThat(testWarningMessage.getWarningMessage()).isEqualTo(DEFAULT_WARNING_MESSAGE);
        assertThat(testWarningMessage.getSendWarningMessageToMc()).isEqualTo(DEFAULT_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningMessage.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWarningMessage.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testWarningMessage.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningMessage.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateWarningMessageWithPatch() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();

        // Update the warningMessage using partial update
        WarningMessage partialUpdatedWarningMessage = new WarningMessage();
        partialUpdatedWarningMessage.setId(warningMessage.getId());

        partialUpdatedWarningMessage
            .mcUserId(UPDATED_MC_USER_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restWarningMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarningMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarningMessage))
            )
            .andExpect(status().isOk());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
        WarningMessage testWarningMessage = warningMessageList.get(warningMessageList.size() - 1);
        assertThat(testWarningMessage.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningMessage.getWarningDistance()).isEqualTo(UPDATED_WARNING_DISTANCE);
        assertThat(testWarningMessage.getShowWarningCircle()).isEqualTo(UPDATED_SHOW_WARNING_CIRCLE);
        assertThat(testWarningMessage.getShowWarningMessage()).isEqualTo(UPDATED_SHOW_WARNING_MESSAGE);
        assertThat(testWarningMessage.getWarningMessage()).isEqualTo(UPDATED_WARNING_MESSAGE);
        assertThat(testWarningMessage.getSendWarningMessageToMc()).isEqualTo(UPDATED_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningMessage.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarningMessage.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testWarningMessage.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningMessage.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warningMessage.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarningMessage() throws Exception {
        int databaseSizeBeforeUpdate = warningMessageRepository.findAll().size();
        warningMessage.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningMessageMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(warningMessage))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarningMessage in the database
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarningMessage() throws Exception {
        // Initialize the database
        warningMessageRepository.saveAndFlush(warningMessage);

        int databaseSizeBeforeDelete = warningMessageRepository.findAll().size();

        // Delete the warningMessage
        restWarningMessageMockMvc
            .perform(delete(ENTITY_API_URL_ID, warningMessage.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WarningMessage> warningMessageList = warningMessageRepository.findAll();
        assertThat(warningMessageList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
