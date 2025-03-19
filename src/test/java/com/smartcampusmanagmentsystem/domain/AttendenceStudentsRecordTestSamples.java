package com.smartcampusmanagmentsystem.domain;

import java.util.UUID;

public class AttendenceStudentsRecordTestSamples {

    public static AttendenceStudentsRecord getAttendenceStudentsRecordSample1() {
        return new AttendenceStudentsRecord()
            .id("id1")
            .attendenceId("attendenceId1")
            .studentId("studentId1")
            .studentName("studentName1")
            .createdAt("createdAt1")
            .createdBy("createdBy1");
    }

    public static AttendenceStudentsRecord getAttendenceStudentsRecordSample2() {
        return new AttendenceStudentsRecord()
            .id("id2")
            .attendenceId("attendenceId2")
            .studentId("studentId2")
            .studentName("studentName2")
            .createdAt("createdAt2")
            .createdBy("createdBy2");
    }

    public static AttendenceStudentsRecord getAttendenceStudentsRecordRandomSampleGenerator() {
        return new AttendenceStudentsRecord()
            .id(UUID.randomUUID().toString())
            .attendenceId(UUID.randomUUID().toString())
            .studentId(UUID.randomUUID().toString())
            .studentName(UUID.randomUUID().toString())
            .createdAt(UUID.randomUUID().toString())
            .createdBy(UUID.randomUUID().toString());
    }
}
