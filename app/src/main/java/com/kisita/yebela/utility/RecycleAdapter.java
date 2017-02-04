package com.kisita.yebela.utility;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;
import com.kisita.yebela.R;
import com.kisita.yebela.activities.ServiceChoicesActivity;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

import static com.kisita.yebela.R.id.item_title;


public class RecycleAdapter extends RecyclerView.Adapter<RecycleAdapter.GridItemViewHolder> {
    private Context context;
    private final Resources mResources;
    private AdapterView.OnItemClickListener mOnItemClickListener;
    private ArrayList<String> servicePicture = new ArrayList<>();
    private ArrayList<String> serviceName = new ArrayList<>();

    /*
     * Called when a grid adapter has been called
     *
     * @param context   The context of main activity
     */
    public RecycleAdapter(Context context) {

        this.context = context;
        mResources = context.getResources();
        initStringArrays();
    }

    private void initStringArrays() {
        JSONArray jsonArray;
        JSONObject category;
        try {
            jsonArray = new JSONArray(readCategoriesFromResources());
            for (int i = 0; i < jsonArray.length(); i++) {
                category = jsonArray.getJSONObject(i);
                serviceName.add(category.getString(JsonAttributes.NAME));
                servicePicture.add(category.getString(JsonAttributes.PICTURE));
            }
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readCategoriesFromResources() throws IOException {
        StringBuilder categoriesJson = new StringBuilder();
        InputStream rawCategories = mResources.openRawResource(R.raw.categories);
        BufferedReader reader = new BufferedReader(new InputStreamReader(rawCategories));
        String line;

        while ((line = reader.readLine()) != null) {
            categoriesJson.append(line);
        }
        return categoriesJson.toString();
    }

    /*
     * Called when RecyclerView needs a new {@link GridItemViewHolder} of the given type to represent
     * an item.
     * <p/>
     * This new ViewHolder should be constructed with a new View that can represent the items
     * of the given type. You can either create a new View manually or inflate it from an XML
     * layout file.
     * <p/>
     * The new ViewHolder will be used to display items of the adapter using
     * {@link #onBindViewHolder(GridItemViewHolder, int)}. Since it will be re-used to display
     * different items in the data set, it is a good idea to cache references to sub views of
     * the View to avoid unnecessary {@link View#findViewById(int)} calls.
     *
     * @param parent   The ViewGroup into which the new View will be added after it is bound to
     *                 an adapter position.
     * @param position The view type of the new View.
     * @return A new ViewHolder that holds a View of the given view type.
     * @see #getItemViewType(int)
     * @see #onBindViewHolder(GridItemViewHolder, int)
     */
    @Override
    public GridItemViewHolder onCreateViewHolder(ViewGroup parent, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.item_list, parent, false);
        return new GridItemViewHolder(itemView, this);
    }


    /*
     * Called by RecyclerView to display the data at the specified position. This method should
     * update the contents of the {@link GridItemViewHolder#itemView} to reflect the item at the given
     * position.
     * <p/>
     * Note that unlike {@link ListView}, RecyclerView will not call this method
     * again if the position of the item changes in the data set unless the item itself is
     * invalidated or the new position cannot be determined. For this reason, you should only
     * use the <code>position</code> parameter while acquiring the related data item inside
     * this method and should not keep a copy of it. If you need the position of an item later
     * on (e.g. in a click listener), use {@link GridItemViewHolder#getAdapterPosition()} which will
     * have the updated adapter position.
     * <p/>
     * Override {@link #onBindViewHolder(GridItemViewHolder, int)} instead if Adapter can
     * handle effcient partial bind.
     *
     * @param holder   The ViewHolder which should be updated to represent the contents of the
     *                 item at the given position in the data set.
     * @param position The position of the item within the adapter's data set.
     */
    @Override
    public void onBindViewHolder(final GridItemViewHolder holder, int position) {
        holder.mTitle.setText(serviceName.get(position));
        holder.mImage.setImageResource(context.getResources().getIdentifier(servicePicture.get(position),"drawable",context.getPackageName()));

        this.mOnItemClickListener = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(context,ServiceChoicesActivity.class);
                // Pass data object in the bundle and populate details activity.
                intent.putExtra("title", serviceName.get(i));
                intent.putExtra(context.getString(R.string.service_id),i);
                //ActivityOptionsCompat options = ActivityOptionsCompat.
                //        makeSceneTransitionAnimation((Activity)context,view.findViewById(R.id.item_title), "profile");
                context.startActivity(intent);
            }
        };
    }


    /*
     * Returns the total number of items in the data set hold by the adapter.
     *
     * @return The total number of items in this adapter.
     */

    @Override
    public int getItemCount() {
        return serviceName.size();
    }

    private void onItemHolderClick(GridItemViewHolder itemHolder) {
        if (mOnItemClickListener != null) {
            mOnItemClickListener.onItemClick(null, itemHolder.itemView,
                    itemHolder.getAdapterPosition(), itemHolder.getItemId());
        }
    }


    class GridItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView mTitle;
        ImageView mImage;
        RecycleAdapter mAdapter;

        GridItemViewHolder(View itemView, RecycleAdapter mAdapter) {
            super(itemView);
            this.mAdapter = mAdapter;
            //Typeface face= Typeface.createFromAsset(context.getAssets(), "orange_juice.ttf");
            mTitle = (TextView) itemView.findViewById(item_title);
            mImage = (ImageView) itemView.findViewById(R.id.item_image);
           // mTitle.setTypeface(face);
            itemView.setOnClickListener(this);
        }

        /*
         * Called when a view has been clicked.
         *
         * @param v The view that was clicked.
         */
        @Override
        public void onClick(View v) {
            mAdapter.onItemHolderClick(this);
        }
    }
}
