package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarningRuleTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarningRule.class);
        WarningRule warningRule1 = new WarningRule();
        warningRule1.setId(1L);
        WarningRule warningRule2 = new WarningRule();
        warningRule2.setId(warningRule1.getId());
        assertThat(warningRule1).isEqualTo(warningRule2);
        warningRule2.setId(2L);
        assertThat(warningRule1).isNotEqualTo(warningRule2);
        warningRule1.setId(null);
        assertThat(warningRule1).isNotEqualTo(warningRule2);
    }
}
