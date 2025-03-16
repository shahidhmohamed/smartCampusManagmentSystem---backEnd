package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.AssignmentFileAsserts.*;
import static com.smartcampusmanagmentsystem.domain.AssignmentFileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class AssignmentFileMapperTest {

    private AssignmentFileMapper assignmentFileMapper;

    @BeforeEach
    void setUp() {
        assignmentFileMapper = new AssignmentFileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getAssignmentFileSample1();
        var actual = assignmentFileMapper.toEntity(assignmentFileMapper.toDto(expected));
        assertAssignmentFileAllPropertiesEquals(expected, actual);
    }
}
