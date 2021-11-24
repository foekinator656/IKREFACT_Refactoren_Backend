package nl.hsleiden.ipsen2.groep3.bouncer.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * the Entity QrCode with its attributes and with the relevant getter's en setter's
 *
 * @author youp van Leeuwen
 * @author Stef Haring
 */


@Entity
public class QrCode implements Serializable {
    @Id
    private String code;

    @OneToOne
    private Request request;

    private LocalDateTime validTill;

    public QrCode() {
    }

    public QrCode(String code, Request request, LocalDateTime validTill) {
        this.code = code;
        this.request = request;
        this.validTill = validTill;
    }

    public String getCode() {
        return code;
    }

    public QrCode setCode(String code) {
        this.code = code;

        return this;
    }

    public Request getRequest() {
        return request;
    }

    public QrCode setRequest(Request request) {
        this.request = request;

        return this;
    }

    public LocalDateTime getValidTill() {
        return validTill;
    }

    public QrCode setValidTill(LocalDateTime validTill) {
        this.validTill = validTill;

        return this;
    }

    @Override
    public String toString() {
        return "QrCode{" +
                "code='" + code + '\'' +
                '}';
    }
}
