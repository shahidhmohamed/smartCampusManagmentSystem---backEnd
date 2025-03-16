package com.smartcampusmanagmentsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class FileTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static File getFileSample1() {
        return new File()
            .id("id1")
            .folderId("folderId1")
            .name("name1")
            .type("type1")
            .fileSize(1)
            .createdBy("createdBy1")
            .createdAt("createdAt1")
            .modifiedAt("modifiedAt1")
            .mimeType("mimeType1")
            .extension("extension1");
    }

    public static File getFileSample2() {
        return new File()
            .id("id2")
            .folderId("folderId2")
            .name("name2")
            .type("type2")
            .fileSize(2)
            .createdBy("createdBy2")
            .createdAt("createdAt2")
            .modifiedAt("modifiedAt2")
            .mimeType("mimeType2")
            .extension("extension2");
    }

    public static File getFileRandomSampleGenerator() {
        return new File()
            .id(UUID.randomUUID().toString())
            .folderId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .fileSize(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .modifiedAt(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .extension(UUID.randomUUID().toString());
    }
}
