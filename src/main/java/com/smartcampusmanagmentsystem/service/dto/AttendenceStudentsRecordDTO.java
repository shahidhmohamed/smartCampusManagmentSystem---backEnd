package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.AttendenceStudentsRecord} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendenceStudentsRecordDTO implements Serializable {

    private String id;

    private String attendenceId;

    private String studentId;

    private String studentName;

    private Boolean isPresent;

    private String createdAt;

    private String createdBy;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendenceId() {
        return attendenceId;
    }

    public void setAttendenceId(String attendenceId) {
        this.attendenceId = attendenceId;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return studentName;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Boolean getIsPresent() {
        return isPresent;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendenceStudentsRecordDTO)) {
            return false;
        }

        AttendenceStudentsRecordDTO attendenceStudentsRecordDTO = (AttendenceStudentsRecordDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attendenceStudentsRecordDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendenceStudentsRecordDTO{" +
            "id='" + getId() + "'" +
            ", attendenceId='" + getAttendenceId() + "'" +
            ", studentId='" + getStudentId() + "'" +
            ", studentName='" + getStudentName() + "'" +
            ", isPresent='" + getIsPresent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
