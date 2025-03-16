package com.smartcampusmanagmentsystem.service.mapper;

import static com.smartcampusmanagmentsystem.domain.FolderAsserts.*;
import static com.smartcampusmanagmentsystem.domain.FolderTestSamples.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FolderMapperTest {

    private FolderMapper folderMapper;

    @BeforeEach
    void setUp() {
        folderMapper = new FolderMapperImpl();
    }

    @Test
    void shouldConvertToDtoAndBack() {
        var expected = getFolderSample1();
        var actual = folderMapper.toEntity(folderMapper.toDto(expected));
        assertFolderAllPropertiesEquals(expected, actual);
    }
}
