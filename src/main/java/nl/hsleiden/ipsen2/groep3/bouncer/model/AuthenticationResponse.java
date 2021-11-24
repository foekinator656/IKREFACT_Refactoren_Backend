package nl.hsleiden.ipsen2.groep3.bouncer.model;

/**
 * the Class AuthenticationResponse that can get JWT token and get User
 *
 * @author Youp van Leeuwen
 */

public class AuthenticationResponse {
    private final String jwt;
    private User user;

    public AuthenticationResponse(String jwt, User user) {
        this.jwt = jwt;
        this.user = user;
    }

    public String getJwt() {
        return jwt;
    }

    public User getUser() {
        return user;
    }
}
