package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.AttendenceTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendenceTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(Attendence.class);
        Attendence attendence1 = getAttendenceSample1();
        Attendence attendence2 = new Attendence();
        assertThat(attendence1).isNotEqualTo(attendence2);

        attendence2.setId(attendence1.getId());
        assertThat(attendence1).isEqualTo(attendence2);

        attendence2 = getAttendenceSample2();
        assertThat(attendence1).isNotEqualTo(attendence2);
    }
}
