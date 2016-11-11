package rakshan.himachal.dit.sms.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import rakshan.himachal.dit.sms.Activity.MainActivity_Navigation_Drawer;
import rakshan.himachal.dit.sms.Activity.Permissions;
import rakshan.himachal.dit.sms.Activity.Registration;
import rakshan.himachal.dit.sms.Activity.VacationTraveller;
import rakshan.himachal.dit.sms.R;

/**
 * Created by kuush on 11/10/2016.
 */

public class ModuleAdapter extends BaseAdapter {
    String [] result;
    Context context;
    int [] imageId;
    private static LayoutInflater inflater=null;
    public ModuleAdapter(MainActivity_Navigation_Drawer mainActivity, String[] prgmNameList, int[] prgmImages) {
        // TODO Auto-generated constructor stub
        result=prgmNameList;
        context=mainActivity;
        imageId=prgmImages;
        inflater = (LayoutInflater)context.
                getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return result.length;
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    public class Holder
    {
        TextView tv;
        ImageView img;
    }
    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
        Holder holder=new Holder();
        View rowView;

        rowView = inflater.inflate(R.layout.modulelist, null);
        holder.tv=(TextView) rowView.findViewById(R.id.textView1);
        holder.img=(ImageView) rowView.findViewById(R.id.imageView1);

        holder.tv.setText(result[position]);
        holder.img.setImageResource(imageId[position]);

        rowView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                // TODO Auto-generated method stub


                if(result[position].toString().equalsIgnoreCase("Vacation Locator")){
                    Intent intent = new Intent(context,VacationTraveller.class);
                    context.startActivity(intent);
                }else if (result[position].toString().equalsIgnoreCase("Travel Tracking")){
                    Toast.makeText(context, "You Clicked "+result[position], Toast.LENGTH_LONG).show();
                   /* Intent intent = new Intent(context,Registration.class);
                    context.startActivity(intent);*/
                }else{
                    Toast.makeText(context, "Something went wrong.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return rowView;
    }

}
