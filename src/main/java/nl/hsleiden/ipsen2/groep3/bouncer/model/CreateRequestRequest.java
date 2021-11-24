package nl.hsleiden.ipsen2.groep3.bouncer.model;

import org.springframework.web.multipart.MultipartFile;

import java.io.Serializable;

public class CreateRequestRequest implements Serializable {
    private String fName;
    private String lName;
    private String eMail;
    private String birthday;
    private MultipartFile file;

    public CreateRequestRequest() {
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

    public String geteMail() {
        return eMail;
    }

    public void seteMail(String eMail) {
        this.eMail = eMail;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public MultipartFile getFile() {
        return file;
    }

    public void setFile(MultipartFile file) {
        this.file = file;
    }
}
