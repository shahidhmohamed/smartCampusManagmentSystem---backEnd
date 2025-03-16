package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class ResourceBookingDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(ResourceBookingDTO.class);
        ResourceBookingDTO resourceBookingDTO1 = new ResourceBookingDTO();
        resourceBookingDTO1.setId("id1");
        ResourceBookingDTO resourceBookingDTO2 = new ResourceBookingDTO();
        assertThat(resourceBookingDTO1).isNotEqualTo(resourceBookingDTO2);
        resourceBookingDTO2.setId(resourceBookingDTO1.getId());
        assertThat(resourceBookingDTO1).isEqualTo(resourceBookingDTO2);
        resourceBookingDTO2.setId("id2");
        assertThat(resourceBookingDTO1).isNotEqualTo(resourceBookingDTO2);
        resourceBookingDTO1.setId(null);
        assertThat(resourceBookingDTO1).isNotEqualTo(resourceBookingDTO2);
    }
}
