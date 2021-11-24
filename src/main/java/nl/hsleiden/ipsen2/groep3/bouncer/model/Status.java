package nl.hsleiden.ipsen2.groep3.bouncer.model;

/**
 * Statuses that a client can get
 *
 * @author Youp van Leeuwen
 * @author Wouter van der Neut
 */

public enum Status {
    DECLINED(0),
    APPROVED(1),
    EXPIRED(2),
    ACTION_REQUIRED(3),
    PENDING(4);

    private final int value;

    Status(int value) {
        this.value = value;
    }

    public int getValue() {
        return value;
    }

}
