package com.smartcampusmanagmentsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class ResourceTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static Resource getResourceSample1() {
        return new Resource().id("id1").resourceId("resourceId1").name("name1").location("location1").capacity(1);
    }

    public static Resource getResourceSample2() {
        return new Resource().id("id2").resourceId("resourceId2").name("name2").location("location2").capacity(2);
    }

    public static Resource getResourceRandomSampleGenerator() {
        return new Resource()
            .id(UUID.randomUUID().toString())
            .resourceId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString())
            .capacity(intCount.incrementAndGet());
    }
}
