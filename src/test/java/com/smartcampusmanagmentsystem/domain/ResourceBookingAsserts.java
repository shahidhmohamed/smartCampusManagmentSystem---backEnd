package com.smartcampusmanagmentsystem.domain;

import static org.assertj.core.api.Assertions.assertThat;

public class ResourceBookingAsserts {

    /**
     * Asserts that the entity has all properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResourceBookingAllPropertiesEquals(ResourceBooking expected, ResourceBooking actual) {
        assertResourceBookingAutoGeneratedPropertiesEquals(expected, actual);
        assertResourceBookingAllUpdatablePropertiesEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all updatable properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResourceBookingAllUpdatablePropertiesEquals(ResourceBooking expected, ResourceBooking actual) {
        assertResourceBookingUpdatableFieldsEquals(expected, actual);
        assertResourceBookingUpdatableRelationshipsEquals(expected, actual);
    }

    /**
     * Asserts that the entity has all the auto generated properties (fields/relationships) set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResourceBookingAutoGeneratedPropertiesEquals(ResourceBooking expected, ResourceBooking actual) {
        assertThat(actual)
            .as("Verify ResourceBooking auto generated properties")
            .satisfies(a -> assertThat(a.getId()).as("check id").isEqualTo(expected.getId()));
    }

    /**
     * Asserts that the entity has all the updatable fields set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResourceBookingUpdatableFieldsEquals(ResourceBooking expected, ResourceBooking actual) {
        assertThat(actual)
            .as("Verify ResourceBooking relevant properties")
            .satisfies(a -> assertThat(a.getUserId()).as("check userId").isEqualTo(expected.getUserId()))
            .satisfies(a -> assertThat(a.getStartTime()).as("check startTime").isEqualTo(expected.getStartTime()))
            .satisfies(a -> assertThat(a.getEndTime()).as("check endTime").isEqualTo(expected.getEndTime()))
            .satisfies(a -> assertThat(a.getStatus()).as("check status").isEqualTo(expected.getStatus()))
            .satisfies(a -> assertThat(a.getReason()).as("check reason").isEqualTo(expected.getReason()))
            .satisfies(a -> assertThat(a.getAdminComment()).as("check adminComment").isEqualTo(expected.getAdminComment()));
    }

    /**
     * Asserts that the entity has all the updatable relationships set.
     *
     * @param expected the expected entity
     * @param actual the actual entity
     */
    public static void assertResourceBookingUpdatableRelationshipsEquals(ResourceBooking expected, ResourceBooking actual) {
        assertThat(actual)
            .as("Verify ResourceBooking relationships")
            .satisfies(a -> assertThat(a.getResource()).as("check resource").isEqualTo(expected.getResource()));
    }
}
