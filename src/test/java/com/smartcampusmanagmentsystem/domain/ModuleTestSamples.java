package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class ModuleTestSamples {

    public static Module getModuleSample1() {
        return new Module()
            .id("id1")
            .moduleName("moduleName1")
            .moduleCode("moduleCode1")
            .courseId("courseId1")
            .semester("semester1")
            .lecturerId("lecturerId1")
            .duration("duration1");
    }

    public static Module getModuleSample2() {
        return new Module()
            .id("id2")
            .moduleName("moduleName2")
            .moduleCode("moduleCode2")
            .courseId("courseId2")
            .semester("semester2")
            .lecturerId("lecturerId2")
            .duration("duration2");
    }

    public static Module getModuleRandomSampleGenerator() {
        return new Module()
            .id(UUID.randomUUID().toString())
            .moduleName(UUID.randomUUID().toString())
            .moduleCode(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .semester(UUID.randomUUID().toString())
            .lecturerId(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString());
    }
}
