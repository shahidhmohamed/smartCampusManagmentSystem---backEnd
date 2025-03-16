package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A ClassSchedule.
 */
@Document(collection = "class_schedule")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "classschedule")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ClassSchedule implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("course_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String courseId;

    @Field("module_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String moduleId;

    @Field("instructor_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String instructorId;

    @Field("schedule_date")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String scheduleDate;

    @Field("schedule_time_from")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String scheduleTimeFrom;

    @Field("schedule_time_to")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String scheduleTimeTo;

    @Field("location")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String location;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public ClassSchedule id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public ClassSchedule courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getModuleId() {
        return this.moduleId;
    }

    public ClassSchedule moduleId(String moduleId) {
        this.setModuleId(moduleId);
        return this;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getInstructorId() {
        return this.instructorId;
    }

    public ClassSchedule instructorId(String instructorId) {
        this.setInstructorId(instructorId);
        return this;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getScheduleDate() {
        return this.scheduleDate;
    }

    public ClassSchedule scheduleDate(String scheduleDate) {
        this.setScheduleDate(scheduleDate);
        return this;
    }

    public void setScheduleDate(String scheduleDate) {
        this.scheduleDate = scheduleDate;
    }

    public String getScheduleTimeFrom() {
        return this.scheduleTimeFrom;
    }

    public ClassSchedule scheduleTimeFrom(String scheduleTimeFrom) {
        this.setScheduleTimeFrom(scheduleTimeFrom);
        return this;
    }

    public void setScheduleTimeFrom(String scheduleTimeFrom) {
        this.scheduleTimeFrom = scheduleTimeFrom;
    }

    public String getScheduleTimeTo() {
        return this.scheduleTimeTo;
    }

    public ClassSchedule scheduleTimeTo(String scheduleTimeTo) {
        this.setScheduleTimeTo(scheduleTimeTo);
        return this;
    }

    public void setScheduleTimeTo(String scheduleTimeTo) {
        this.scheduleTimeTo = scheduleTimeTo;
    }

    public String getLocation() {
        return this.location;
    }

    public ClassSchedule location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ClassSchedule)) {
            return false;
        }
        return getId() != null && getId().equals(((ClassSchedule) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ClassSchedule{" +
            "id=" + getId() +
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
