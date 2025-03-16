package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.CourseRegistrationAsserts.*;
import static com.smartcampusmanagmentsystem.domain.CourseRegistrationTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CourseRegistrationMapperTest {

    private CourseRegistrationMapper courseRegistrationMapper;

    @BeforeEach
    void setUp() {
        courseRegistrationMapper = new CourseRegistrationMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCourseRegistrationSample1();
        var actual = courseRegistrationMapper.toEntity(courseRegistrationMapper.toDto(expected));
        assertCourseRegistrationAllPropertiesEquals(expected, actual);
    }
}
