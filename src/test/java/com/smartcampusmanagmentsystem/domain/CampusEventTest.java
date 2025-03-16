package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.CampusEventTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CampusEventTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampusEvent.class);
        CampusEvent campusEvent1 = getCampusEventSample1();
        CampusEvent campusEvent2 = new CampusEvent();
        assertThat(campusEvent1).isNotEqualTo(campusEvent2);

        campusEvent2.setId(campusEvent1.getId());
        assertThat(campusEvent1).isEqualTo(campusEvent2);

        campusEvent2 = getCampusEventSample2();
        assertThat(campusEvent1).isNotEqualTo(campusEvent2);
    }
}
