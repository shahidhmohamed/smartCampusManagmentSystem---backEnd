package com.smartcampusmanagmentsystem.domain;

import java.util.Random;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class AssignmentFileTestSamples {

    private static final Random random = new Random();
    private static final AtomicInteger intCount = new AtomicInteger(random.nextInt() + (2 * Short.MAX_VALUE));

    public static AssignmentFile getAssignmentFileSample1() {
        return new AssignmentFile()
            .id("id1")
            .studentId("studentId1")
            .assignmentId("assignmentId1")
            .name("name1")
            .type("type1")
            .fileSize(1)
            .createdBy("createdBy1")
            .createdAt("createdAt1")
            .modifiedAt("modifiedAt1")
            .mimeType("mimeType1")
            .extension("extension1")
            .feedback("feedback1")
            .gradedBy("gradedBy1")
            .gradedAt("gradedAt1");
    }

    public static AssignmentFile getAssignmentFileSample2() {
        return new AssignmentFile()
            .id("id2")
            .studentId("studentId2")
            .assignmentId("assignmentId2")
            .name("name2")
            .type("type2")
            .fileSize(2)
            .createdBy("createdBy2")
            .createdAt("createdAt2")
            .modifiedAt("modifiedAt2")
            .mimeType("mimeType2")
            .extension("extension2")
            .feedback("feedback2")
            .gradedBy("gradedBy2")
            .gradedAt("gradedAt2");
    }

    public static AssignmentFile getAssignmentFileRandomSampleGenerator() {
        return new AssignmentFile()
            .id(UUID.randomUUID().toString())
            .studentId(UUID.randomUUID().toString())
            .assignmentId(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .type(UUID.randomUUID().toString())
            .fileSize(intCount.incrementAndGet())
            .createdBy(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .modifiedAt(UUID.randomUUID().toString())
            .mimeType(UUID.randomUUID().toString())
            .extension(UUID.randomUUID().toString())
            .feedback(UUID.randomUUID().toString())
            .gradedBy(UUID.randomUUID().toString())
            .gradedAt(UUID.randomUUID().toString());
    }
}
