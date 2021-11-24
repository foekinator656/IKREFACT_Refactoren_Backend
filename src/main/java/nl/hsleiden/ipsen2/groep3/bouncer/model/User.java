package nl.hsleiden.ipsen2.groep3.bouncer.model;

import javax.persistence.*;
import java.io.Serializable;

/**
 * the super Entity User with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 */

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
public class User implements Serializable {
    @Id
    @GeneratedValue()
    private Long id;

    @ManyToOne
    @JoinColumn(name = "site_id", nullable = true)
    protected Site site;
    protected String username;
    protected Role role;

    public User() {
    }

    public User(Long id, Site site, String username, Role role) {
        this.id = id;
        this.site = site;
        this.username = username;
        this.role = role;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Site getSite() {
        return site;
    }

    public void setSite(Site site) {
        this.site = site;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public Role getRole() {
        return role;
    }

    public void setRole(Role role) {
        this.role = role;
    }

    public boolean isGranted(Role role) {
        return this.getRole() == role;
    }

    public boolean isGrantedMinimum(Role role) {
        return this.getRole().getValue() >= role.getValue();
    }

    @Override
    public String toString() {
        return "User{" +
                "username='" + username + '\'' +
                '}';
    }
}
