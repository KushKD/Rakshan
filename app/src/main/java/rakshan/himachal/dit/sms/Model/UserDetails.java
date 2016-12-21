package rakshan.himachal.dit.sms.Model;

import java.io.Serializable;

/**
 * Created by kuush on 11/3/2016.
 */

public class UserDetails implements Serializable {


    private String Name;
    private String IMEI;
    private String Mobile;
    private String Email;
    private String ResAadhaar;
    private String Gender;
    private String PhotoName;
    private byte[] Photo;
    private String DateTime;

    public String getDateTime() {
        return DateTime;
    }

    public void setDateTime(String dateTime) {
        DateTime = dateTime;
    }



    public byte[] getPhoto() {
        return Photo;
    }

    public void setPhoto(byte[] photo) {
        Photo = photo;
    }


    public String getEmail() {
        return Email;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }

    public String getMobile() {
        return Mobile;
    }

    public void setMobile(String mobile) {
        Mobile = mobile;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getResAadhaar() {
        return ResAadhaar;
    }

    public void setResAadhaar(String resAadhaar) {
        ResAadhaar = resAadhaar;
    }


    public String getGender() {
        return Gender;
    }

    public void setGender(String gender) {
        Gender = gender;
    }


    public String getPhotoName() {
        return PhotoName;
    }

    public void setPhotoName(String photoName) {
        PhotoName = photoName;
    }


}
