package nl.hsleiden.ipsen2.groep3.bouncer.model;

import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.Entity;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import java.io.Serializable;

/**
 * the Entity UserAccount with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 */
@Entity
public class UserAccount extends User implements Serializable {
    private String email;
    private String password;
    private String firstName;
    private String lastName;

    public UserAccount() {
        super();
    }

    public UserAccount(Long id, Site site, String username, String email, String password, String firstName, String lastName, Role role) {
        super(id, site, username, role);
        this.email = email;
        this.password = password;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @JsonIgnore
    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
