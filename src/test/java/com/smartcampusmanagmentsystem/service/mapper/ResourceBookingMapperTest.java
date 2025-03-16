package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.ResourceBookingAsserts.*;
import static com.smartcampusmanagmentsystem.domain.ResourceBookingTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ResourceBookingMapperTest {

    private ResourceBookingMapper resourceBookingMapper;

    @BeforeEach
    void setUp() {
        resourceBookingMapper = new ResourceBookingMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getResourceBookingSample1();
        var actual = resourceBookingMapper.toEntity(resourceBookingMapper.toDto(expected));
        assertResourceBookingAllPropertiesEquals(expected, actual);
    }
}
