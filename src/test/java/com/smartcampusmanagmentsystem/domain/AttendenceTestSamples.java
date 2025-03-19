package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class AttendenceTestSamples {

    public static Attendence getAttendenceSample1() {
        return new Attendence()
            .id("id1")
            .createdAt("createdAt1")
            .courseId("courseId1")
            .courseName("courseName1")
            .instructorId("instructorId1")
            .instructorName("instructorName1")
            .moduleId("moduleId1")
            .moduleName("moduleName1");
    }

    public static Attendence getAttendenceSample2() {
        return new Attendence()
            .id("id2")
            .createdAt("createdAt2")
            .courseId("courseId2")
            .courseName("courseName2")
            .instructorId("instructorId2")
            .instructorName("instructorName2")
            .moduleId("moduleId2")
            .moduleName("moduleName2");
    }

    public static Attendence getAttendenceRandomSampleGenerator() {
        return new Attendence()
            .id(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .courseName(UUID.randomUUID().toString())
            .instructorId(UUID.randomUUID().toString())
            .instructorName(UUID.randomUUID().toString())
            .moduleId(UUID.randomUUID().toString())
            .moduleName(UUID.randomUUID().toString());
    }
}
