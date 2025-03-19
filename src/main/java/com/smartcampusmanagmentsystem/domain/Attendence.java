package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Attendence.
 */
@Document(collection = "attendence")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "attendence")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Attendence implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("created_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdAt;

    @Field("course_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String courseId;

    @Field("course_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String courseName;

    @Field("instructor_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String instructorId;

    @Field("instructor_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String instructorName;

    @Field("module_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String moduleId;

    @Field("module_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String moduleName;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Attendence id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public Attendence createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Attendence courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getCourseName() {
        return this.courseName;
    }

    public Attendence courseName(String courseName) {
        this.setCourseName(courseName);
        return this;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getInstructorId() {
        return this.instructorId;
    }

    public Attendence instructorId(String instructorId) {
        this.setInstructorId(instructorId);
        return this;
    }

    public void setInstructorId(String instructorId) {
        this.instructorId = instructorId;
    }

    public String getInstructorName() {
        return this.instructorName;
    }

    public Attendence instructorName(String instructorName) {
        this.setInstructorName(instructorName);
        return this;
    }

    public void setInstructorName(String instructorName) {
        this.instructorName = instructorName;
    }

    public String getModuleId() {
        return this.moduleId;
    }

    public Attendence moduleId(String moduleId) {
        this.setModuleId(moduleId);
        return this;
    }

    public void setModuleId(String moduleId) {
        this.moduleId = moduleId;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public Attendence moduleName(String moduleName) {
        this.setModuleName(moduleName);
        return this;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Attendence)) {
            return false;
        }
        return getId() != null && getId().equals(((Attendence) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Attendence{" +
            "id=" + getId() +
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
