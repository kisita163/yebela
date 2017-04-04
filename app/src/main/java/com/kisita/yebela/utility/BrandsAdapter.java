package com.kisita.yebela.utility;


import android.content.Context;
import android.database.DataSetObserver;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;

import com.kisita.yebela.R;

import java.util.Random;

/**
 * Created by ELECTRO DEPOT on 02-04-17.
 */

public class BrandsAdapter extends ArrayAdapter {
    private Context context;
    static final public Integer[] pImages = {R.drawable.bookstore_icon,
            R.drawable.bar_cafe,
            R.drawable.amusement_park_icon,
            R.drawable.apartment_icon,
            R.drawable.bakery_icon,
            R.drawable.theater_icon
    };


    static final public String[] pNames = {  "Bas-Uele",
            "Equateur",
            "Haut-Uele",
            "Haut-Katanga",
            "Haut-Lomami",
            "Ituri"
    };


    public BrandsAdapter(Context context) {
        super(context,0);
        this.context = context;
    }

    @Override
    public boolean areAllItemsEnabled() {
        return false;
    }

    @Override
    public boolean isEnabled(int i) {
        return false;
    }

    @Override
    public void registerDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public void unregisterDataSetObserver(DataSetObserver dataSetObserver) {

    }

    @Override
    public int getCount() {
        return pNames.length;
    }

    @Override
    public Object getItem(int i) {
        return pImages[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View row = view;
        if(row == null) {
            //System.out.println("ConvertView is not provided");
            LayoutInflater inflater = (LayoutInflater) context.getSystemService( Context.LAYOUT_INFLATER_SERVICE );
            row = inflater.inflate(R.layout.brands_layout,null);
        }
        TextView brandName = (TextView)row.findViewById(R.id.brandName);
        ImageView brandImage = (ImageView)row.findViewById(R.id.brandImage);

        brandImage.setImageResource(pImages[i]);
        brandName.setText(pNames[i]);

        return row;
    }

    @Override
    public int getItemViewType(int i) {
        return 0;
    }


    @Override
    public boolean isEmpty() {
        return false;
    }

    public int generateColor()
    {
        Random rnd = new Random();
        int color = Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256));
        return (color);
    }
}



