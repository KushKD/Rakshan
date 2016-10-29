package rakshan.himachal.dit.rakshan.HTTP;

/**
 * Created by kuush on 10/29/2016.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONStringer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

public class HttpManager {


    public String GetData(String url) {
        System.out.print(url);
        BufferedReader reader = null;

        try {
            URL url_ = new URL(url);
            HttpURLConnection con = (HttpURLConnection) url_.openConnection();

            if (con.getResponseCode() != 200) {
                return "Timeout";
            }


            reader = new BufferedReader(new InputStreamReader(con.getInputStream()));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            con.disconnect();
            return sb.toString();

        } catch (Exception e) {
            e.printStackTrace();
            return "Timeout";
        } finally {
            if (reader != null) {
                try {
                    reader.close();

                } catch (IOException e) {
                    e.printStackTrace();
                    return "Error";
                }
            }
        }
    }

    public String PostData_Vehicle_IN(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String Parking_ID = null;
        String Car_Type = null;
        String Car_Number = null;
        String Driver_Name = null;
        String Phone_Number = null;
        String ES_Parking_Time = null;
        String time = null;

        try {

            URL = params[1];
            Parking_ID = params[2];
            Car_Type = params[3];
            Car_Number = params[4];
            Driver_Name = params[5];
            Phone_Number = params[6];
            ES_Parking_Time = params[7];
            time = params[8];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("ParkTrans")
                    .object()
                    .key("ParkingId").value(Parking_ID)
                    .key("TypeofCar").value(Car_Type)
                    .key("VehicleNo").value(Car_Number)
                    .key("DriverName").value("")
                    .key("PhoneNumber").value(Phone_Number)
                    .key("EstimatedParkingtime").value(ES_Parking_Time)
                    .key("EstimatedFee").value("")
                    .key("InTime").value(time)
                    .key("OutTime").value("")
                    .key("ActualFee").value("")
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    public String PostData_Vehicle_OUT(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String Parking_Id = null;
        String Driver_Name = null;
        String Phone_number = null;
        String Vehicle_NO = null;
        String OUT_Time = null;
        String Result_to_Show = null;
        String URL = null;

        try {

            URL = params[1];
            Parking_Id = params[2];
            Driver_Name = params[3];
            Phone_number = params[4];
            Vehicle_NO = params[5];
            OUT_Time = params[6];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("VehicleOuts")
                    .object()
                    .key("VehicleNo").value(Vehicle_NO)
                    .key("ParkingId").value(Parking_Id)
                    .key("DriverName").value(Driver_Name)
                    .key("PhoneNumber").value(Phone_number)
                    .key("OutTime").value(OUT_Time)
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            //  Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }

    public String PostData_Vehicle_OUT_Confirm(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String Parking_Id = null;
        String Driver_Name = null;
        String Phone_number = null;
        String Vehicle_NO = null;
        String OUT_Time = null;
        String Result_to_Show = null;
        String URL = null;
        String Aadhaar = null;

        try {

            URL = params[1];
            Parking_Id = params[2];
            Driver_Name = params[3];
            Phone_number = params[4];
            Vehicle_NO = params[5];
            OUT_Time = params[6];
            Aadhaar = params[7];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            userJson = new JSONStringer()
                    .object().key("VehicleOuts")
                    .object()
                    .key("VehicleNo").value(Vehicle_NO)
                    .key("ParkingId").value(Parking_Id)
                    .key("DriverName").value(Driver_Name)
                    .key("PhoneNumber").value(Phone_number)
                    .key("OutTime").value(OUT_Time)
                    .key("OprAadhaar").value(Aadhaar)
                    .endObject()
                    .endObject();


            //  System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            OutputStreamWriter out = new OutputStreamWriter(conn_.getOutputStream());
            out.write(userJson.toString());
            out.close();

            try{
                int HttpResult =conn_.getResponseCode();
                if(HttpResult !=HttpURLConnection.HTTP_OK){
                    return "Timeout.";

                }else{
                    BufferedReader br = new BufferedReader(new InputStreamReader(conn_.getInputStream(),"utf-8"));
                    String line = null;
                    sb = new StringBuilder();
                    while ((line = br.readLine()) != null) {
                        sb.append(line + "\n");
                    }
                    br.close();
                    System.out.println(sb.toString());
                }

            } catch(Exception e){
                return "Error";
            }

        }
        catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();
    }


}