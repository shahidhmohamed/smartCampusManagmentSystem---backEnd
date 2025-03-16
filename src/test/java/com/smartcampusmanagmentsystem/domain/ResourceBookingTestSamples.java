package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class ResourceBookingTestSamples {

    public static ResourceBooking getResourceBookingSample1() {
        return new ResourceBooking()
            .id("id1")
            .userId("userId1")
            .startTime("startTime1")
            .endTime("endTime1")
            .reason("reason1")
            .adminComment("adminComment1");
    }

    public static ResourceBooking getResourceBookingSample2() {
        return new ResourceBooking()
            .id("id2")
            .userId("userId2")
            .startTime("startTime2")
            .endTime("endTime2")
            .reason("reason2")
            .adminComment("adminComment2");
    }

    public static ResourceBooking getResourceBookingRandomSampleGenerator() {
        return new ResourceBooking()
            .id(UUID.randomUUID().toString())
            .userId(UUID.randomUUID().toString())
            .startTime(UUID.randomUUID().toString())
            .endTime(UUID.randomUUID().toString())
            .reason(UUID.randomUUID().toString())
            .adminComment(UUID.randomUUID().toString());
    }
}
