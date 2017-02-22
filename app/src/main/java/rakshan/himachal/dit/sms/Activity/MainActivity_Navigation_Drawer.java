package rakshan.himachal.dit.sms.Activity;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.dit.kushkumardhawan.com.materialhelp.MaterialTutorialActivity;
import org.dit.kushkumardhawan.com.materialhelp.TutorialItem;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import rakshan.himachal.dit.sms.DatabaseHandler.DatabaseHandler;
import rakshan.himachal.dit.sms.Helper.AppStatus;
import rakshan.himachal.dit.sms.Presentation.CircleButton;
import rakshan.himachal.dit.sms.Presentation.CircleImageView;
import rakshan.himachal.dit.sms.Presentation.Custom_Dialog;
import rakshan.himachal.dit.sms.R;
import rakshan.himachal.dit.sms.Services.SendLocationService;
import rakshan.himachal.dit.sms.fragments.OneFragment;
import rakshan.himachal.dit.sms.fragments.ThreeFragment;
import rakshan.himachal.dit.sms.fragments.TwoFragment;

public class MainActivity_Navigation_Drawer extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    DatabaseHandler DH = null;
    Custom_Dialog CD = new Custom_Dialog();
    private Toolbar toolbar;
    ActionBarDrawerToggle toggle;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private int[] tabIcons = {
            R.drawable.ic_tab_favourite,
            R.drawable.ic_tab_call,
            R.drawable.ic_tab_contacts
    };


    private AlarmManager alarmManager;
    private PendingIntent notifyIntent;
    private DrawerLayout drawer;

    ArrayList<HashMap<String,String>> listDB_UserDetails = null;
    private CircleButton SOS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main__navigation__drawer);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setLogo(R.mipmap.ic_launcher);
        setSupportActionBar(toolbar);

        SOS = (CircleButton) findViewById(R.id.fab);
        SOS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

               // Toast.makeText(MainActivity_Navigation_Drawer.this,"Button Working",Toast.LENGTH_LONG).show();
              //  Intent i = new Intent(MainActivity_Navigation_Drawer.this, Permissions.class);
              //  startActivity(i);
                try{
                    Log.e("We are in ","Set Alarms");
                setAlarms();
                }catch(Exception e){
                    Log.e("Error in ","Set Alarms");

                }
            }
        });

        SOS.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                Intent i = new Intent(MainActivity_Navigation_Drawer.this, SOS_Detailed.class);
                  startActivity(i);
                return false;
            }
        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close ){
            @Override
            public void onDrawerClosed(View drawerView) {
                super.onDrawerClosed(drawerView);
                supportInvalidateOptionsMenu();
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                super.onDrawerOpened(drawerView);
            }
        };

        toggle.setToolbarNavigationClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("We are","Here");
               /* if(!toggle.isDrawerIndicatorEnabled()) {
                    (MainActivity_Navigation_Drawer.onBackPressed();
                }*/
            }
        });

        toggle.setDrawerIndicatorEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);

        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        View header=navigationView.getHeaderView(0);
        ImageView Photo = (ImageView) header.findViewById(R.id.imageView);
        TextView  _Name_tv = (TextView)header.findViewById(R.id.name);
        TextView Phone_tv = (TextView)header.findViewById(R.id.phone);

        try{
            DH = new DatabaseHandler(MainActivity_Navigation_Drawer.this);
            listDB_UserDetails = new ArrayList<>();
            listDB_UserDetails = DH.GetAllData_UserDetails();

        }catch(Exception ex){
            CD.showDialog(MainActivity_Navigation_Drawer.this,ex.getLocalizedMessage().toString().trim());


        }

        Log.e("Photo Name",listDB_UserDetails.get(0).get(DatabaseHandler.PHOTO_NAMEDB));

        _Name_tv.setText(listDB_UserDetails.get(0).get(DatabaseHandler.NAME_DB));
        Phone_tv.setText(listDB_UserDetails.get(0).get(DatabaseHandler.MobileDB));
        Bitmap PhotoBmp = AppStatus.getBitmap(MainActivity_Navigation_Drawer.this, listDB_UserDetails.get(0).get(DatabaseHandler.PHOTO_NAMEDB));
        Photo.setImageBitmap(PhotoBmp);

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);

        setupTabIcons();
    }

    private void setAlarms() {
        Intent myIntent = new Intent(this,
                SendLocationService.class);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        notifyIntent = PendingIntent.getService(this, 0,
                myIntent, 0);
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.MINUTE, 1);
        Log.v("TAG", "Time for alarm trigger:" + calendar.getTime().toString());
        Toast.makeText(getApplicationContext(),"Time for alarm trigger: "+calendar.getTime().toString(),Toast.LENGTH_LONG).show();
        alarmManager.setRepeating(AlarmManager.RTC_WAKEUP,
                calendar.getTimeInMillis(), 5 * 60 * 1000, notifyIntent);
    }

    private void setupTabIcons() {
        TextView tabOne = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabOne.setText("HOME");
        tabOne.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_favourite, 0, 0);
        tabLayout.getTabAt(0).setCustomView(tabOne);

        TextView tabTwo = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabTwo.setText("SERVICES");
        tabTwo.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_call, 0, 0);
        tabLayout.getTabAt(1).setCustomView(tabTwo);

        TextView tabThree = (TextView) LayoutInflater.from(this).inflate(R.layout.custom_tab, null);
        tabThree.setText("SOCIAL");
        tabThree.setCompoundDrawablesWithIntrinsicBounds(0, R.drawable.ic_tab_contacts, 0, 0);
        tabLayout.getTabAt(2).setCustomView(tabThree);


    }

    private void setupViewPager(ViewPager viewPager) {
        MainActivity_Navigation_Drawer.ViewPagerAdapter adapter = new MainActivity_Navigation_Drawer.ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFrag(new OneFragment(), "HOME");
        adapter.addFrag(new TwoFragment(), "SERVICES");
        adapter.addFrag(new ThreeFragment(), "SOCIAL");
        viewPager.setAdapter(adapter);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main_activity__navigation__drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.profile) {
            try{
                Intent profile = new Intent(MainActivity_Navigation_Drawer.this,Profile.class);
                startActivity(profile);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Profile Activity not able to Load",Toast.LENGTH_LONG).show();
            }

        } else if(id == R.id.help){
            try{
                Intent load_tutorial = new Intent(MainActivity_Navigation_Drawer.this,MaterialTutorialActivity.class);
                load_tutorial.putParcelableArrayListExtra(MaterialTutorialActivity.MATERIAL_TUTORIAL_ARG_TUTORIAL_ITEMS, getTutorialItems(this));

                startActivity(load_tutorial);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Profile Activity not able to Load",Toast.LENGTH_LONG).show();
            }


        }else if(id == R.id.logout){
            try{
                Intent profile = new Intent(MainActivity_Navigation_Drawer.this,Profile.class);
                startActivity(profile);
            }catch(Exception e){
                Toast.makeText(getApplicationContext(),"Profile Activity not able to Load",Toast.LENGTH_LONG).show();
            }


        }

        else if(item.getItemId() == android.R.id.home){  //  use android.R.id
           // drawer.openDrawer(Gravity.LEFT);
            if(drawer.isDrawerOpen(Gravity.LEFT)) {
                drawer.closeDrawer(Gravity.LEFT);
            }else{
                drawer.openDrawer(Gravity.LEFT);
            }

        }


        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private ArrayList<TutorialItem> getTutorialItems(Context context) {
        TutorialItem tutorialItem1 = new TutorialItem(R.string.slide_1_african_story_books, R.string.slide_1_african_story_books,
                R.color.slide_1, R.drawable.tut_page_1_front, R.drawable.tut_page_1_background);

        TutorialItem tutorialItem2 = new TutorialItem(R.string.slide_2_volunteer_professionals, R.string.slide_2_volunteer_professionals_subtitle,
                R.color.slide_2, R.drawable.tut_page_2_front, R.drawable.tut_page_2_background);

        TutorialItem tutorialItem3 = new TutorialItem(context.getString(R.string.slide_3_download_and_go), null,
                R.color.slide_3, R.drawable.tut_page_3_foreground);

        TutorialItem tutorialItem4 = new TutorialItem(R.string.slide_4_different_languages, R.string.slide_4_different_languages_subtitle,
                R.color.slide_4, R.drawable.tut_page_4_foreground, R.drawable.tut_page_4_background);

        ArrayList<TutorialItem> tutorialItems = new ArrayList<>();
        tutorialItems.add(tutorialItem1);
        tutorialItems.add(tutorialItem2);
        tutorialItems.add(tutorialItem3);
        tutorialItems.add(tutorialItem4);

        return tutorialItems;
    }


    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFrag(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }
}
