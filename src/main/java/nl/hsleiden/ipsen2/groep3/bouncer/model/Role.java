package nl.hsleiden.ipsen2.groep3.bouncer.model;

/**
 *  all the specific Roles that a client can have
 *
 * @author Youp van Leeuwen
 * @author Wouter van der Neut
 */

public enum Role {
    WORKER(0),
    MODERATOR(1),
    SITE_ADMIN(2),
    ADMIN(3);

    private final int value;

    Role(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }
}
