package rakshan.himachal.dit.rakshan.Model;

import java.io.Serializable;

/**
 * Created by kuush on 11/7/2016.
 */

public class Register_User implements Serializable {

    public String photoname;
    public String photobase64encode;
    public String Username_User;
    public String Phone_User;
    public String Aadhaar_User;

    public String getUsername_User() {
        return Username_User;
    }

    public void setUsername_User(String username_User) {
        Username_User = username_User;
    }

    public String getPhone_User() {
        return Phone_User;
    }

    public void setPhone_User(String phone_User) {
        Phone_User = phone_User;
    }

    public String getAadhaar_User() {
        return Aadhaar_User;
    }

    public void setAadhaar_User(String aadhaar_User) {
        Aadhaar_User = aadhaar_User;
    }

    public String getEmail_User() {
        return Email_User;
    }

    public void setEmail_User(String email_User) {
        Email_User = email_User;
    }

    public String getGender_User() {
        return Gender_User;
    }

    public void setGender_User(String gender_User) {
        Gender_User = gender_User;
    }

    public String getDOB_User() {
        return DOB_User;
    }

    public void setDOB_User(String DOB_User) {
        this.DOB_User = DOB_User;
    }

    public String getIMEI_User() {
        return IMEI_User;
    }

    public void setIMEI_User(String IMEI_User) {
        this.IMEI_User = IMEI_User;
    }

    public String Email_User;
    public String Gender_User;
    public String DOB_User;
    public String IMEI_User;


    public String getPhotoname() {
        return photoname;
    }

    public void setPhotoname(String photoname) {
        this.photoname = photoname;
    }

    public String getPhotobase64encode() {
        return photobase64encode;
    }

    public void setPhotobase64encode(String photobase64encode) {
        this.photobase64encode = photobase64encode;
    }




}