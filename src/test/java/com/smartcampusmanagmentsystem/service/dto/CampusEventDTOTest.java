package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CampusEventDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CampusEventDTO.class);
        CampusEventDTO campusEventDTO1 = new CampusEventDTO();
        campusEventDTO1.setId("id1");
        CampusEventDTO campusEventDTO2 = new CampusEventDTO();
        assertThat(campusEventDTO1).isNotEqualTo(campusEventDTO2);
        campusEventDTO2.setId(campusEventDTO1.getId());
        assertThat(campusEventDTO1).isEqualTo(campusEventDTO2);
        campusEventDTO2.setId("id2");
        assertThat(campusEventDTO1).isNotEqualTo(campusEventDTO2);
        campusEventDTO1.setId(null);
        assertThat(campusEventDTO1).isNotEqualTo(campusEventDTO2);
    }
}
