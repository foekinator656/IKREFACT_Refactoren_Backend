package nl.hsleiden.ipsen2.groep3.bouncer.model;

/**
 * the Class AuthenticationRequestWithUID that can get and set UUID
 *
 * @author Youp van Leeuwen
 */
public class AuthenticationRequestWithUID {
    private String uid;

    public AuthenticationRequestWithUID() {
    }

    public AuthenticationRequestWithUID(String uid) {
        this.uid = uid;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
    }
}
