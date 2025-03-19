package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecordAsserts.*;
import static com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecordTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AttendenceStudentsRecordMapperTest {

    private AttendenceStudentsRecordMapper attendenceStudentsRecordMapper;

    @BeforeEach
    void setUp() {
        attendenceStudentsRecordMapper = new AttendenceStudentsRecordMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAttendenceStudentsRecordSample1();
        var actual = attendenceStudentsRecordMapper.toEntity(attendenceStudentsRecordMapper.toDto(expected));
        assertAttendenceStudentsRecordAllPropertiesEquals(expected, actual);
    }
}
