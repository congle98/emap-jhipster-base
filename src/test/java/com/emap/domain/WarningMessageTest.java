package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class WarningMessageTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(WarningMessage.class);
        WarningMessage warningMessage1 = new WarningMessage();
        warningMessage1.setId(1L);
        WarningMessage warningMessage2 = new WarningMessage();
        warningMessage2.setId(warningMessage1.getId());
        assertThat(warningMessage1).isEqualTo(warningMessage2);
        warningMessage2.setId(2L);
        assertThat(warningMessage1).isNotEqualTo(warningMessage2);
        warningMessage1.setId(null);
        assertThat(warningMessage1).isNotEqualTo(warningMessage2);
    }
}
