package rakshan.himachal.dit.sms.Model;

import java.io.Serializable;

/**
 * Created by kuush on 11/17/2016.
 */

public class vacationTravelDetailsPojo implements Serializable {

    private String houseLongitude;
    private String houseLatitude;
    private String toDate;
    private String fromDate;
    private String aadhaarNo;
    private String mobileNumber;
    private String policeStation;
    private String contactName;
    private String emergencyMobileNumber;
    private String emergencyMobileNumberTwo;
    private String address;
    private String IMEI;


    public String getHouseLatitude() {
        return houseLatitude;
    }

    public void setHouseLatitude(String houseLatitude) {
        this.houseLatitude = houseLatitude;
    }

    public String getHouseLongitude() {
        return houseLongitude;
    }

    public void setHouseLongitude(String houseLongitude) {
        this.houseLongitude = houseLongitude;
    }

    public String getToDate() {
        return toDate;
    }

    public void setToDate(String toDate) {
        this.toDate = toDate;
    }

    public String getFromDate() {
        return fromDate;
    }

    public void setFromDate(String fromDate) {
        this.fromDate = fromDate;
    }

    public String getAadhaarNo() {
        return aadhaarNo;
    }

    public void setAadhaarNo(String aadhaarNo) {
        this.aadhaarNo = aadhaarNo;
    }

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
    }

    public String getPoliceStation() {
        return policeStation;
    }

    public void setPoliceStation(String policeStation) {
        this.policeStation = policeStation;
    }

    public String getContactName() {
        return contactName;
    }

    public void setContactName(String contactName) {
        this.contactName = contactName;
    }

    public String getEmergencyMobileNumber() {
        return emergencyMobileNumber;
    }

    public void setEmergencyMobileNumber(String emergencyMobileNumber) {
        this.emergencyMobileNumber = emergencyMobileNumber;
    }

    public String getEmergencyMobileNumberTwo() {
        return emergencyMobileNumberTwo;
    }

    public void setEmergencyMobileNumberTwo(String emergencyMobileNumberTwo) {
        this.emergencyMobileNumberTwo = emergencyMobileNumberTwo;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getIMEI() {
        return IMEI;
    }

    public void setIMEI(String IMEI) {
        this.IMEI = IMEI;
    }


}
