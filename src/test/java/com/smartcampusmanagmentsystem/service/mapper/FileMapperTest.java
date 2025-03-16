package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.FileAsserts.*;
import static com.smartcampusmanagmentsystem.domain.FileTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FileMapperTest {

    private FileMapper fileMapper;

    @BeforeEach
    void setUp() {
        fileMapper = new FileMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFileSample1();
        var actual = fileMapper.toEntity(fileMapper.toDto(expected));
        assertFileAllPropertiesEquals(expected, actual);
    }
}
