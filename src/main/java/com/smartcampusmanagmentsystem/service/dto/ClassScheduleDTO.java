package com.smartcampusmanagmentsystem.service.dto;

import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.ClassSchedule} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassScheduleDTO implements Serializable {

    private String id;

    private String courseId;

    private String moduleId;

    private String instructorId;

    private String scheduleDate;

    private String scheduleTimeFrom;

    private String scheduleTimeTo;

    private String location;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return courseId;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getModuleId() {
        return moduleId;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getInstructorId() {
        return instructorId;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getScheduleDate() {
        return scheduleDate;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTimeFrom() {
        return scheduleTimeFrom;
    }

    public void setScheduleTimeFrom(String scheduleTimeFrom) {
        this.scheduleTimeFrom = scheduleTimeFrom;
    }

    public String getScheduleTimeTo() {
        return scheduleTimeTo;
    }

    public void setScheduleTimeTo(String scheduleTimeTo) {
        this.scheduleTimeTo = scheduleTimeTo;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassScheduleDTO)) {
            return false;
        }

        ClassScheduleDTO classScheduleDTO = (ClassScheduleDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, classScheduleDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassScheduleDTO{" +
            "id='" + getId() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", moduleId='" + getModuleId() + "'" +
            ", instructorId='" + getInstructorId() + "'" +
            ", scheduleDate='" + getScheduleDate() + "'" +
            ", scheduleTimeFrom='" + getScheduleTimeFrom() + "'" +
            ", scheduleTimeTo='" + getScheduleTimeTo() + "'" +
            ", location='" + getLocation() + "'" +
            "}";
    }
}
