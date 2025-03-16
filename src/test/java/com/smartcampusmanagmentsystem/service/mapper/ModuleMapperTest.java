package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.ModuleAsserts.*;
import static com.smartcampusmanagmentsystem.domain.ModuleTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class ModuleMapperTest {

    private ModuleMapper moduleMapper;

    @BeforeEach
    void setUp() {
        moduleMapper = new ModuleMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getModuleSample1();
        var actual = moduleMapper.toEntity(moduleMapper.toDto(expected));
        assertModuleAllPropertiesEquals(expected, actual);
    }
}
