package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendenceDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendenceDTO.class);
        AttendenceDTO attendenceDTO1 = new AttendenceDTO();
        attendenceDTO1.setId("id1");
        AttendenceDTO attendenceDTO2 = new AttendenceDTO();
        assertThat(attendenceDTO1).isNotEqualTo(attendenceDTO2);
        attendenceDTO2.setId(attendenceDTO1.getId());
        assertThat(attendenceDTO1).isEqualTo(attendenceDTO2);
        attendenceDTO2.setId("id2");
        assertThat(attendenceDTO1).isNotEqualTo(attendenceDTO2);
        attendenceDTO1.setId(null);
        assertThat(attendenceDTO1).isNotEqualTo(attendenceDTO2);
    }
}
