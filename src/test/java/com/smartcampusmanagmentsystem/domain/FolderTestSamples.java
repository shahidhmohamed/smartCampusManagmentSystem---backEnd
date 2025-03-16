package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class FolderTestSamples {

    public static Folder getFolderSample1() {
        return new Folder()
            .id("id1")
            .name("name1")
            .contents("contents1")
            .courseId("courseId1")
            .course("course1")
            .semester("semester1")
            .createdBy("createdBy1")
            .createdAt("createdAt1")
            .modifiedAt("modifiedAt1")
            .parentId("parentId1");
    }

    public static Folder getFolderSample2() {
        return new Folder()
            .id("id2")
            .name("name2")
            .contents("contents2")
            .courseId("courseId2")
            .course("course2")
            .semester("semester2")
            .createdBy("createdBy2")
            .createdAt("createdAt2")
            .modifiedAt("modifiedAt2")
            .parentId("parentId2");
    }

    public static Folder getFolderRandomSampleGenerator() {
        return new Folder()
            .id(UUID.randomUUID().toString())
            .name(UUID.randomUUID().toString())
            .contents(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .course(UUID.randomUUID().toString())
            .semester(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .modifiedAt(UUID.randomUUID().toString())
            .parentId(UUID.randomUUID().toString());
    }
}
