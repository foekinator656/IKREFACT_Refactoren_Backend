package nl.hsleiden.ipsen2.groep3.bouncer.model;

import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;

/**
 * the Entity Site with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 * @author Wouter van der Neut
 */

@Entity
public class Site implements Serializable {
    @Id
    @GeneratedValue
    private Long id;
    private String name;
    private String url;

    @ManyToOne(fetch = FetchType.LAZY)
    private UserAccount createdBy;

    @CreationTimestamp
    private LocalDate createdAt;

    public Site() {
    }

    public Site(Long id, String name, String url, UserAccount createdBy, LocalDate createdAt) {
        this.id = id;
        this.name = name;
        this.url = url;
        this.createdBy = createdBy;
        this.createdAt = createdAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public UserAccount getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(UserAccount createdBy) {
        this.createdBy = createdBy;
    }

    public LocalDate getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDate createdAt) {
        this.createdAt = createdAt;
    }

    @Override
    public String toString() {
        return "Site{" +
                "name='" + name + '\'' +
                ", url='" + url + '\'' +
                '}';
    }
}
