package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ConfigSettingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ConfigSetting.class);
        ConfigSetting configSetting1 = new ConfigSetting();
        configSetting1.setId(1L);
        ConfigSetting configSetting2 = new ConfigSetting();
        configSetting2.setId(configSetting1.getId());
        assertThat(configSetting1).isEqualTo(configSetting2);
        configSetting2.setId(2L);
        assertThat(configSetting1).isNotEqualTo(configSetting2);
        configSetting1.setId(null);
        assertThat(configSetting1).isNotEqualTo(configSetting2);
    }
}
