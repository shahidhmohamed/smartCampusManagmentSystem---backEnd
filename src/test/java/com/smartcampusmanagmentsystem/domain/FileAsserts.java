package com.smartcampusmanagmentsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class FileAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAllPropertiesEquals(File expected, File actual) {
        assertFileAutoGeneratedPropertiesEquals(expected, actual);
        assertFileAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAllUpdatablePropertiesEquals(File expected, File actual) {
        assertFileUpdatableFieldsEquals(expected, actual);
        assertFileUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileAutoGeneratedPropertiesEquals(File expected, File actual) {
        assertThat(actual)
            .as("Verify File auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileUpdatableFieldsEquals(File expected, File actual) {
        assertThat(actual)
            .as("Verify File relevant properties")
            .satisfies(a -> assertThat(a.getFolderId()).as("check folderId").isEqualTo(expected.getFolderId()))
            .satisfies(a -> assertThat(a.getName()).as("check name").isEqualTo(expected.getName()))
            .satisfies(a -> assertThat(a.getType()).as("check type").isEqualTo(expected.getType()))
            .satisfies(a -> assertThat(a.getFileSize()).as("check fileSize").isEqualTo(expected.getFileSize()))
            .satisfies(a -> assertThat(a.getCreatedBy()).as("check createdBy").isEqualTo(expected.getCreatedBy()))
            .satisfies(a -> assertThat(a.getCreatedAt()).as("check createdAt").isEqualTo(expected.getCreatedAt()))
            .satisfies(a -> assertThat(a.getModifiedAt()).as("check modifiedAt").isEqualTo(expected.getModifiedAt()))
            .satisfies(a -> assertThat(a.getMimeType()).as("check mimeType").isEqualTo(expected.getMimeType()))
            .satisfies(a -> assertThat(a.getExtension()).as("check extension").isEqualTo(expected.getExtension()))
            .satisfies(a -> assertThat(a.getBinaryData()).as("check binaryData").isEqualTo(expected.getBinaryData()))
            .satisfies(a ->
                assertThat(a.getBinaryDataContentType()).as("check binaryData contenty type").isEqualTo(expected.getBinaryDataContentType())
            );
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertFileUpdatableRelationshipsEquals(File expected, File actual) {
        // empty method
    }
}
