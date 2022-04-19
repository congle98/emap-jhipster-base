package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CoordinatesDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CoordinatesDetails.class);
        CoordinatesDetails coordinatesDetails1 = new CoordinatesDetails();
        coordinatesDetails1.setId(1L);
        CoordinatesDetails coordinatesDetails2 = new CoordinatesDetails();
        coordinatesDetails2.setId(coordinatesDetails1.getId());
        assertThat(coordinatesDetails1).isEqualTo(coordinatesDetails2);
        coordinatesDetails2.setId(2L);
        assertThat(coordinatesDetails1).isNotEqualTo(coordinatesDetails2);
        coordinatesDetails1.setId(null);
        assertThat(coordinatesDetails1).isNotEqualTo(coordinatesDetails2);
    }
}
