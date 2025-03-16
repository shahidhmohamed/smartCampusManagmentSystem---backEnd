package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.CourseRegistrationTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class CourseRegistrationTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(CourseRegistration.class);
        CourseRegistration courseRegistration1 = getCourseRegistrationSample1();
        CourseRegistration courseRegistration2 = new CourseRegistration();
        assertThat(courseRegistration1).isNotEqualTo(courseRegistration2);

        courseRegistration2.setId(courseRegistration1.getId());
        assertThat(courseRegistration1).isEqualTo(courseRegistration2);

        courseRegistration2 = getCourseRegistrationSample2();
        assertThat(courseRegistration1).isNotEqualTo(courseRegistration2);
    }
}
