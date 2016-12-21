package rakshan.himachal.dit.sms.DatabaseHandler;

import android.database.sqlite.SQLiteOpenHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.HashMap;

import rakshan.himachal.dit.sms.Model.UserDetails;

/**
 * Created by kuush on 12/21/2016.
 */

public class DatabaseHandler extends SQLiteOpenHelper {
    boolean bool = false;
    static final ArrayList<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
    private ArrayList<HashMap<String, String>> list_Users_Details = new ArrayList<HashMap<String, String>>();


    // All Static variables
    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String
            DATABASE_NAME = "RAKSHAN_DATABASE";

    // USER DETAILS table name
    private static final String TABLE_USER_DETAILS = "USER_REGISTRAITON_DETAILS";


    //User Details Columns
    private static final String KEY_ID = "id";
    private static final String NAME = "ResName";
    private static final String IMEI = "imei";
    private static final String Mobile = "Mobile";
    private static final String PHOTO_NAME = "Photo_Name";
    private static final String GENDER = "Gender";
    private static final String AADHAAR = "Aadhaar";
    private static final String Email = "ContactPersonemail";
    private static final String EMERGENCY_CONTACT1 = "EmergencyContact1";
    private static final String EMERGENCY_CONTACT2 = "EmergencyContact2";
    private static final String EMERGENCY_CONTACT3 = "EmergencyContact3";
    private static final String DATE_TIME = "DATE_TIME";


    //Database Columns HASHMAP Attendance Details
    public static final String ID_DB = "id";
    private static final String NAME_DB = "ResName";
    private static final String IMEI_DB = "imei";
    private static final String MobileDB = "Mobile";
    private static final String PHOTO_NAMEDB = "Photo_Name";
    private static final String GENDER_DB = "Gender";
    private static final String AADHAAR_DB = "Aadhaar";
    private static final String Email_DB = "ContactPersonemail";
    private static final String EMERGENCY_CONTACT1_DB = "EmergencyContact1";
    private static final String EMERGENCY_CONTACT2_DB = "EmergencyContact2";
    private static final String EMERGENCY_CONTACT3_DB = "EmergencyContact3";
    private static final String DATE_TIME_DB = "DATE_TIME";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // Creating Tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_USERDATA_TABLE = "CREATE TABLE " + TABLE_USER_DETAILS + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + NAME + " TEXT,"
                + IMEI + " TEXT,"
                + Mobile + " TEXT,"
                + PHOTO_NAME + " TEXT,"
                + GENDER + " TEXT,"
                + AADHAAR + " TEXT,"
                + Email + " TEXT,"
                + EMERGENCY_CONTACT1 + " TEXT,"
                + EMERGENCY_CONTACT2 + " TEXT,"
                + EMERGENCY_CONTACT3 + " TEXT,"
                + DATE_TIME + " TEXT" + ")";


        db.execSQL(CREATE_USERDATA_TABLE);
    }

    // Upgrading database
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_DETAILS);

        // Create tables again
        onCreate(db);
    }

    /**
     * All CRUD(Create, Read, Update, Delete) Operations
     */

    public Boolean addContact(UserDetails Details) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(NAME, Details.getName());
        values.put(IMEI, Details.getIMEI());
        values.put(Mobile, Details.getMobile());
        values.put(PHOTO_NAME, Details.getPhotoName());
        values.put(GENDER, Details.getGender());
        values.put(AADHAAR, Details.getResAadhaar());
        values.put(Email, Details.getEmail());
        values.put(EMERGENCY_CONTACT1, "");
        values.put(EMERGENCY_CONTACT2, "");
        values.put(EMERGENCY_CONTACT3, "");
        values.put(DATE_TIME, Details.getDateTime());

        Log.e("Valaues", values.toString());

        // Inserting Row
        db.insert(TABLE_USER_DETAILS, null, values);
        db.close(); // Closing database connection

        try {
            exportDatabse(DATABASE_NAME);
            return true;
        } catch (Exception e) {
            Log.d("Got Error ..", e.getLocalizedMessage());
            return false;
        }

    }


    // // Getting the Complete Database in a List Attendance
    public ArrayList<HashMap<String, String>> GetAllData_AttendanceStatus() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_DETAILS + " ORDER BY " + DATE_TIME + " DESC";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        while (cursor.moveToNext()) {
            HashMap<String, String> Attendance_Details = new HashMap<String, String>();
            //Log.d(KEY_ID_DB, cursor.getString(0));
            Attendance_Details.put(ID_DB, cursor.getString(0));
            Attendance_Details.put(NAME_DB, cursor.getString(1));
            Attendance_Details.put(IMEI_DB, cursor.getString(2));
            Attendance_Details.put(MobileDB, cursor.getString(3));
            Attendance_Details.put(PHOTO_NAMEDB, cursor.getString(4));
            Attendance_Details.put(GENDER_DB, cursor.getString(5));
            Attendance_Details.put(AADHAAR_DB, cursor.getString(6));
            Attendance_Details.put(Email_DB, cursor.getString(7));
            Attendance_Details.put(EMERGENCY_CONTACT1_DB, cursor.getString(8));
            Attendance_Details.put(EMERGENCY_CONTACT2_DB, cursor.getString(9));
            Attendance_Details.put(EMERGENCY_CONTACT3_DB, cursor.getString(10));
            Attendance_Details.put(DATE_TIME_DB, cursor.getString(11));
            list_Users_Details.add(Attendance_Details);
        }
        return list_Users_Details;

    }


    // Getting contacts Count
    public int getFeedbackCount() {
        String countQuery = "SELECT  * FROM " + TABLE_USER_DETAILS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        int count = cursor.getCount();
        cursor.close();
        db.close();

        return count;
    }

    // Updating single contact
  /*  public int updateContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName());
        values.put(KEY_PH_NO, contact.getPhoneNumber());
        // updating row
        return db.update(TABLE_CONTACTS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
    }*/

    // Deleting single contact
  /*  public void deleteContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CONTACTS, KEY_ID + " = ?",
                new String[] { String.valueOf(contact.getID()) });
        db.close();
    }*/

    // Adding new contact
   /* void addContact(Contact contact) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_NAME, contact.getName()); // Contact Name
        values.put(KEY_PH_NO, contact.getPhoneNumber()); // Contact Phone
        // Inserting Row
        db.insert(TABLE_CONTACTS, null, values);
        db.close(); // Closing database connection
    }*/

    // Getting single contact
   /* Contact getContact(int id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_REPORTING, new String[] { KEY_ID,
                        KEY_NAME, KEY_PH_NO }, KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();
        Contact contact = new Contact(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return contact;
    }*/

    // Getting All Contacts
  /*  public List<FingurePoJo> getAllContacts() {
        List<FingurePoJo> FingureList = new ArrayList<FingurePoJo>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_REPORTING;
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                FingurePoJo FP = new FingurePoJo();
               // contact.setID(Integer.parseInt(cursor.getString(0)));
               // contact.setName(cursor.getString(1));
               // contact.setPhoneNumber(cursor.getString(2));
                FP.setFingure_DB(cursor.getString(1));
                Log.d(cursor.getString(1) , "Nothing");
                FingureList.add(FP);
            } while (cursor.moveToNext());
        }
        // return contact list
        return FingureList;
    }*/


    public void exportDatabse(String databaseName) {

        try {
            File sd = Environment.getExternalStorageDirectory();
            File data = Environment.getDataDirectory();

            if (sd.canWrite()) {
                Log.e("We are Here", "1");
                String currentDBPath = "//data//" + DatabaseHandler.class.getPackage().getName() + "//databases//" + databaseName + "";
                Log.e("We are Here", "2");
                String backupDBPath = "Rakshan.db";
                Log.e("We are Here", "3");
                File currentDB = new File(data, currentDBPath);
                Log.e("Path", currentDBPath.toString());
                Log.e("We are Here", "4");
                File backupDB = new File(sd, backupDBPath);
                Log.e("Path", backupDB.toString());

                try {
                    Log.e("Exists", Boolean.toString(currentDB.exists()));
                } catch (Exception e) {
                    Log.d("Error", e.getLocalizedMessage().toString());
                }

                try {
                    if (currentDB.exists()) {
                        FileChannel src = new FileInputStream(currentDB).getChannel();
                        Log.e("We are Here", "6");
                        FileChannel dst = new FileOutputStream(backupDB).getChannel();
                        Log.e("We are Here", "7");
                        dst.transferFrom(src, 0, src.size());
                        Log.e("We are Here", "8");
                        src.close();
                        dst.close();
                    } else {
                        Log.d("Error", "No Idea");
                    }
                } catch (Exception e) {
                    Log.d("Error", e.getLocalizedMessage().toString());
                }
            } else {
                Log.d("Error", "No Idea 2");
            }
        } catch (Exception e) {
            Log.d("Error", e.getLocalizedMessage().toString());
        }
    }
}