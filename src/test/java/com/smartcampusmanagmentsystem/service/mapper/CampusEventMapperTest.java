package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.CampusEventAsserts.*;
import static com.smartcampusmanagmentsystem.domain.CampusEventTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class CampusEventMapperTest {

    private CampusEventMapper campusEventMapper;

    @BeforeEach
    void setUp() {
        campusEventMapper = new CampusEventMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getCampusEventSample1();
        var actual = campusEventMapper.toEntity(campusEventMapper.toDto(expected));
        assertCampusEventAllPropertiesEquals(expected, actual);
    }
}
