package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.CourseRegistration} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CourseRegistrationDTO implements Serializable {

    private String id;

    private String studentId;

    private String courseId;

    private String courseCode;

    private String duration;

    private String registrationDate;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return studentId;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseCode() {
        return courseCode;
    }

    public void setCourseCode(String courseCode) {
        this.courseCode = courseCode;
    }

    public String getDuration() {
        return duration;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    public String getRegistrationDate() {
        return registrationDate;
    }

    public void setRegistrationDate(String registrationDate) {
        this.registrationDate = registrationDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CourseRegistrationDTO)) {
            return false;
        }

        CourseRegistrationDTO courseRegistrationDTO = (CourseRegistrationDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, courseRegistrationDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CourseRegistrationDTO{" +
            "id='" + getId() + "'" +
            ", studentId='" + getStudentId() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", courseCode='" + getCourseCode() + "'" +
            ", duration='" + getDuration() + "'" +
            ", registrationDate='" + getRegistrationDate() + "'" +
            "}";
    }
}
