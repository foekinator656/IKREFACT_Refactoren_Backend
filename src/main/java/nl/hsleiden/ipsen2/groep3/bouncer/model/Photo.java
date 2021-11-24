package nl.hsleiden.ipsen2.groep3.bouncer.model;

import com.fasterxml.jackson.annotation.JsonBackReference;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.io.Serializable;

/**
 * Used to get, set and store image files
 *
 * @author Wouter van der Neut
 */

@Entity
public class Photo implements Serializable {
    @Id
    @GeneratedValue
    private Long pid;

    private String filename;


    @ManyToOne
    @JsonBackReference
    private Request request;

    public Photo(){}

    public Photo(Long pid, String filename, Request request) {
        this.pid = pid;
        this.filename = filename;
        this.request = request;
    }

    public String getFilename() {
        return filename;
    }

    public void setFilename(String filename) {
        this.filename = filename;
    }

    public Request getRequest() {
        return request;
    }

    public void setRequest(Request request) {
        this.request = request;
    }

    public void setPid(Long pid) {
        this.pid = pid;
    }

    public Long getPid() {
        return pid;
    }

    @Override
    public String toString() {
        return "Photo{" +
                "pid=" + pid +
                ", filename='" + filename + '\'' +
                ", request=" + request +
                '}';
    }
}
