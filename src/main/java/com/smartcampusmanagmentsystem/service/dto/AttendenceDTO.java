package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.Attendence} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendenceDTO implements Serializable {

    private String id;

    private String createdAt;

    private String courseId;

    private String courseName;

    private String instructorId;

    private String instructorName;

    private String moduleId;

    private String moduleName;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return instructorName;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return moduleName;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendenceDTO)) {
            return false;
        }

        AttendenceDTO attendenceDTO = (AttendenceDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, attendenceDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendenceDTO{" +
            "id='" + getId() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", courseName='" + getCourseName() + "'" +
            ", instructorId='" + getInstructorId() + "'" +
            ", instructorName='" + getInstructorName() + "'" +
            ", moduleId='" + getModuleId() + "'" +
            ", moduleName='" + getModuleName() + "'" +
            "}";
    }
}
