package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.WarningRule;
import com.emap.repository.WarningRuleRepository;
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
 * Integration tests for the {@link WarningRuleResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class WarningRuleResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_MC_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_USER_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_DELAY_CHECK = 1;
    private static final Integer UPDATED_DELAY_CHECK = 2;

    private static final String DEFAULT_DELAY_CHECK_UNIT = "AAAAAAAAAA";
    private static final String UPDATED_DELAY_CHECK_UNIT = "BBBBBBBBBB";

    private static final String DEFAULT_CONDITION_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_CONDITION_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDE_MC_CAMPAIGN_ID = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDE_MC_CAMPAIGN_ID = "BBBBBBBBBB";

    private static final String DEFAULT_INCLUDE_MC_TARGET_ID = "AAAAAAAAAA";
    private static final String UPDATED_INCLUDE_MC_TARGET_ID = "BBBBBBBBBB";

    private static final Integer DEFAULT_WARNING_DISTANCE = 1;
    private static final Integer UPDATED_WARNING_DISTANCE = 2;

    private static final Boolean DEFAULT_SHOW_WARNING_CIRCLE = false;
    private static final Boolean UPDATED_SHOW_WARNING_CIRCLE = true;

    private static final Boolean DEFAULT_SHOW_WARNING_MESSAGE = false;
    private static final Boolean UPDATED_SHOW_WARNING_MESSAGE = true;

    private static final String DEFAULT_WARNING_MESSAGE = "AAAAAAAAAA";
    private static final String UPDATED_WARNING_MESSAGE = "BBBBBBBBBB";

    private static final Boolean DEFAULT_SEND_WARNING_MESSAGE_TO_MC = false;
    private static final Boolean UPDATED_SEND_WARNING_MESSAGE_TO_MC = true;

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

    private static final String ENTITY_API_URL = "/api/warning-rules";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private WarningRuleRepository warningRuleRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restWarningRuleMockMvc;

    private WarningRule warningRule;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarningRule createEntity(EntityManager em) {
        WarningRule warningRule = new WarningRule()
            .name(DEFAULT_NAME)
            .mcUserId(DEFAULT_MC_USER_ID)
            .delayCheck(DEFAULT_DELAY_CHECK)
            .delayCheckUnit(DEFAULT_DELAY_CHECK_UNIT)
            .conditionType(DEFAULT_CONDITION_TYPE)
            .includeMcCampaignId(DEFAULT_INCLUDE_MC_CAMPAIGN_ID)
            .includeMcTargetId(DEFAULT_INCLUDE_MC_TARGET_ID)
            .warningDistance(DEFAULT_WARNING_DISTANCE)
            .showWarningCircle(DEFAULT_SHOW_WARNING_CIRCLE)
            .showWarningMessage(DEFAULT_SHOW_WARNING_MESSAGE)
            .warningMessage(DEFAULT_WARNING_MESSAGE)
            .sendWarningMessageToMc(DEFAULT_SEND_WARNING_MESSAGE_TO_MC)
            .status(DEFAULT_STATUS)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return warningRule;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static WarningRule createUpdatedEntity(EntityManager em) {
        WarningRule warningRule = new WarningRule()
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .delayCheck(UPDATED_DELAY_CHECK)
            .delayCheckUnit(UPDATED_DELAY_CHECK_UNIT)
            .conditionType(UPDATED_CONDITION_TYPE)
            .includeMcCampaignId(UPDATED_INCLUDE_MC_CAMPAIGN_ID)
            .includeMcTargetId(UPDATED_INCLUDE_MC_TARGET_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return warningRule;
    }

    @BeforeEach
    public void initTest() {
        warningRule = createEntity(em);
    }

    @Test
    @Transactional
    void createWarningRule() throws Exception {
        int databaseSizeBeforeCreate = warningRuleRepository.findAll().size();
        // Create the WarningRule
        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isCreated());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeCreate + 1);
        WarningRule testWarningRule = warningRuleList.get(warningRuleList.size() - 1);
        assertThat(testWarningRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarningRule.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testWarningRule.getDelayCheck()).isEqualTo(DEFAULT_DELAY_CHECK);
        assertThat(testWarningRule.getDelayCheckUnit()).isEqualTo(DEFAULT_DELAY_CHECK_UNIT);
        assertThat(testWarningRule.getConditionType()).isEqualTo(DEFAULT_CONDITION_TYPE);
        assertThat(testWarningRule.getIncludeMcCampaignId()).isEqualTo(DEFAULT_INCLUDE_MC_CAMPAIGN_ID);
        assertThat(testWarningRule.getIncludeMcTargetId()).isEqualTo(DEFAULT_INCLUDE_MC_TARGET_ID);
        assertThat(testWarningRule.getWarningDistance()).isEqualTo(DEFAULT_WARNING_DISTANCE);
        assertThat(testWarningRule.getShowWarningCircle()).isEqualTo(DEFAULT_SHOW_WARNING_CIRCLE);
        assertThat(testWarningRule.getShowWarningMessage()).isEqualTo(DEFAULT_SHOW_WARNING_MESSAGE);
        assertThat(testWarningRule.getWarningMessage()).isEqualTo(DEFAULT_WARNING_MESSAGE);
        assertThat(testWarningRule.getSendWarningMessageToMc()).isEqualTo(DEFAULT_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningRule.getStatus()).isEqualTo(DEFAULT_STATUS);
        assertThat(testWarningRule.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testWarningRule.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testWarningRule.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testWarningRule.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createWarningRuleWithExistingId() throws Exception {
        // Create the WarningRule with an existing ID
        warningRule.setId(1L);

        int databaseSizeBeforeCreate = warningRuleRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkNameIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setName(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkMcUserIdIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setMcUserId(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDelayCheckIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setDelayCheck(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkDelayCheckUnitIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setDelayCheckUnit(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkConditionTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setConditionType(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkWarningDistanceIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setWarningDistance(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShowWarningCircleIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setShowWarningCircle(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkShowWarningMessageIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setShowWarningMessage(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSendWarningMessageToMcIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setSendWarningMessageToMc(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkStatusIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setStatus(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setCreateDate(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setCreateUid(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setLastUpdate(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = warningRuleRepository.findAll().size();
        // set the field null
        warningRule.setLastUpdateUid(null);

        // Create the WarningRule, which fails.

        restWarningRuleMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isBadRequest());

        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllWarningRules() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        // Get all the warningRuleList
        restWarningRuleMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(warningRule.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].mcUserId").value(hasItem(DEFAULT_MC_USER_ID)))
            .andExpect(jsonPath("$.[*].delayCheck").value(hasItem(DEFAULT_DELAY_CHECK)))
            .andExpect(jsonPath("$.[*].delayCheckUnit").value(hasItem(DEFAULT_DELAY_CHECK_UNIT)))
            .andExpect(jsonPath("$.[*].conditionType").value(hasItem(DEFAULT_CONDITION_TYPE)))
            .andExpect(jsonPath("$.[*].includeMcCampaignId").value(hasItem(DEFAULT_INCLUDE_MC_CAMPAIGN_ID)))
            .andExpect(jsonPath("$.[*].includeMcTargetId").value(hasItem(DEFAULT_INCLUDE_MC_TARGET_ID)))
            .andExpect(jsonPath("$.[*].warningDistance").value(hasItem(DEFAULT_WARNING_DISTANCE)))
            .andExpect(jsonPath("$.[*].showWarningCircle").value(hasItem(DEFAULT_SHOW_WARNING_CIRCLE.booleanValue())))
            .andExpect(jsonPath("$.[*].showWarningMessage").value(hasItem(DEFAULT_SHOW_WARNING_MESSAGE.booleanValue())))
            .andExpect(jsonPath("$.[*].warningMessage").value(hasItem(DEFAULT_WARNING_MESSAGE)))
            .andExpect(jsonPath("$.[*].sendWarningMessageToMc").value(hasItem(DEFAULT_SEND_WARNING_MESSAGE_TO_MC.booleanValue())))
            .andExpect(jsonPath("$.[*].status").value(hasItem(DEFAULT_STATUS.booleanValue())))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getWarningRule() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        // Get the warningRule
        restWarningRuleMockMvc
            .perform(get(ENTITY_API_URL_ID, warningRule.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(warningRule.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.mcUserId").value(DEFAULT_MC_USER_ID))
            .andExpect(jsonPath("$.delayCheck").value(DEFAULT_DELAY_CHECK))
            .andExpect(jsonPath("$.delayCheckUnit").value(DEFAULT_DELAY_CHECK_UNIT))
            .andExpect(jsonPath("$.conditionType").value(DEFAULT_CONDITION_TYPE))
            .andExpect(jsonPath("$.includeMcCampaignId").value(DEFAULT_INCLUDE_MC_CAMPAIGN_ID))
            .andExpect(jsonPath("$.includeMcTargetId").value(DEFAULT_INCLUDE_MC_TARGET_ID))
            .andExpect(jsonPath("$.warningDistance").value(DEFAULT_WARNING_DISTANCE))
            .andExpect(jsonPath("$.showWarningCircle").value(DEFAULT_SHOW_WARNING_CIRCLE.booleanValue()))
            .andExpect(jsonPath("$.showWarningMessage").value(DEFAULT_SHOW_WARNING_MESSAGE.booleanValue()))
            .andExpect(jsonPath("$.warningMessage").value(DEFAULT_WARNING_MESSAGE))
            .andExpect(jsonPath("$.sendWarningMessageToMc").value(DEFAULT_SEND_WARNING_MESSAGE_TO_MC.booleanValue()))
            .andExpect(jsonPath("$.status").value(DEFAULT_STATUS.booleanValue()))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingWarningRule() throws Exception {
        // Get the warningRule
        restWarningRuleMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewWarningRule() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();

        // Update the warningRule
        WarningRule updatedWarningRule = warningRuleRepository.findById(warningRule.getId()).get();
        // Disconnect from session so that the updates on updatedWarningRule are not directly saved in db
        em.detach(updatedWarningRule);
        updatedWarningRule
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .delayCheck(UPDATED_DELAY_CHECK)
            .delayCheckUnit(UPDATED_DELAY_CHECK_UNIT)
            .conditionType(UPDATED_CONDITION_TYPE)
            .includeMcCampaignId(UPDATED_INCLUDE_MC_CAMPAIGN_ID)
            .includeMcTargetId(UPDATED_INCLUDE_MC_TARGET_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restWarningRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedWarningRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedWarningRule))
            )
            .andExpect(status().isOk());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
        WarningRule testWarningRule = warningRuleList.get(warningRuleList.size() - 1);
        assertThat(testWarningRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarningRule.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningRule.getDelayCheck()).isEqualTo(UPDATED_DELAY_CHECK);
        assertThat(testWarningRule.getDelayCheckUnit()).isEqualTo(UPDATED_DELAY_CHECK_UNIT);
        assertThat(testWarningRule.getConditionType()).isEqualTo(UPDATED_CONDITION_TYPE);
        assertThat(testWarningRule.getIncludeMcCampaignId()).isEqualTo(UPDATED_INCLUDE_MC_CAMPAIGN_ID);
        assertThat(testWarningRule.getIncludeMcTargetId()).isEqualTo(UPDATED_INCLUDE_MC_TARGET_ID);
        assertThat(testWarningRule.getWarningDistance()).isEqualTo(UPDATED_WARNING_DISTANCE);
        assertThat(testWarningRule.getShowWarningCircle()).isEqualTo(UPDATED_SHOW_WARNING_CIRCLE);
        assertThat(testWarningRule.getShowWarningMessage()).isEqualTo(UPDATED_SHOW_WARNING_MESSAGE);
        assertThat(testWarningRule.getWarningMessage()).isEqualTo(UPDATED_WARNING_MESSAGE);
        assertThat(testWarningRule.getSendWarningMessageToMc()).isEqualTo(UPDATED_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningRule.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWarningRule.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarningRule.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testWarningRule.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningRule.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, warningRule.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warningRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(warningRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(warningRule)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateWarningRuleWithPatch() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();

        // Update the warningRule using partial update
        WarningRule partialUpdatedWarningRule = new WarningRule();
        partialUpdatedWarningRule.setId(warningRule.getId());

        partialUpdatedWarningRule
            .mcUserId(UPDATED_MC_USER_ID)
            .delayCheck(UPDATED_DELAY_CHECK)
            .delayCheckUnit(UPDATED_DELAY_CHECK_UNIT)
            .includeMcCampaignId(UPDATED_INCLUDE_MC_CAMPAIGN_ID)
            .includeMcTargetId(UPDATED_INCLUDE_MC_TARGET_ID)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE);

        restWarningRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarningRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarningRule))
            )
            .andExpect(status().isOk());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
        WarningRule testWarningRule = warningRuleList.get(warningRuleList.size() - 1);
        assertThat(testWarningRule.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testWarningRule.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningRule.getDelayCheck()).isEqualTo(UPDATED_DELAY_CHECK);
        assertThat(testWarningRule.getDelayCheckUnit()).isEqualTo(UPDATED_DELAY_CHECK_UNIT);
        assertThat(testWarningRule.getConditionType()).isEqualTo(DEFAULT_CONDITION_TYPE);
        assertThat(testWarningRule.getIncludeMcCampaignId()).isEqualTo(UPDATED_INCLUDE_MC_CAMPAIGN_ID);
        assertThat(testWarningRule.getIncludeMcTargetId()).isEqualTo(UPDATED_INCLUDE_MC_TARGET_ID);
        assertThat(testWarningRule.getWarningDistance()).isEqualTo(DEFAULT_WARNING_DISTANCE);
        assertThat(testWarningRule.getShowWarningCircle()).isEqualTo(DEFAULT_SHOW_WARNING_CIRCLE);
        assertThat(testWarningRule.getShowWarningMessage()).isEqualTo(UPDATED_SHOW_WARNING_MESSAGE);
        assertThat(testWarningRule.getWarningMessage()).isEqualTo(DEFAULT_WARNING_MESSAGE);
        assertThat(testWarningRule.getSendWarningMessageToMc()).isEqualTo(UPDATED_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningRule.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWarningRule.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarningRule.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testWarningRule.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningRule.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateWarningRuleWithPatch() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();

        // Update the warningRule using partial update
        WarningRule partialUpdatedWarningRule = new WarningRule();
        partialUpdatedWarningRule.setId(warningRule.getId());

        partialUpdatedWarningRule
            .name(UPDATED_NAME)
            .mcUserId(UPDATED_MC_USER_ID)
            .delayCheck(UPDATED_DELAY_CHECK)
            .delayCheckUnit(UPDATED_DELAY_CHECK_UNIT)
            .conditionType(UPDATED_CONDITION_TYPE)
            .includeMcCampaignId(UPDATED_INCLUDE_MC_CAMPAIGN_ID)
            .includeMcTargetId(UPDATED_INCLUDE_MC_TARGET_ID)
            .warningDistance(UPDATED_WARNING_DISTANCE)
            .showWarningCircle(UPDATED_SHOW_WARNING_CIRCLE)
            .showWarningMessage(UPDATED_SHOW_WARNING_MESSAGE)
            .warningMessage(UPDATED_WARNING_MESSAGE)
            .sendWarningMessageToMc(UPDATED_SEND_WARNING_MESSAGE_TO_MC)
            .status(UPDATED_STATUS)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restWarningRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedWarningRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedWarningRule))
            )
            .andExpect(status().isOk());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
        WarningRule testWarningRule = warningRuleList.get(warningRuleList.size() - 1);
        assertThat(testWarningRule.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testWarningRule.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testWarningRule.getDelayCheck()).isEqualTo(UPDATED_DELAY_CHECK);
        assertThat(testWarningRule.getDelayCheckUnit()).isEqualTo(UPDATED_DELAY_CHECK_UNIT);
        assertThat(testWarningRule.getConditionType()).isEqualTo(UPDATED_CONDITION_TYPE);
        assertThat(testWarningRule.getIncludeMcCampaignId()).isEqualTo(UPDATED_INCLUDE_MC_CAMPAIGN_ID);
        assertThat(testWarningRule.getIncludeMcTargetId()).isEqualTo(UPDATED_INCLUDE_MC_TARGET_ID);
        assertThat(testWarningRule.getWarningDistance()).isEqualTo(UPDATED_WARNING_DISTANCE);
        assertThat(testWarningRule.getShowWarningCircle()).isEqualTo(UPDATED_SHOW_WARNING_CIRCLE);
        assertThat(testWarningRule.getShowWarningMessage()).isEqualTo(UPDATED_SHOW_WARNING_MESSAGE);
        assertThat(testWarningRule.getWarningMessage()).isEqualTo(UPDATED_WARNING_MESSAGE);
        assertThat(testWarningRule.getSendWarningMessageToMc()).isEqualTo(UPDATED_SEND_WARNING_MESSAGE_TO_MC);
        assertThat(testWarningRule.getStatus()).isEqualTo(UPDATED_STATUS);
        assertThat(testWarningRule.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testWarningRule.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testWarningRule.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testWarningRule.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, warningRule.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warningRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(warningRule))
            )
            .andExpect(status().isBadRequest());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamWarningRule() throws Exception {
        int databaseSizeBeforeUpdate = warningRuleRepository.findAll().size();
        warningRule.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restWarningRuleMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(warningRule))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the WarningRule in the database
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteWarningRule() throws Exception {
        // Initialize the database
        warningRuleRepository.saveAndFlush(warningRule);

        int databaseSizeBeforeDelete = warningRuleRepository.findAll().size();

        // Delete the warningRule
        restWarningRuleMockMvc
            .perform(delete(ENTITY_API_URL_ID, warningRule.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<WarningRule> warningRuleList = warningRuleRepository.findAll();
        assertThat(warningRuleList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
