package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class CourseTestSamples {

    public static Course getCourseSample1() {
        return new Course().id("id1").courseName("courseName1").courseCode("courseCode1").department("department1").duration("duration1");
    }

    public static Course getCourseSample2() {
        return new Course().id("id2").courseName("courseName2").courseCode("courseCode2").department("department2").duration("duration2");
    }

    public static Course getCourseRandomSampleGenerator() {
        return new Course()
            .id(UUID.randomUUID().toString())
            .courseName(UUID.randomUUID().toString())
            .courseCode(UUID.randomUUID().toString())
            .department(UUID.randomUUID().toString())
            .duration(UUID.randomUUID().toString());
    }
}
