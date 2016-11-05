package rakshan.himachal.dit.rakshan.Helper;

/**
 * Created by kuush on 10/29/2016.
 */

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
public class Date_Time {

    /**
     * Function to get Data and Time
     * @return
     */
    public static String GetDateAndTime(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(c.getTime());


        String IN_TIME = formattedDate;

        return IN_TIME;
    }

    public static String GetDateAndTimeImage(){

        Calendar c = Calendar.getInstance();
        SimpleDateFormat df = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss");
        String formattedDate = df.format(c.getTime());


        String IN_TIME = formattedDate;

        return IN_TIME;
    }


    public static String Change_Date_Format(String date_Current) throws ParseException {

        String strCurrentDate = date_Current;
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd hh:mm a");
        Date newDate = format.parse(strCurrentDate);

        format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String date = format.format(newDate);

        return date;
    }


}