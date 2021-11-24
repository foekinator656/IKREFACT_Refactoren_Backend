package nl.hsleiden.ipsen2.groep3.bouncer.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.data.annotation.CreatedBy;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;


/**
 * the Entity Request with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 * @author Stef Haring
 */

@Entity
public class Request implements Serializable {
    @Id
    @GeneratedValue
    private Long id;

    @ManyToOne
    @CreatedBy
    private Worker requestedBy;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @OneToMany
    @JsonManagedReference
    private Set<Photo> photos;

    @OneToMany(fetch = FetchType.EAGER, mappedBy = "request")
    @JsonManagedReference
    @OrderBy("id desc")
    private Set<RequestUpdate> updates;

    public Request() {
        this.photos = new HashSet<>();
        this.updates = new HashSet<>();
    }

    public Request(Long id, Worker requestedBy, LocalDateTime createdAt, Set<Photo> photos, Set<RequestUpdate> updates) {
        this.id = id;
        this.requestedBy = requestedBy;
        this.createdAt = createdAt;
        this.photos = photos;
        this.updates = updates;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Worker getRequestedBy() {
        return requestedBy;
    }

    public void setRequestedBy(Worker requestedBy) {
        this.requestedBy = requestedBy;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public Set<Photo> getPhotos() {
        return photos;
    }

    public void addPhoto (Photo photo) {
        photo.setRequest(this);
        this.photos.add(photo);
    }

    public void setPhotos(Set<Photo> photos) {
        this.photos = photos;
    }

    public Set<RequestUpdate> getUpdates() {
        return updates;
    }

    public void setUpdates(Set<RequestUpdate> updates) {
        this.updates = updates;
    }

    @Override
    public String toString() {
        return "Request{" +
                "id='" + id + '\'' +
                ", requestedBy=" + requestedBy +
                ", createdAt=" + createdAt +
                '}';
    }
}
