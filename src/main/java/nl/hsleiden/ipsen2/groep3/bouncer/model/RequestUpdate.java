package nl.hsleiden.ipsen2.groep3.bouncer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import org.hibernate.annotations.CreationTimestamp;
import org.springframework.lang.Nullable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.time.LocalDate;

/**
 * the Entity RequestUpdate with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 */

@Entity
public class RequestUpdate {
    @Id
    @GeneratedValue
    private Long id;

    @CreationTimestamp
    private LocalDate updatedAt;

    private Status newState;

    @ManyToOne
    @Nullable
    private UserAccount updatedBy;

    @ManyToOne
    @JsonBackReference
    private Request request;

    public void setNewState(Status newState) {
        this.newState = newState;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getId() {
        return id;
    }
}
