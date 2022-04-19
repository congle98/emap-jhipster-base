package com.emap.domain;

import static org.assertj.core.api.Assertions.assertThat;

import com.emap.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class TrackingListTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(TrackingList.class);
        TrackingList trackingList1 = new TrackingList();
        trackingList1.setId(1L);
        TrackingList trackingList2 = new TrackingList();
        trackingList2.setId(trackingList1.getId());
        assertThat(trackingList1).isEqualTo(trackingList2);
        trackingList2.setId(2L);
        assertThat(trackingList1).isNotEqualTo(trackingList2);
        trackingList1.setId(null);
        assertThat(trackingList1).isNotEqualTo(trackingList2);
    }
}
