package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.ResourceBookingTestSamples.*;
import static com.smartcampusmanagmentsystem.domain.ResourceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceBookingTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceBooking.class);
        ResourceBooking resourceBooking1 = getResourceBookingSample1();
        ResourceBooking resourceBooking2 = new ResourceBooking();
        assertThat(resourceBooking1).isNotEqualTo(resourceBooking2);

        resourceBooking2.setId(resourceBooking1.getId());
        assertThat(resourceBooking1).isEqualTo(resourceBooking2);

        resourceBooking2 = getResourceBookingSample2();
        assertThat(resourceBooking1).isNotEqualTo(resourceBooking2);
    }

    @Test
    void resourceTest() {
        ResourceBooking resourceBooking = getResourceBookingRandomSampleGenerator();
        Resource resourceBack = getResourceRandomSampleGenerator();

        resourceBooking.setResource(resourceBack);
        assertThat(resourceBooking.getResource()).isEqualTo(resourceBack);

        resourceBooking.resource(null);
        assertThat(resourceBooking.getResource()).isNull();
    }
}
