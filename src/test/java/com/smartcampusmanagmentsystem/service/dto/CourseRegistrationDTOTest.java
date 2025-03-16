package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseRegistrationDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseRegistrationDTO.class);
        CourseRegistrationDTO courseRegistrationDTO1 = new CourseRegistrationDTO();
        courseRegistrationDTO1.setId("id1");
        CourseRegistrationDTO courseRegistrationDTO2 = new CourseRegistrationDTO();
        assertThat(courseRegistrationDTO1).isNotEqualTo(courseRegistrationDTO2);
        courseRegistrationDTO2.setId(courseRegistrationDTO1.getId());
        assertThat(courseRegistrationDTO1).isEqualTo(courseRegistrationDTO2);
        courseRegistrationDTO2.setId("id2");
        assertThat(courseRegistrationDTO1).isNotEqualTo(courseRegistrationDTO2);
        courseRegistrationDTO1.setId(null);
        assertThat(courseRegistrationDTO1).isNotEqualTo(courseRegistrationDTO2);
    }
}
