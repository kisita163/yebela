package com.kisita.yebela.utility;

/*
 * Created by Hugues on 25-11-16.
 */

import android.content.Context;
import android.database.Cursor;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import com.kisita.yebela.R;
import java.util.Random;

public class ResultFragmentAdapter extends CursorAdapter implements Filterable {

    private static class ViewHolder {
        final ImageView iconView;
        final TextView placeView;
        final TextView cityView;
        final TextView typeView;
        final TextView numberView;

        ViewHolder(View view) {
            iconView = (ImageView) view.findViewById(R.id.resultImage);
            placeView = (TextView) view.findViewById(R.id.resultName);
            cityView = (TextView) view.findViewById(R.id.resultCity);
            typeView = (TextView) view.findViewById(R.id.resultType);
            numberView = (TextView) view.findViewById(R.id.resultNumber);
        }
    }

    public ResultFragmentAdapter(Context context, Cursor c, int flags) {
        super(context, c, flags);
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
        String name = cursor.getString(1);
        String city = cursor.getString(2);
        String type = cursor.getString(7);
        String number = cursor.getString(10);

        viewHolder.placeView.setText(name);
        viewHolder.cityView.setText(city);
        viewHolder.typeView.setText(type);
        viewHolder.numberView.setText(number);
        viewHolder.iconView.setBackgroundColor(generateColor());
    }

    private int generateColor()
    {
        Random rnd = new Random();
        return (Color.argb(255, rnd.nextInt(256), rnd.nextInt(256), rnd.nextInt(256)));
    }
}
