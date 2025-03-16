package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class ClassScheduleTestSamples {

    public static ClassSchedule getClassScheduleSample1() {
        return new ClassSchedule()
            .id("id1")
            .courseId("courseId1")
            .moduleId("moduleId1")
            .instructorId("instructorId1")
            .scheduleDate("scheduleDate1")
            .scheduleTimeFrom("scheduleTimeFrom1")
            .scheduleTimeTo("scheduleTimeTo1")
            .location("location1");
    }

    public static ClassSchedule getClassScheduleSample2() {
        return new ClassSchedule()
            .id("id2")
            .courseId("courseId2")
            .moduleId("moduleId2")
            .instructorId("instructorId2")
            .scheduleDate("scheduleDate2")
            .scheduleTimeFrom("scheduleTimeFrom2")
            .scheduleTimeTo("scheduleTimeTo2")
            .location("location2");
    }

    public static ClassSchedule getClassScheduleRandomSampleGenerator() {
        return new ClassSchedule()
            .id(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .moduleId(UUID.randomUUID().toString())
            .instructorId(UUID.randomUUID().toString())
            .scheduleDate(UUID.randomUUID().toString())
            .scheduleTimeFrom(UUID.randomUUID().toString())
            .scheduleTimeTo(UUID.randomUUID().toString())
            .location(UUID.randomUUID().toString());
    }
}
