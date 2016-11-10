package rakshan.himachal.dit.sms.Model;

import java.io.Serializable;

/**
 * Created by kuush on 11/3/2016.
 */

public class UserDetails implements Serializable {

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

    private  String Name;
    private  String IMEI;
    private  String Mobile;

}
