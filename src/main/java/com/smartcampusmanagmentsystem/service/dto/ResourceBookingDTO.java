package com.smartcampusmanagmentsystem.service.dto;

import com.smartcampusmanagmentsystem.domain.enumeration.Status;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.ResourceBooking} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class ResourceBookingDTO implements Serializable {

    private String id;

    private String userId;

    private String startTime;

    private String endTime;

    private Status status;

    private String reason;

    private String adminComment;

    private ResourceDTO resource;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getAdminComment() {
        return adminComment;
    }

    public void setAdminComment(String adminComment) {
        this.adminComment = adminComment;
    }

    public ResourceDTO getResource() {
        return resource;
    }

    public void setResource(ResourceDTO resource) {
        this.resource = resource;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof ResourceBookingDTO)) {
            return false;
        }

        ResourceBookingDTO resourceBookingDTO = (ResourceBookingDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, resourceBookingDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "ResourceBookingDTO{" +
            "id='" + getId() + "'" +
            ", userId='" + getUserId() + "'" +
            ", startTime='" + getStartTime() + "'" +
            ", endTime='" + getEndTime() + "'" +
            ", status='" + getStatus() + "'" +
            ", reason='" + getReason() + "'" +
            ", adminComment='" + getAdminComment() + "'" +
            ", resource=" + getResource() +
            "}";
    }
}
