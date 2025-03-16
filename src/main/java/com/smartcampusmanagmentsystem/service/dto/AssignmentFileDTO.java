package com.smartcampusmanagmentsystem.service.dto;

import com.smartcampusmanagmentsystem.domain.enumeration.MarkingStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.AssignmentFile} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class AssignmentFileDTO implements Serializable {

    private String id;

    private String studentId;

    private String assignmentId;

    private String name;

    private String type;

    private Integer fileSize;

    private String createdBy;

    private String createdAt;

    private String modifiedAt;

    private String mimeType;

    private String extension;

    private byte[] binaryData;

    private String binaryDataContentType;

    private MarkingStatus markingStatus;

    private Float grade;

    private String feedback;

    private String gradedBy;

    private String gradedAt;

    private Boolean isSubmitted;

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

    public String getAssignmentId() {
        return assignmentId;
    }

    public void setAssignmentId(String assignmentId) {
        this.assignmentId = assignmentId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Integer getFileSize() {
        return fileSize;
    }

    public void setFileSize(Integer fileSize) {
        this.fileSize = fileSize;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }

    public String getModifiedAt() {
        return modifiedAt;
    }

    public void setModifiedAt(String modifiedAt) {
        this.modifiedAt = modifiedAt;
    }

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public String getExtension() {
        return extension;
    }

    public void setExtension(String extension) {
        this.extension = extension;
    }

    public byte[] getBinaryData() {
        return binaryData;
    }

    public void setBinaryData(byte[] binaryData) {
        this.binaryData = binaryData;
    }

    public String getBinaryDataContentType() {
        return binaryDataContentType;
    }

    public void setBinaryDataContentType(String binaryDataContentType) {
        this.binaryDataContentType = binaryDataContentType;
    }

    public MarkingStatus getMarkingStatus() {
        return markingStatus;
    }

    public void setMarkingStatus(MarkingStatus markingStatus) {
        this.markingStatus = markingStatus;
    }

    public Float getGrade() {
        return grade;
    }

    public void setGrade(Float grade) {
        this.grade = grade;
    }

    public String getFeedback() {
        return feedback;
    }

    public void setFeedback(String feedback) {
        this.feedback = feedback;
    }

    public String getGradedBy() {
        return gradedBy;
    }

    public void setGradedBy(String gradedBy) {
        this.gradedBy = gradedBy;
    }

    public String getGradedAt() {
        return gradedAt;
    }

    public void setGradedAt(String gradedAt) {
        this.gradedAt = gradedAt;
    }

    public Boolean getIsSubmitted() {
        return isSubmitted;
    }

    public void setIsSubmitted(Boolean isSubmitted) {
        this.isSubmitted = isSubmitted;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof AssignmentFileDTO)) {
            return false;
        }

        AssignmentFileDTO assignmentFileDTO = (AssignmentFileDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, assignmentFileDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "AssignmentFileDTO{" +
            "id='" + getId() + "'" +
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
            ", markingStatus='" + getMarkingStatus() + "'" +
            ", grade=" + getGrade() +
            ", feedback='" + getFeedback() + "'" +
            ", gradedBy='" + getGradedBy() + "'" +
            ", gradedAt='" + getGradedAt() + "'" +
            ", isSubmitted='" + getIsSubmitted() + "'" +
            "}";
    }
}
