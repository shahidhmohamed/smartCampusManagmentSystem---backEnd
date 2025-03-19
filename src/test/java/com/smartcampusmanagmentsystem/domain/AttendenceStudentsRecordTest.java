package com.smartcampusmanagmentsystem.domain;

import static com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecordTestSamples.*;
import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendenceStudentsRecordTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendenceStudentsRecord.class);
        AttendenceStudentsRecord attendenceStudentsRecord1 = getAttendenceStudentsRecordSample1();
        AttendenceStudentsRecord attendenceStudentsRecord2 = new AttendenceStudentsRecord();
        assertThat(attendenceStudentsRecord1).isNotEqualTo(attendenceStudentsRecord2);

        attendenceStudentsRecord2.setId(attendenceStudentsRecord1.getId());
        assertThat(attendenceStudentsRecord1).isEqualTo(attendenceStudentsRecord2);

        attendenceStudentsRecord2 = getAttendenceStudentsRecordSample2();
        assertThat(attendenceStudentsRecord1).isNotEqualTo(attendenceStudentsRecord2);
    }
}
