package nl.hsleiden.ipsen2.groep3.bouncer.model;

import javax.persistence.Entity;


/**
 * the Entity UserAccount with its attributes and with the relevant getter's en setter's
 *
 * @author Youp van Leeuwen
 * @author Stef Haring
 */

@Entity
public class Worker extends User {
    private String uid;
    private String fName;
    private String lName;
    private String EMail;
    private String birthday;

    public Worker() {
    }

    public Worker(String uid, String fName, String lName, String EMail, String birthday) {
        this.uid = uid;
        this.fName = fName;
        this.lName = lName;
        this.EMail = EMail;
        this.birthday = birthday;
    }

    public Worker(Long id, String username, Role role, String uid, String fName, String lName, String EMail, String birthday) {
        super(id, username, role);
        this.uid = uid;
        this.fName = fName;
        this.lName = lName;
        this.EMail = EMail;
        this.birthday = birthday;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getEMail() {
        return EMail;
    }

    public void setEMail(String eMail) {
        this.EMail = eMail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }
}
