package com.smartcampusmanagmentsystem.domain;

import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AttendenceStudentsRecord.
 */
@Document(collection = "attendence_students_record")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "attendencestudentsrecord")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AttendenceStudentsRecord implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("attendence_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String attendenceId;

    @Field("student_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String studentId;

    @Field("student_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String studentName;

    @Field("is_present")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Boolean)
    private Boolean isPresent;

    @Field("created_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdAt;

    @Field("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AttendenceStudentsRecord id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAttendenceId() {
        return this.attendenceId;
    }

    public AttendenceStudentsRecord attendenceId(String attendenceId) {
        this.setAttendenceId(attendenceId);
        return this;
    }

    public void setAttendenceId(String attendenceId) {
        this.attendenceId = attendenceId;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public AttendenceStudentsRecord studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getStudentName() {
        return this.studentName;
    }

    public AttendenceStudentsRecord studentName(String studentName) {
        this.setStudentName(studentName);
        return this;
    }

    public void setStudentName(String studentName) {
        this.studentName = studentName;
    }

    public Boolean getIsPresent() {
        return this.isPresent;
    }

    public AttendenceStudentsRecord isPresent(Boolean isPresent) {
        this.setIsPresent(isPresent);
        return this;
    }

    public void setIsPresent(Boolean isPresent) {
        this.isPresent = isPresent;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public AttendenceStudentsRecord createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AttendenceStudentsRecord createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AttendenceStudentsRecord)) {
            return false;
        }
        return getId() != null && getId().equals(((AttendenceStudentsRecord) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AttendenceStudentsRecord{" +
            "id=" + getId() +
            ", attendenceId='" + getAttendenceId() + "'" +
            ", studentId='" + getStudentId() + "'" +
            ", studentName='" + getStudentName() + "'" +
            ", isPresent='" + getIsPresent() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", createdBy='" + getCreatedBy() + "'" +
            "}";
    }
}
