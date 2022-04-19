package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackingListDetailsTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackingListDetails.class);
        TrackingListDetails trackingListDetails1 = new TrackingListDetails();
        trackingListDetails1.setId(1L);
        TrackingListDetails trackingListDetails2 = new TrackingListDetails();
        trackingListDetails2.setId(trackingListDetails1.getId());
        assertThat(trackingListDetails1).isEqualTo(trackingListDetails2);
        trackingListDetails2.setId(2L);
        assertThat(trackingListDetails1).isNotEqualTo(trackingListDetails2);
        trackingListDetails1.setId(null);
        assertThat(trackingListDetails1).isNotEqualTo(trackingListDetails2);
    }
}
