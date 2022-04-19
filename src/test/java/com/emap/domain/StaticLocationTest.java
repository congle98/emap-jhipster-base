package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class StaticLocationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(StaticLocation.class);
        StaticLocation staticLocation1 = new StaticLocation();
        staticLocation1.setId(1L);
        StaticLocation staticLocation2 = new StaticLocation();
        staticLocation2.setId(staticLocation1.getId());
        assertThat(staticLocation1).isEqualTo(staticLocation2);
        staticLocation2.setId(2L);
        assertThat(staticLocation1).isNotEqualTo(staticLocation2);
        staticLocation1.setId(null);
        assertThat(staticLocation1).isNotEqualTo(staticLocation2);
    }
}
