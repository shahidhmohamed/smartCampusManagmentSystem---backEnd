package com.smartcampusmanagmentsystem.service.dto;

import com.smartcampusmanagmentsystem.domain.enumeration.EventStatus;
import java.io.Serializable;
import java.util.Objects;

/**
 * A DTO for the {@link com.smartcampusmanagmentsystem.domain.CampusEvent} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CampusEventDTO implements Serializable {

    private String id;

    private String eventName;

    private String description;

    private String eventDate;

    private String location;

    private String organizerId;

    private String eventType;

    private Integer capacity;

    private EventStatus status;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return eventName;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDate() {
        return eventDate;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizerId() {
        return organizerId;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public EventStatus getStatus() {
        return status;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampusEventDTO)) {
            return false;
        }

        CampusEventDTO campusEventDTO = (CampusEventDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, campusEventDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampusEventDTO{" +
            "id='" + getId() + "'" +
            ", eventName='" + getEventName() + "'" +
            ", description='" + getDescription() + "'" +
            ", eventDate='" + getEventDate() + "'" +
            ", location='" + getLocation() + "'" +
            ", organizerId='" + getOrganizerId() + "'" +
            ", eventType='" + getEventType() + "'" +
            ", capacity=" + getCapacity() +
            ", status='" + getStatus() + "'" +
            "}";
    }
}
