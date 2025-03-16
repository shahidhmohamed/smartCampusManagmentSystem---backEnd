package com.smartcampusmanagmentsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class CampusEventTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static CampusEvent getCampusEventSample1() {
        return new CampusEvent()
            .id("id1")
            .eventName("eventName1")
            .description("description1")
            .eventDate("eventDate1")
            .location("location1")
            .organizerId("organizerId1")
            .eventType("eventType1")
            .capacity(1);
    }

    public static CampusEvent getCampusEventSample2() {
        return new CampusEvent()
            .id("id2")
            .eventName("eventName2")
            .description("description2")
            .eventDate("eventDate2")
            .location("location2")
            .organizerId("organizerId2")
            .eventType("eventType2")
            .capacity(2);
    }

    public static CampusEvent getCampusEventRandomSampleGenerator() {
        return new CampusEvent()
            .id(UUID.randomUUID().toString())
            .eventName(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .eventDate(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .organizerId(UUID.randomUUID().toString())
            .eventType(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet());
    }
}
