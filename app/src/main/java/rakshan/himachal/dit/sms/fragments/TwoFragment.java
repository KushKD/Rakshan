package rakshan.himachal.dit.sms.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridView;
import android.widget.RelativeLayout;

import java.util.ArrayList;

import rakshan.himachal.dit.sms.Activity.MainActivity_Navigation_Drawer;
import rakshan.himachal.dit.sms.Adapters.ModuleAdapter;
import rakshan.himachal.dit.sms.R;


public class TwoFragment extends Fragment{

    GridView gv;
    Context context;
    ArrayList prgmName;
 //   public static String [] prgmNameList={"Let Us C","c++","JAVA","Jsp","Microsoft .Net","Android","PHP","Jquery","JavaScript"};
   // public static int [] prgmImages={R.mipmap.ic_launcher,R.drawable.images1,R.drawable.images2,R.drawable.images3,R.drawable.images4,R.drawable.images5,R.drawable.images6,R.drawable.images7,R.drawable.images8};

       public static String [] prgmNameList={"Vacation Locator","Travel Tracking","Locate Police Station"};
    public static int [] prgmImages={R.mipmap.ic_launcher,R.mipmap.ic_launcher,R.mipmap.ic_launcher};

    public TwoFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {

        RelativeLayout mLinearLayout = (RelativeLayout) inflater.inflate(R.layout.fragment_two,container, false);
        MainActivity_Navigation_Drawer activity = (MainActivity_Navigation_Drawer) getActivity();
        gv=(GridView)mLinearLayout.findViewById(R.id.gridView1);
        gv.setAdapter(new ModuleAdapter(activity, prgmNameList,prgmImages));

        // Inflate the layout for this fragment
        return mLinearLayout;
    }

}
