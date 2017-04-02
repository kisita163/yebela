package com.kisita.yebela.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;
import com.kisita.yebela.R;
import com.kisita.yebela.utility.JsonAttributes;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;

public class ServiceChoicesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private final String TAG = this.getClass().getName();
    private Resources mResources;
    private ArrayList<String> choicePicture = new ArrayList<>();
    private ArrayList<String> choiceName = new ArrayList<>();
    private int[][] slider_resources = {{R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.event_1,R.drawable.event_2,R.drawable.event_3,R.drawable.event_4,R.drawable.event_5},
                                         {R.drawable.culture_1,R.drawable.culture_2,R.drawable.culture_3,R.drawable.culture_4,R.drawable.culture_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.loisir_1,R.drawable.loisir_2,R.drawable.loisir_3,R.drawable.loisir_4,R.drawable.loisir_5},
                                         {R.drawable.well_being_1,R.drawable.well_being_2,R.drawable.well_being_3,R.drawable.well_being_4,R.drawable.well_being_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_choices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG,"Click on back button");
                finish();
            }
        });
        getSupportActionBar().setTitle("");
        mResources = this.getResources();

        TextView text = (TextView)findViewById(R.id.title_choices);
        text.setText(getIntent().getStringExtra("title"));

        initStringArrays();
        set_slider_parameters();
        set_layout_choices();
    }

    private void initStringArrays() {
        JSONArray jsonArray;
        JSONObject category;
        JSONArray categories;

        try {
            jsonArray = new JSONArray(readCategoriesFromResources());
            for (int i = 0; i < jsonArray.length(); i++) {
                category = jsonArray.getJSONObject(i);
                if(category.getString(JsonAttributes.NAME).equalsIgnoreCase(getIntent().getStringExtra("title"))){
                    categories =  category.getJSONArray(JsonAttributes.CATEGORIES);
                    for (int k = 0; k < categories.length(); k++) {
                        choiceName.add(categories.getJSONObject(k).getString(JsonAttributes.NAME));
                        choicePicture.add(categories.getJSONObject(k).getString(JsonAttributes.PICTURE));
                    }

                }
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

    private void set_layout_choices() {
        final Intent searchList = new Intent(this,SearchActivity.class);
        final Intent map = new Intent(this,MapsActivity.class);
        String choice;

        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid_layout);
        int service = getIntent().getIntExtra(getString(R.string.service_id),0);
        searchList.putExtra(getString(R.string.service_id),service);

        for(int i=0 ; i < gridLayout.getChildCount() ; i++){
            final View v = gridLayout.getChildAt(i);
            if(i < choiceName.size()){
                    if (v instanceof LinearLayout) {
                        for (int k = 0; k < ((LinearLayout) v).getChildCount(); k++) {
                            View m = ((LinearLayout) v).getChildAt(k);
                            if (m instanceof ImageView) {
                                ((ImageView)m).setImageResource(this.getResources().getIdentifier(choicePicture.get(i),"drawable",this.getPackageName()));
                            }
                            if (m instanceof TextView) {
                                choice = choiceName.get(i);
                                ((TextView) m).setText(choice);
                                final String finalChoice = choice;
                                v.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        searchList.putExtra(getString(R.string.service_id), finalChoice);
                                        startActivity(searchList);
                                    }
                                });
                            }
                        }
                    }
            }
            else if(i == choiceName.size()){
                if (v instanceof LinearLayout) {
                    for (int k = 0; k < ((LinearLayout) v).getChildCount(); k++) {
                        View m = ((LinearLayout) v).getChildAt(k);
                        if (m instanceof ImageView) {
                            ((ImageView)m).setImageResource(R.drawable.ic_search_black_24dp);
                        }
                        if (m instanceof TextView) {
                            ((TextView) m).setText(R.string.other);
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(searchList);
                                }
                            });
                        }
                    }
                }
            } /*else if(i == choiceName.size() + 1){
                if (v instanceof LinearLayout) {
                    for (int k = 0; k < ((LinearLayout) v).getChildCount(); k++) {
                        View m = ((LinearLayout) v).getChildAt(k);
                        if (m instanceof ImageView) {
                            ((ImageView) m).setImageResource(R.drawable.location_icon);
                        }
                        if (m instanceof TextView) {
                            ((TextView) m).setText(R.string.near);
                            v.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    startActivity(map);
                                    //v.setBackgroundColor(Color.parseColor("#9e9fa1"));
                                }
                            });
                        }
                    }
                }
            }*/else{
                if (v instanceof LinearLayout) {
                    v.setVisibility(View.GONE);
                }
            }
        }
    }

    private void set_slider_parameters() {
        SliderLayout mDemoSlider = (SliderLayout) findViewById(R.id.slider);
        HashMap<String,Integer> file_maps = new HashMap<>();
        String id = "";
        for(int i = 0;i < 5;i++){
            file_maps.put(id,slider_resources[getIntent().getIntExtra(getString(R.string.service_id),0)][i]);
            id += " ";
        }

        for(String name : file_maps.keySet()){
            TextSliderView textSliderView = new TextSliderView(this);
            // initialize a SliderLayout
            textSliderView
                    .description(name)
                    .image(file_maps.get(name))
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener(this);

            //add your extra information
            textSliderView.bundle(new Bundle());
            textSliderView.getBundle()
                    .putString("extra",name);

            mDemoSlider.addSlider(textSliderView);
        }
        mDemoSlider.setPresetTransformer(SliderLayout.Transformer.Accordion);
        mDemoSlider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom);
        mDemoSlider.setCustomAnimation(new DescriptionAnimation());
        mDemoSlider.setDuration(4000);
        mDemoSlider.addOnPageChangeListener(this);
    }

   /* @Override
    protected void onStop() {
        super.onStop();
        GridLayout gridLayout = (GridLayout)findViewById(R.id.grid_layout);
        for(int i=0 ; i < gridLayout.getChildCount() ; i++) {
            final View v = gridLayout.getChildAt(i);
            if (v instanceof LinearLayout) {
                v.setBackgroundResource(R.color.cardview_light_background);
            }
        }
    }*/

    @Override
    public void onSliderClick(BaseSliderView slider) {

    }

    @Override
    public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

    }

    @Override
    public void onPageSelected(int position) {

    }

    @Override
    public void onPageScrollStateChanged(int state) {

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_settings) {
            Intent settings = new Intent(this,SettingsActivity.class);
            startActivity(settings);
        }

        if(id == R.id.search_icon){
            Intent search = new Intent(this,SearchActivity.class);
            startActivity(search);
            //onSearchRequested();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_services, menu);

        /*SearchManager searchManager =
                (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView =
                (SearchView) menu.findItem(R.id.search).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getComponentName()));*/
       /* MenuItem search_icon = menu.findItem(R.id.search_icon);
        search_icon.setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {

                return false;
            }
        });*/
        return true;
    }
}
