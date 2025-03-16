package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class CourseRegistrationTestSamples {

    public static CourseRegistration getCourseRegistrationSample1() {
        return new CourseRegistration()
            .id("id1")
            .studentId("studentId1")
            .courseId("courseId1")
            .courseCode("courseCode1")
            .duration("duration1")
            .registrationDate("registrationDate1");
    }

    public static CourseRegistration getCourseRegistrationSample2() {
        return new CourseRegistration()
            .id("id2")
            .studentId("studentId2")
            .courseId("courseId2")
            .courseCode("courseCode2")
            .duration("duration2")
            .registrationDate("registrationDate2");
    }

    public static CourseRegistration getCourseRegistrationRandomSampleGenerator() {
        return new CourseRegistration()
            .id(UUID.randomUUID().toString())
            .studentId(UUID.randomUUID().toString())
            .courseId(UUID.randomUUID().toString())
            .courseCode(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString())
            .registrationDate(UUID.randomUUID().toString());
    }
}
