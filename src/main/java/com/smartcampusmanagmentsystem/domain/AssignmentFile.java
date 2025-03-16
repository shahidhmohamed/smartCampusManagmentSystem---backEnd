package com.smartcampusmanagmentsystem.domain;

import com.smartcampusmanagmentsystem.domain.enumeration.MarkingStatus;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A AssignmentFile.
 */
@Document(collection = "assignment_file")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "assignmentfile")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignmentFile implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("student_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String studentId;

    @Field("assignment_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String assignmentId;

    @Field("name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String name;

    @Field("type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String type;

    @Field("file_size")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer fileSize;

    @Field("created_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdBy;

    @Field("created_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String createdAt;

    @Field("modified_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String modifiedAt;

    @Field("mime_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String mimeType;

    @Field("extension")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String extension;

    @Field("binary_data")
    private byte[] binaryData;

    @Field("binary_data_content_type")
    private String binaryDataContentType;

    @Field("marking_status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private MarkingStatus markingStatus;

    @Field("grade")
    private Float grade;

    @Field("feedback")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String feedback;

    @Field("graded_by")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gradedBy;

    @Field("graded_at")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String gradedAt;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public AssignmentFile id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStudentId() {
        return this.studentId;
    }

    public AssignmentFile studentId(String studentId) {
        this.setStudentId(studentId);
        return this;
    }

    public void setStudentId(String studentId) {
        this.studentId = studentId;
    }

    public String getAssignmentId() {
        return this.assignmentId;
    }

    public AssignmentFile assignmentId(String assignmentId) {
        this.setAssignmentId(assignmentId);
        return this;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getName() {
        return this.name;
    }

    public AssignmentFile name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return this.type;
    }

    public AssignmentFile type(String type) {
        this.setType(type);
        return this;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFileSize() {
        return this.fileSize;
    }

    public AssignmentFile fileSize(Integer fileSize) {
        this.setFileSize(fileSize);
        return this;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreatedBy() {
        return this.createdBy;
    }

    public AssignmentFile createdBy(String createdBy) {
        this.setCreatedBy(createdBy);
        return this;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return this.createdAt;
    }

    public AssignmentFile createdAt(String createdAt) {
        this.setCreatedAt(createdAt);
        return this;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return this.modifiedAt;
    }

    public AssignmentFile modifiedAt(String modifiedAt) {
        this.setModifiedAt(modifiedAt);
        return this;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getMimeType() {
        return this.mimeType;
    }

    public AssignmentFile mimeType(String mimeType) {
        this.setMimeType(mimeType);
        return this;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return this.extension;
    }

    public AssignmentFile extension(String extension) {
        this.setExtension(extension);
        return this;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getBinaryData() {
        return this.binaryData;
    }

    public AssignmentFile binaryData(byte[] binaryData) {
        this.setBinaryData(binaryData);
        return this;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getBinaryDataContentType() {
        return this.binaryDataContentType;
    }

    public AssignmentFile binaryDataContentType(String binaryDataContentType) {
        this.binaryDataContentType = binaryDataContentType;
        return this;
    }

    public void setBinaryDataContentType(String binaryDataContentType) {
        this.binaryDataContentType = binaryDataContentType;
    }

    public MarkingStatus getMarkingStatus() {
        return this.markingStatus;
    }

    public AssignmentFile markingStatus(MarkingStatus markingStatus) {
        this.setMarkingStatus(markingStatus);
        return this;
    }

    public void setMarkingStatus(MarkingStatus markingStatus) {
        this.markingStatus = markingStatus;
    }

    public Float getGrade() {
        return this.grade;
    }

    public AssignmentFile grade(Float grade) {
        this.setGrade(grade);
        return this;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return this.feedback;
    }

    public AssignmentFile feedback(String feedback) {
        this.setFeedback(feedback);
        return this;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGradedBy() {
        return this.gradedBy;
    }

    public AssignmentFile gradedBy(String gradedBy) {
        this.setGradedBy(gradedBy);
        return this;
    }

    public void setGradedBy(String gradedBy) {
        this.gradedBy = gradedBy;
    }

    public String getGradedAt() {
        return this.gradedAt;
    }

    public AssignmentFile gradedAt(String gradedAt) {
        this.setGradedAt(gradedAt);
        return this;
    }

    public void setGradedAt(String gradedAt) {
        this.gradedAt = gradedAt;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentFile)) {
            return false;
        }
        return getId() != null && getId().equals(((AssignmentFile) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentFile{" +
            "id=" + getId() +
            ", studentId='" + getStudentId() + "'" +
            ", assignmentId='" + getAssignmentId() + "'" +
            ", name='" + getName() + "'" +
            ", type='" + getType() + "'" +
            ", fileSize=" + getFileSize() +
            ", createdBy='" + getCreatedBy() + "'" +
            ", createdAt='" + getCreatedAt() + "'" +
            ", modifiedAt='" + getModifiedAt() + "'" +
            ", mimeType='" + getMimeType() + "'" +
            ", extension='" + getExtension() + "'" +
            ", binaryData='" + getBinaryData() + "'" +
            ", binaryDataContentType='" + getBinaryDataContentType() + "'" +
            ", markingStatus='" + getMarkingStatus() + "'" +
            ", grade=" + getGrade() +
            ", feedback='" + getFeedback() + "'" +
            ", gradedBy='" + getGradedBy() + "'" +
            ", gradedAt='" + getGradedAt() + "'" +
            "}";
    }
}
