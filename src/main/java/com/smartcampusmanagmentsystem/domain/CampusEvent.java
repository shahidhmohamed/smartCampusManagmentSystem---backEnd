package com.smartcampusmanagmentsystem.domain;

import com.smartcampusmanagmentsystem.domain.enumeration.EventStatus;
import java.io.Serializable;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * A CampusEvent.
 */
@Document(collection = "campus_event")
@org.springframework.data.elasticsearch.annotations.Document(indexName = "campusevent")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class CampusEvent implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("event_name")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String eventName;

    @Field("description")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String description;

    @Field("event_date")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String eventDate;

    @Field("location")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String location;

    @Field("organizer_id")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String organizerId;

    @Field("event_type")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Text)
    private String eventType;

    @Field("capacity")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Integer)
    private Integer capacity;

    @Field("status")
    @org.springframework.data.elasticsearch.annotations.Field(type = org.springframework.data.elasticsearch.annotations.FieldType.Keyword)
    private EventStatus status;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public String getId() {
        return this.id;
    }

    public CampusEvent id(String id) {
        this.setId(id);
        return this;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEventName() {
        return this.eventName;
    }

    public CampusEvent eventName(String eventName) {
        this.setEventName(eventName);
        return this;
    }

    public void setEventName(String eventName) {
        this.eventName = eventName;
    }

    public String getDescription() {
        return this.description;
    }

    public CampusEvent description(String description) {
        this.setDescription(description);
        return this;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getEventDate() {
        return this.eventDate;
    }

    public CampusEvent eventDate(String eventDate) {
        this.setEventDate(eventDate);
        return this;
    }

    public void setEventDate(String eventDate) {
        this.eventDate = eventDate;
    }

    public String getLocation() {
        return this.location;
    }

    public CampusEvent location(String location) {
        this.setLocation(location);
        return this;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOrganizerId() {
        return this.organizerId;
    }

    public CampusEvent organizerId(String organizerId) {
        this.setOrganizerId(organizerId);
        return this;
    }

    public void setOrganizerId(String organizerId) {
        this.organizerId = organizerId;
    }

    public String getEventType() {
        return this.eventType;
    }

    public CampusEvent eventType(String eventType) {
        this.setEventType(eventType);
        return this;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public Integer getCapacity() {
        return this.capacity;
    }

    public CampusEvent capacity(Integer capacity) {
        this.setCapacity(capacity);
        return this;
    }

    public void setCapacity(Integer capacity) {
        this.capacity = capacity;
    }

    public EventStatus getStatus() {
        return this.status;
    }

    public CampusEvent status(EventStatus status) {
        this.setStatus(status);
        return this;
    }

    public void setStatus(EventStatus status) {
        this.status = status;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof CampusEvent)) {
            return false;
        }
        return getId() != null && getId().equals(((CampusEvent) o).getId());
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "CampusEvent{" +
            "id=" + getId() +
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
