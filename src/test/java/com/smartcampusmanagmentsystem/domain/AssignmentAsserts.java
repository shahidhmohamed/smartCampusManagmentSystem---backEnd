package com.smartcampusmanagmentsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class AssignmentAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllPropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentAutoGeneratedPropertiesEquals(expected, actual);
        assertAssignmentAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAllUpdatablePropertiesEquals(Assignment expected, Assignment actual) {
        assertAssignmentUpdatableFieldsEquals(expected, actual);
        assertAssignmentUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentAutoGeneratedPropertiesEquals(Assignment expected, Assignment actual) {
        assertThat(actual)
            .as("Verify Assignment auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentUpdatableFieldsEquals(Assignment expected, Assignment actual) {
        assertThat(actual)
            .as("Verify Assignment relevant properties")
            .satisfies(a -> assertThat(a.getTitle()).as("check title").isEqualTo(expected.getTitle()))
            .satisfies(a -> assertThat(a.getDescription()).as("check description").isEqualTo(expected.getDescription()))
            .satisfies(a -> assertThat(a.getCourseId()).as("check courseId").isEqualTo(expected.getCourseId()))
            .satisfies(a -> assertThat(a.getModuleId()).as("check moduleId").isEqualTo(expected.getModuleId()))
            .satisfies(a -> assertThat(a.getInstructorId()).as("check instructorId").isEqualTo(expected.getInstructorId()))
            .satisfies(a -> assertThat(a.getCreatedBy()).as("check createdBy").isEqualTo(expected.getCreatedBy()))
            .satisfies(a -> assertThat(a.getCreatedAt()).as("check createdAt").isEqualTo(expected.getCreatedAt()))
            .satisfies(a -> assertThat(a.getModifiedAt()).as("check modifiedAt").isEqualTo(expected.getModifiedAt()))
            .satisfies(a -> assertThat(a.getDeadLine()).as("check deadLine").isEqualTo(expected.getDeadLine()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertAssignmentUpdatableRelationshipsEquals(Assignment expected, Assignment actual) {
        // empty method
    }
}
