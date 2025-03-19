package com.smartcampusmanagmentsystem.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import com.smartcampusmanagmentsystem.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class AttendenceStudentsRecordDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(AttendenceStudentsRecordDTO.class);
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO1 = new AttendenceStudentsRecordDTO();
        attendenceStudentsRecordDTO1.setId("id1");
        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO2 = new AttendenceStudentsRecordDTO();
        assertThat(attendenceStudentsRecordDTO1).isNotEqualTo(attendenceStudentsRecordDTO2);
        attendenceStudentsRecordDTO2.setId(attendenceStudentsRecordDTO1.getId());
        assertThat(attendenceStudentsRecordDTO1).isEqualTo(attendenceStudentsRecordDTO2);
        attendenceStudentsRecordDTO2.setId("id2");
        assertThat(attendenceStudentsRecordDTO1).isNotEqualTo(attendenceStudentsRecordDTO2);
        attendenceStudentsRecordDTO1.setId(null);
        assertThat(attendenceStudentsRecordDTO1).isNotEqualTo(attendenceStudentsRecordDTO2);
    }
}
