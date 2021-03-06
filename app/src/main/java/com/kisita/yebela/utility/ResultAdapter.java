package com.kisita.yebela.utility;

/*
 * Created by Hugues on 25-11-16.
 */

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.graphics.Color;
import android.location.Location;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.kisita.yebela.R;
import com.kisita.yebela.activities.PlaceActivity;

import java.util.Random;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ResultAdapter extends CursorAdapter implements Filterable,AdapterView.OnItemClickListener{
    private Context mContext;

    @Override
    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
        Cursor c  = (Cursor)this.getItem(i);
        Intent intent = new Intent(mContext, PlaceActivity.class);
        intent.putExtra("PlaceId",c.getString(0)); // send the database item id
        mContext.startActivity(intent);
    }


    private class ViewHolder {
        final ImageView iconView;
        final TextView placeView;
        final TextView cityView;
        final TextView distanceView;

        ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.resultImage);
            placeView = (TextView) view.findViewById(R.id.resultName);
            cityView = (TextView) view.findViewById(R.id.resultVicinity);
            distanceView = (TextView)view.findViewById(R.id.resultDistance);
        }
    }

    public ResultAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
        this.mContext = context;
    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup viewGroup) {

        View view = LayoutInflater.from(context).inflate(R.layout.row_adapter, viewGroup, false);

        ViewHolder viewHolder = new ViewHolder(view);
        view.setTag(viewHolder);

        return view;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {
        ViewHolder viewHolder = (ViewHolder) view.getTag();
        Location destination = new Location("destination");
        Location currPosition = new Location("currPosition");

        String currLatitude ;
        String currLongitude ;

        SharedPreferences sharedPref = PreferenceManager.getDefaultSharedPreferences(mContext);
        String reference = sharedPref.getString("reference","");

        Pattern loc = Pattern.compile("(.*),(.*)");
        Matcher match = loc.matcher(reference);

        if(match.matches()) {
            if(!match.group(1).equalsIgnoreCase("0.0")) {
                currLatitude = match.group(1);
                currLongitude = match.group(2);
            }
            else // My position
            {
                sharedPref = mContext.getSharedPreferences(
                        mContext.getString(R.string.yebela_keys), Context.MODE_PRIVATE);

                currLatitude = sharedPref.getString(mContext.getString(R.string.latitude),"-4.4011293");
                currLongitude = sharedPref.getString(mContext.getString(R.string.longitude),"15.2527045");
            }
        }
        else { // Default lat long is Kinshasa
            currLatitude = "-4.4011293";
            currLongitude = "15.2527045";
        }

        String name = cursor.getString(1);
        String city = cursor.getString(8);
        String latitude = cursor.getString(4);
        String longitude = cursor.getString(3);

        destination.setLatitude(Double.valueOf(latitude));
        destination.setLongitude(Double.valueOf(longitude));

        currPosition.setLatitude(Double.valueOf(currLatitude));
        currPosition.setLongitude(Double.valueOf(currLongitude));

        int distance = Math.round(destination.distanceTo(currPosition)/1000);

        viewHolder.placeView.setText(name);
        viewHolder.cityView.setText(city);
        viewHolder.distanceView.setText(""+distance+" Km");
        viewHolder.iconView.setBackgroundColor(Color.parseColor("#ff91a2a4"));
    }
}
