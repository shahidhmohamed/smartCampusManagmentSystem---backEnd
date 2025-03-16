package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class AssignmentTestSamples {

    public static Assignment getAssignmentSample1() {
        return new Assignment()
            .id("id1")
            .title("title1")
            .description("description1")
            .courseId("courseId1")
            .moduleId("moduleId1")
            .instructorId("instructorId1")
            .createdBy("createdBy1")
            .createdAt("createdAt1")
            .modifiedAt("modifiedAt1")
            .deadLine("deadLine1");
    }

    public static Assignment getAssignmentSample2() {
        return new Assignment()
            .id("id2")
            .title("title2")
            .description("description2")
            .courseId("courseId2")
            .moduleId("moduleId2")
            .instructorId("instructorId2")
            .createdBy("createdBy2")
            .createdAt("createdAt2")
            .modifiedAt("modifiedAt2")
            .deadLine("deadLine2");
    }

    public static Assignment getAssignmentRandomSampleGenerator() {
        return new Assignment()
            .id(UUID.randomUUID().toString())
            .title(UUID.randomUUID().toString())
            .description(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .moduleId(UUID.randomUUID().toString())
            .instructorId(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .modifiedAt(UUID.randomUUID().toString())
            .deadLine(UUID.randomUUID().toString());
    }
}
