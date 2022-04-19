package com.emap.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import com.emap.IntegrationTest;
import com.emap.domain.ConfigSetting;
import com.emap.repository.ConfigSettingRepository;
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
 * Integration tests for the {@link ConfigSettingResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class ConfigSettingResourceIT {

    private static final String DEFAULT_SOURCE_TYPE = "AAAAAAAAAA";
    private static final String UPDATED_SOURCE_TYPE = "BBBBBBBBBB";

    private static final String DEFAULT_MC_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_MC_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_TML_USER_ID = "AAAAAAAAAA";
    private static final String UPDATED_TML_USER_ID = "BBBBBBBBBB";

    private static final String DEFAULT_VM_SYS_DEFAULT_MODE_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_SYS_DEFAULT_MODE_CONF = "BBBBBBBBBB";

    private static final Integer DEFAULT_VM_SYS_SYNC_CYCLE_CONF = 1;
    private static final Integer UPDATED_VM_SYS_SYNC_CYCLE_CONF = 2;

    private static final String DEFAULT_VM_SYS_SYNC_CYCLE_UNIT_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF = "BBBBBBBBBB";

    private static final String DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF = "BBBBBBBBBB";

    private static final String DEFAULT_VM_LIVE_DEFAULT_MODE_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_LIVE_DEFAULT_MODE_CONF = "BBBBBBBBBB";

    private static final String DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF = "BBBBBBBBBB";

    private static final Integer DEFAULT_VM_LIVE_POSITION_CYCLE_CONF = 1;
    private static final Integer UPDATED_VM_LIVE_POSITION_CYCLE_CONF = 2;

    private static final String DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF = "BBBBBBBBBB";

    private static final Integer DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF = 1;
    private static final Integer UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF = 2;

    private static final String DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF = "AAAAAAAAAA";
    private static final String UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF = "BBBBBBBBBB";

    private static final Integer DEFAULT_SAR_SYS_SYNC_CYCLE_CONF = 1;
    private static final Integer UPDATED_SAR_SYS_SYNC_CYCLE_CONF = 2;

    private static final String DEFAULT_SAR_SYS_SYNC_CYCLE_UNIT_CONF = "AAAAAAAAAA";
    private static final String UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF = "BBBBBBBBBB";

    private static final String DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF = "AAAAAAAAAA";
    private static final String UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF = "BBBBBBBBBB";

    private static final String DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF = "AAAAAAAAAA";
    private static final String UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF = "BBBBBBBBBB";

    private static final Instant DEFAULT_CREATE_DATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_CREATE_DATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_CREATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_CREATE_UID = "BBBBBBBBBB";

    private static final Instant DEFAULT_LAST_UPDATE = Instant.ofEpochMilli(0L);
    private static final Instant UPDATED_LAST_UPDATE = Instant.now().truncatedTo(ChronoUnit.MILLIS);

    private static final String DEFAULT_LAST_UPDATE_UID = "AAAAAAAAAA";
    private static final String UPDATED_LAST_UPDATE_UID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/config-settings";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private ConfigSettingRepository configSettingRepository;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restConfigSettingMockMvc;

    private ConfigSetting configSetting;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigSetting createEntity(EntityManager em) {
        ConfigSetting configSetting = new ConfigSetting()
            .sourceType(DEFAULT_SOURCE_TYPE)
            .mcUserId(DEFAULT_MC_USER_ID)
            .tmlUserId(DEFAULT_TML_USER_ID)
            .vmSysDefaultModeConf(DEFAULT_VM_SYS_DEFAULT_MODE_CONF)
            .vmSysSyncCycleConf(DEFAULT_VM_SYS_SYNC_CYCLE_CONF)
            .vmSysSyncCycleUnitConf(DEFAULT_VM_SYS_SYNC_CYCLE_UNIT_CONF)
            .vmSysTargetDisplayNameConf(DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF)
            .vmLiveDefaultModeConf(DEFAULT_VM_LIVE_DEFAULT_MODE_CONF)
            .vmLiveDefaultTimerangeConf(DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF)
            .vmLivePositionCycleConf(DEFAULT_VM_LIVE_POSITION_CYCLE_CONF)
            .vmLivePositionCycleUnitConf(DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF)
            .vmLiveTrackingAmplitudeConf(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF)
            .vmLiveTrackingAmplitudeUnitConf(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF)
            .sarSysSyncCycleConf(DEFAULT_SAR_SYS_SYNC_CYCLE_CONF)
            .sarSysSyncCycleUnitConf(DEFAULT_SAR_SYS_SYNC_CYCLE_UNIT_CONF)
            .sarSysObjectDisplayName01Conf(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF)
            .sarSysObjectDisplayName02Conf(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF)
            .createDate(DEFAULT_CREATE_DATE)
            .createUid(DEFAULT_CREATE_UID)
            .lastUpdate(DEFAULT_LAST_UPDATE)
            .lastUpdateUid(DEFAULT_LAST_UPDATE_UID);
        return configSetting;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static ConfigSetting createUpdatedEntity(EntityManager em) {
        ConfigSetting configSetting = new ConfigSetting()
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcUserId(UPDATED_MC_USER_ID)
            .tmlUserId(UPDATED_TML_USER_ID)
            .vmSysDefaultModeConf(UPDATED_VM_SYS_DEFAULT_MODE_CONF)
            .vmSysSyncCycleConf(UPDATED_VM_SYS_SYNC_CYCLE_CONF)
            .vmSysSyncCycleUnitConf(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF)
            .vmSysTargetDisplayNameConf(UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF)
            .vmLiveDefaultModeConf(UPDATED_VM_LIVE_DEFAULT_MODE_CONF)
            .vmLiveDefaultTimerangeConf(UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF)
            .vmLivePositionCycleConf(UPDATED_VM_LIVE_POSITION_CYCLE_CONF)
            .vmLivePositionCycleUnitConf(UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF)
            .vmLiveTrackingAmplitudeConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF)
            .vmLiveTrackingAmplitudeUnitConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF)
            .sarSysSyncCycleConf(UPDATED_SAR_SYS_SYNC_CYCLE_CONF)
            .sarSysSyncCycleUnitConf(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF)
            .sarSysObjectDisplayName01Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF)
            .sarSysObjectDisplayName02Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);
        return configSetting;
    }

    @BeforeEach
    public void initTest() {
        configSetting = createEntity(em);
    }

    @Test
    @Transactional
    void createConfigSetting() throws Exception {
        int databaseSizeBeforeCreate = configSettingRepository.findAll().size();
        // Create the ConfigSetting
        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isCreated());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeCreate + 1);
        ConfigSetting testConfigSetting = configSettingList.get(configSettingList.size() - 1);
        assertThat(testConfigSetting.getSourceType()).isEqualTo(DEFAULT_SOURCE_TYPE);
        assertThat(testConfigSetting.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testConfigSetting.getTmlUserId()).isEqualTo(DEFAULT_TML_USER_ID);
        assertThat(testConfigSetting.getVmSysDefaultModeConf()).isEqualTo(DEFAULT_VM_SYS_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleConf()).isEqualTo(DEFAULT_VM_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleUnitConf()).isEqualTo(DEFAULT_VM_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmSysTargetDisplayNameConf()).isEqualTo(DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultModeConf()).isEqualTo(DEFAULT_VM_LIVE_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultTimerangeConf()).isEqualTo(DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleConf()).isEqualTo(DEFAULT_VM_LIVE_POSITION_CYCLE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleUnitConf()).isEqualTo(DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeConf()).isEqualTo(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeUnitConf()).isEqualTo(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleConf()).isEqualTo(DEFAULT_SAR_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleUnitConf()).isEqualTo(DEFAULT_SAR_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName01Conf()).isEqualTo(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName02Conf()).isEqualTo(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF);
        assertThat(testConfigSetting.getCreateDate()).isEqualTo(DEFAULT_CREATE_DATE);
        assertThat(testConfigSetting.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testConfigSetting.getLastUpdate()).isEqualTo(DEFAULT_LAST_UPDATE);
        assertThat(testConfigSetting.getLastUpdateUid()).isEqualTo(DEFAULT_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void createConfigSettingWithExistingId() throws Exception {
        // Create the ConfigSetting with an existing ID
        configSetting.setId(1L);

        int databaseSizeBeforeCreate = configSettingRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void checkSourceTypeIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setSourceType(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmSysDefaultModeConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmSysDefaultModeConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmSysSyncCycleConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmSysSyncCycleConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmSysSyncCycleUnitConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmSysSyncCycleUnitConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmSysTargetDisplayNameConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmSysTargetDisplayNameConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLiveDefaultModeConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLiveDefaultModeConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLiveDefaultTimerangeConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLiveDefaultTimerangeConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLivePositionCycleConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLivePositionCycleConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLivePositionCycleUnitConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLivePositionCycleUnitConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLiveTrackingAmplitudeConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLiveTrackingAmplitudeConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkVmLiveTrackingAmplitudeUnitConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setVmLiveTrackingAmplitudeUnitConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSarSysSyncCycleConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setSarSysSyncCycleConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSarSysSyncCycleUnitConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setSarSysSyncCycleUnitConf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSarSysObjectDisplayName01ConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setSarSysObjectDisplayName01Conf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkSarSysObjectDisplayName02ConfIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setSarSysObjectDisplayName02Conf(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateDateIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setCreateDate(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkCreateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setCreateUid(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setLastUpdate(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void checkLastUpdateUidIsRequired() throws Exception {
        int databaseSizeBeforeTest = configSettingRepository.findAll().size();
        // set the field null
        configSetting.setLastUpdateUid(null);

        // Create the ConfigSetting, which fails.

        restConfigSettingMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isBadRequest());

        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeTest);
    }

    @Test
    @Transactional
    void getAllConfigSettings() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        // Get all the configSettingList
        restConfigSettingMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(configSetting.getId().intValue())))
            .andExpect(jsonPath("$.[*].sourceType").value(hasItem(DEFAULT_SOURCE_TYPE)))
            .andExpect(jsonPath("$.[*].mcUserId").value(hasItem(DEFAULT_MC_USER_ID)))
            .andExpect(jsonPath("$.[*].tmlUserId").value(hasItem(DEFAULT_TML_USER_ID)))
            .andExpect(jsonPath("$.[*].vmSysDefaultModeConf").value(hasItem(DEFAULT_VM_SYS_DEFAULT_MODE_CONF)))
            .andExpect(jsonPath("$.[*].vmSysSyncCycleConf").value(hasItem(DEFAULT_VM_SYS_SYNC_CYCLE_CONF)))
            .andExpect(jsonPath("$.[*].vmSysSyncCycleUnitConf").value(hasItem(DEFAULT_VM_SYS_SYNC_CYCLE_UNIT_CONF)))
            .andExpect(jsonPath("$.[*].vmSysTargetDisplayNameConf").value(hasItem(DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF)))
            .andExpect(jsonPath("$.[*].vmLiveDefaultModeConf").value(hasItem(DEFAULT_VM_LIVE_DEFAULT_MODE_CONF)))
            .andExpect(jsonPath("$.[*].vmLiveDefaultTimerangeConf").value(hasItem(DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF)))
            .andExpect(jsonPath("$.[*].vmLivePositionCycleConf").value(hasItem(DEFAULT_VM_LIVE_POSITION_CYCLE_CONF)))
            .andExpect(jsonPath("$.[*].vmLivePositionCycleUnitConf").value(hasItem(DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF)))
            .andExpect(jsonPath("$.[*].vmLiveTrackingAmplitudeConf").value(hasItem(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF)))
            .andExpect(jsonPath("$.[*].vmLiveTrackingAmplitudeUnitConf").value(hasItem(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF)))
            .andExpect(jsonPath("$.[*].sarSysSyncCycleConf").value(hasItem(DEFAULT_SAR_SYS_SYNC_CYCLE_CONF)))
            .andExpect(jsonPath("$.[*].sarSysSyncCycleUnitConf").value(hasItem(DEFAULT_SAR_SYS_SYNC_CYCLE_UNIT_CONF)))
            .andExpect(jsonPath("$.[*].sarSysObjectDisplayName01Conf").value(hasItem(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF)))
            .andExpect(jsonPath("$.[*].sarSysObjectDisplayName02Conf").value(hasItem(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF)))
            .andExpect(jsonPath("$.[*].createDate").value(hasItem(DEFAULT_CREATE_DATE.toString())))
            .andExpect(jsonPath("$.[*].createUid").value(hasItem(DEFAULT_CREATE_UID)))
            .andExpect(jsonPath("$.[*].lastUpdate").value(hasItem(DEFAULT_LAST_UPDATE.toString())))
            .andExpect(jsonPath("$.[*].lastUpdateUid").value(hasItem(DEFAULT_LAST_UPDATE_UID)));
    }

    @Test
    @Transactional
    void getConfigSetting() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        // Get the configSetting
        restConfigSettingMockMvc
            .perform(get(ENTITY_API_URL_ID, configSetting.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(configSetting.getId().intValue()))
            .andExpect(jsonPath("$.sourceType").value(DEFAULT_SOURCE_TYPE))
            .andExpect(jsonPath("$.mcUserId").value(DEFAULT_MC_USER_ID))
            .andExpect(jsonPath("$.tmlUserId").value(DEFAULT_TML_USER_ID))
            .andExpect(jsonPath("$.vmSysDefaultModeConf").value(DEFAULT_VM_SYS_DEFAULT_MODE_CONF))
            .andExpect(jsonPath("$.vmSysSyncCycleConf").value(DEFAULT_VM_SYS_SYNC_CYCLE_CONF))
            .andExpect(jsonPath("$.vmSysSyncCycleUnitConf").value(DEFAULT_VM_SYS_SYNC_CYCLE_UNIT_CONF))
            .andExpect(jsonPath("$.vmSysTargetDisplayNameConf").value(DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF))
            .andExpect(jsonPath("$.vmLiveDefaultModeConf").value(DEFAULT_VM_LIVE_DEFAULT_MODE_CONF))
            .andExpect(jsonPath("$.vmLiveDefaultTimerangeConf").value(DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF))
            .andExpect(jsonPath("$.vmLivePositionCycleConf").value(DEFAULT_VM_LIVE_POSITION_CYCLE_CONF))
            .andExpect(jsonPath("$.vmLivePositionCycleUnitConf").value(DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF))
            .andExpect(jsonPath("$.vmLiveTrackingAmplitudeConf").value(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF))
            .andExpect(jsonPath("$.vmLiveTrackingAmplitudeUnitConf").value(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF))
            .andExpect(jsonPath("$.sarSysSyncCycleConf").value(DEFAULT_SAR_SYS_SYNC_CYCLE_CONF))
            .andExpect(jsonPath("$.sarSysSyncCycleUnitConf").value(DEFAULT_SAR_SYS_SYNC_CYCLE_UNIT_CONF))
            .andExpect(jsonPath("$.sarSysObjectDisplayName01Conf").value(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF))
            .andExpect(jsonPath("$.sarSysObjectDisplayName02Conf").value(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF))
            .andExpect(jsonPath("$.createDate").value(DEFAULT_CREATE_DATE.toString()))
            .andExpect(jsonPath("$.createUid").value(DEFAULT_CREATE_UID))
            .andExpect(jsonPath("$.lastUpdate").value(DEFAULT_LAST_UPDATE.toString()))
            .andExpect(jsonPath("$.lastUpdateUid").value(DEFAULT_LAST_UPDATE_UID));
    }

    @Test
    @Transactional
    void getNonExistingConfigSetting() throws Exception {
        // Get the configSetting
        restConfigSettingMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewConfigSetting() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();

        // Update the configSetting
        ConfigSetting updatedConfigSetting = configSettingRepository.findById(configSetting.getId()).get();
        // Disconnect from session so that the updates on updatedConfigSetting are not directly saved in db
        em.detach(updatedConfigSetting);
        updatedConfigSetting
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcUserId(UPDATED_MC_USER_ID)
            .tmlUserId(UPDATED_TML_USER_ID)
            .vmSysDefaultModeConf(UPDATED_VM_SYS_DEFAULT_MODE_CONF)
            .vmSysSyncCycleConf(UPDATED_VM_SYS_SYNC_CYCLE_CONF)
            .vmSysSyncCycleUnitConf(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF)
            .vmSysTargetDisplayNameConf(UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF)
            .vmLiveDefaultModeConf(UPDATED_VM_LIVE_DEFAULT_MODE_CONF)
            .vmLiveDefaultTimerangeConf(UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF)
            .vmLivePositionCycleConf(UPDATED_VM_LIVE_POSITION_CYCLE_CONF)
            .vmLivePositionCycleUnitConf(UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF)
            .vmLiveTrackingAmplitudeConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF)
            .vmLiveTrackingAmplitudeUnitConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF)
            .sarSysSyncCycleConf(UPDATED_SAR_SYS_SYNC_CYCLE_CONF)
            .sarSysSyncCycleUnitConf(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF)
            .sarSysObjectDisplayName01Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF)
            .sarSysObjectDisplayName02Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restConfigSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, updatedConfigSetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(updatedConfigSetting))
            )
            .andExpect(status().isOk());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
        ConfigSetting testConfigSetting = configSettingList.get(configSettingList.size() - 1);
        assertThat(testConfigSetting.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testConfigSetting.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testConfigSetting.getTmlUserId()).isEqualTo(UPDATED_TML_USER_ID);
        assertThat(testConfigSetting.getVmSysDefaultModeConf()).isEqualTo(UPDATED_VM_SYS_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleUnitConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmSysTargetDisplayNameConf()).isEqualTo(UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultModeConf()).isEqualTo(UPDATED_VM_LIVE_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultTimerangeConf()).isEqualTo(UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleConf()).isEqualTo(UPDATED_VM_LIVE_POSITION_CYCLE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleUnitConf()).isEqualTo(UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeConf()).isEqualTo(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeUnitConf()).isEqualTo(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleConf()).isEqualTo(UPDATED_SAR_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleUnitConf()).isEqualTo(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName01Conf()).isEqualTo(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName02Conf()).isEqualTo(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF);
        assertThat(testConfigSetting.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testConfigSetting.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testConfigSetting.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testConfigSetting.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void putNonExistingConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, configSetting.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(configSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(configSetting)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateConfigSettingWithPatch() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();

        // Update the configSetting using partial update
        ConfigSetting partialUpdatedConfigSetting = new ConfigSetting();
        partialUpdatedConfigSetting.setId(configSetting.getId());

        partialUpdatedConfigSetting
            .sourceType(UPDATED_SOURCE_TYPE)
            .tmlUserId(UPDATED_TML_USER_ID)
            .vmSysDefaultModeConf(UPDATED_VM_SYS_DEFAULT_MODE_CONF)
            .vmSysSyncCycleConf(UPDATED_VM_SYS_SYNC_CYCLE_CONF)
            .vmSysSyncCycleUnitConf(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF)
            .vmLiveDefaultModeConf(UPDATED_VM_LIVE_DEFAULT_MODE_CONF)
            .sarSysSyncCycleUnitConf(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF)
            .createDate(UPDATED_CREATE_DATE)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restConfigSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigSetting))
            )
            .andExpect(status().isOk());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
        ConfigSetting testConfigSetting = configSettingList.get(configSettingList.size() - 1);
        assertThat(testConfigSetting.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testConfigSetting.getMcUserId()).isEqualTo(DEFAULT_MC_USER_ID);
        assertThat(testConfigSetting.getTmlUserId()).isEqualTo(UPDATED_TML_USER_ID);
        assertThat(testConfigSetting.getVmSysDefaultModeConf()).isEqualTo(UPDATED_VM_SYS_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleUnitConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmSysTargetDisplayNameConf()).isEqualTo(DEFAULT_VM_SYS_TARGET_DISPLAY_NAME_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultModeConf()).isEqualTo(UPDATED_VM_LIVE_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultTimerangeConf()).isEqualTo(DEFAULT_VM_LIVE_DEFAULT_TIMERANGE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleConf()).isEqualTo(DEFAULT_VM_LIVE_POSITION_CYCLE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleUnitConf()).isEqualTo(DEFAULT_VM_LIVE_POSITION_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeConf()).isEqualTo(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeUnitConf()).isEqualTo(DEFAULT_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleConf()).isEqualTo(DEFAULT_SAR_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleUnitConf()).isEqualTo(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName01Conf()).isEqualTo(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName02Conf()).isEqualTo(DEFAULT_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF);
        assertThat(testConfigSetting.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testConfigSetting.getCreateUid()).isEqualTo(DEFAULT_CREATE_UID);
        assertThat(testConfigSetting.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testConfigSetting.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void fullUpdateConfigSettingWithPatch() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();

        // Update the configSetting using partial update
        ConfigSetting partialUpdatedConfigSetting = new ConfigSetting();
        partialUpdatedConfigSetting.setId(configSetting.getId());

        partialUpdatedConfigSetting
            .sourceType(UPDATED_SOURCE_TYPE)
            .mcUserId(UPDATED_MC_USER_ID)
            .tmlUserId(UPDATED_TML_USER_ID)
            .vmSysDefaultModeConf(UPDATED_VM_SYS_DEFAULT_MODE_CONF)
            .vmSysSyncCycleConf(UPDATED_VM_SYS_SYNC_CYCLE_CONF)
            .vmSysSyncCycleUnitConf(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF)
            .vmSysTargetDisplayNameConf(UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF)
            .vmLiveDefaultModeConf(UPDATED_VM_LIVE_DEFAULT_MODE_CONF)
            .vmLiveDefaultTimerangeConf(UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF)
            .vmLivePositionCycleConf(UPDATED_VM_LIVE_POSITION_CYCLE_CONF)
            .vmLivePositionCycleUnitConf(UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF)
            .vmLiveTrackingAmplitudeConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF)
            .vmLiveTrackingAmplitudeUnitConf(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF)
            .sarSysSyncCycleConf(UPDATED_SAR_SYS_SYNC_CYCLE_CONF)
            .sarSysSyncCycleUnitConf(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF)
            .sarSysObjectDisplayName01Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF)
            .sarSysObjectDisplayName02Conf(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF)
            .createDate(UPDATED_CREATE_DATE)
            .createUid(UPDATED_CREATE_UID)
            .lastUpdate(UPDATED_LAST_UPDATE)
            .lastUpdateUid(UPDATED_LAST_UPDATE_UID);

        restConfigSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedConfigSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedConfigSetting))
            )
            .andExpect(status().isOk());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
        ConfigSetting testConfigSetting = configSettingList.get(configSettingList.size() - 1);
        assertThat(testConfigSetting.getSourceType()).isEqualTo(UPDATED_SOURCE_TYPE);
        assertThat(testConfigSetting.getMcUserId()).isEqualTo(UPDATED_MC_USER_ID);
        assertThat(testConfigSetting.getTmlUserId()).isEqualTo(UPDATED_TML_USER_ID);
        assertThat(testConfigSetting.getVmSysDefaultModeConf()).isEqualTo(UPDATED_VM_SYS_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getVmSysSyncCycleUnitConf()).isEqualTo(UPDATED_VM_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmSysTargetDisplayNameConf()).isEqualTo(UPDATED_VM_SYS_TARGET_DISPLAY_NAME_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultModeConf()).isEqualTo(UPDATED_VM_LIVE_DEFAULT_MODE_CONF);
        assertThat(testConfigSetting.getVmLiveDefaultTimerangeConf()).isEqualTo(UPDATED_VM_LIVE_DEFAULT_TIMERANGE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleConf()).isEqualTo(UPDATED_VM_LIVE_POSITION_CYCLE_CONF);
        assertThat(testConfigSetting.getVmLivePositionCycleUnitConf()).isEqualTo(UPDATED_VM_LIVE_POSITION_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeConf()).isEqualTo(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_CONF);
        assertThat(testConfigSetting.getVmLiveTrackingAmplitudeUnitConf()).isEqualTo(UPDATED_VM_LIVE_TRACKING_AMPLITUDE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleConf()).isEqualTo(UPDATED_SAR_SYS_SYNC_CYCLE_CONF);
        assertThat(testConfigSetting.getSarSysSyncCycleUnitConf()).isEqualTo(UPDATED_SAR_SYS_SYNC_CYCLE_UNIT_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName01Conf()).isEqualTo(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_01_CONF);
        assertThat(testConfigSetting.getSarSysObjectDisplayName02Conf()).isEqualTo(UPDATED_SAR_SYS_OBJECT_DISPLAY_NAME_02_CONF);
        assertThat(testConfigSetting.getCreateDate()).isEqualTo(UPDATED_CREATE_DATE);
        assertThat(testConfigSetting.getCreateUid()).isEqualTo(UPDATED_CREATE_UID);
        assertThat(testConfigSetting.getLastUpdate()).isEqualTo(UPDATED_LAST_UPDATE);
        assertThat(testConfigSetting.getLastUpdateUid()).isEqualTo(UPDATED_LAST_UPDATE_UID);
    }

    @Test
    @Transactional
    void patchNonExistingConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, configSetting.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(configSetting))
            )
            .andExpect(status().isBadRequest());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamConfigSetting() throws Exception {
        int databaseSizeBeforeUpdate = configSettingRepository.findAll().size();
        configSetting.setId(count.incrementAndGet());

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restConfigSettingMockMvc
            .perform(
                patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(configSetting))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the ConfigSetting in the database
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteConfigSetting() throws Exception {
        // Initialize the database
        configSettingRepository.saveAndFlush(configSetting);

        int databaseSizeBeforeDelete = configSettingRepository.findAll().size();

        // Delete the configSetting
        restConfigSettingMockMvc
            .perform(delete(ENTITY_API_URL_ID, configSetting.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<ConfigSetting> configSettingList = configSettingRepository.findAll();
        assertThat(configSettingList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
