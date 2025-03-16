package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A Module.
 */
@Document(collection = "module")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "module")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class Module implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("module_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String moduleName;

    @Field("module_code")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String moduleCode;

    @Field("course_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String courseId;

    @Field("semester")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String semester;

    @Field("lecturer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String lecturerId;

    @Field("duration")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String duration;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public Module id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getModuleName() {
        return this.moduleName;
    }

    public Module moduleName(String moduleName) {
        this.setModuleName(moduleName);
        return this;
    }

    public void setModuleName(String moduleName) {
        this.moduleName = moduleName;
    }

    public String getModuleCode() {
        return this.moduleCode;
    }

    public Module moduleCode(String moduleCode) {
        this.setModuleCode(moduleCode);
        return this;
    }

    public void setModuleCode(String moduleCode) {
        this.moduleCode = moduleCode;
    }

    public String getCourseId() {
        return this.courseId;
    }

    public Module courseId(String courseId) {
        this.setCourseId(courseId);
        return this;
    }

    public void setCourseId(String courseId) {
        this.courseId = courseId;
    }

    public String getSemester() {
        return this.semester;
    }

    public Module semester(String semester) {
        this.setSemester(semester);
        return this;
    }

    public void setSemester(String semester) {
        this.semester = semester;
    }

    public String getLecturerId() {
        return this.lecturerId;
    }

    public Module lecturerId(String lecturerId) {
        this.setLecturerId(lecturerId);
        return this;
    }

    public void setLecturerId(String lecturerId) {
        this.lecturerId = lecturerId;
    }

    public String getDuration() {
        return this.duration;
    }

    public Module duration(String duration) {
        this.setDuration(duration);
        return this;
    }

    public void setDuration(String duration) {
        this.duration = duration;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof Module)) {
            return false;
        }
        return getId() != null && getId().equals(((Module) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "Module{" +
            "id=" + getId() +
            ", moduleName='" + getModuleName() + "'" +
            ", moduleCode='" + getModuleCode() + "'" +
            ", courseId='" + getCourseId() + "'" +
            ", semester='" + getSemester() + "'" +
            ", lecturerId='" + getLecturerId() + "'" +
            ", duration='" + getDuration() + "'" +
            "}";
    }
}
