package rakshan.himachal.dit.sms.HTTP;

/**
 * Created by kuush on 10/29/2016.
 */

import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;
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

    public String PostData_SOS(String... params){

        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String stringLatitude = null;
        String stringLongitude = null;
        String name = null;
        String Mobile_No = null;
        String IMEI = null;
        String IP_Address = null;
        String Date_Time = null;
        String Action_Status = null;
        String Remarks = null;
        String ActionDatenTime = null;

        try {

            URL = params[1];
            stringLatitude = params[2];
            stringLongitude = params[3];
            name = params[4];
            Mobile_No = params[5];
            IMEI = params[6];
            IP_Address = params[7];
            Date_Time = params[8];
            Action_Status = params[9];
            Remarks = params[10];
            ActionDatenTime = params[11];


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
                    .object().key("SOSRequest")
                    .object()
                    .key("Latitude").value(stringLatitude)
                    .key("Longitutde").value(stringLongitude)
                    .key("Name").value(name)
                    .key("Mobile").value(Mobile_No)
                    .key("IMEI").value(IMEI)
                    .key("IPAddress").value(IP_Address)
                    .key("Datetime").value(Date_Time)
                    .key("ActionStatus").value(Action_Status)
                    .key("Remark").value(Remarks)
                    .key("ActionDatetime").value(ActionDatenTime)
                    .endObject()
                    .endObject();

            System.out.println(userJson.toString());
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

    public String PostData_Registration(String... params){
        URL url_ = null;
        HttpURLConnection conn_ = null;
        StringBuilder sb = null;
        JSONStringer userJson = null;

        String URL = null;
        String ResName = null;
        String Mobile = null;
        String ResAadhaar = null;
        String EMail = null;
        String IMEI = null;
        String Gender = null;
        String ResDOB = null;
        String ResPhotoName = null;
        String ResPhoto = null;

        try {

            URL = params[1];
            ResName = params[2];
            Mobile = params[3];
            ResAadhaar = params[4];
            EMail = params[5];
            IMEI = params[6];
            Gender = params[7];
            ResDOB = params[8];
            ResPhotoName = params[9];
            ResPhoto = params[10];


            url_ =new URL(URL);
            conn_ = (HttpURLConnection)url_.openConnection();
            conn_.setDoOutput(true);
            conn_.setRequestMethod("POST");
            conn_.setUseCaches(false);
            conn_.setConnectTimeout(10000);
            conn_.setReadTimeout(10000);
            conn_.setRequestProperty("Content-Type", "application/json");
            conn_.connect();

            Thread T1 = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });


            JSONObject KL = new JSONObject("{\"imsge\":\"+ResPhoto+\"}");
            userJson = new JSONStringer()
                    .object().key("UsersRegistration")
                    .object()
                    .key("ResName").value(ResName)
                    .key("Mobile").value(Mobile)
                    .key("ResAadhaar").value(ResAadhaar)
                    .key("EMail").value(EMail)
                    .key("IMEI").value(IMEI)
                    .key("Gender").value(Gender)
                    .key("ResDOB").value(ResDOB)
                    .key("PhotoName").value(ResPhotoName)
                    .key("Photo").value(ResPhoto)
                    .endObject()
                    .endObject();



            T1.start();
            T1.join();

            System.out.println(userJson.toString());
            Log.e("Object",userJson.toString());
            Thread T2 = new Thread(new Runnable() {
                @Override
                public void run() {

                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });

            T2.start();
            T2.join();
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
        } catch (InterruptedException e) {
            e.printStackTrace();
        } finally{
            if(conn_!=null)
                conn_.disconnect();
        }
        return sb.toString();

    }


}