package com.kisita.yebela;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.IntegerRes;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class ServiceChoicesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private int[][] slider_resources = {{R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.event_1,R.drawable.event_2,R.drawable.event_3,R.drawable.event_4,R.drawable.event_5},
                                         {R.drawable.culture_1,R.drawable.culture_2,R.drawable.culture_3,R.drawable.culture_4,R.drawable.culture_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.loisir_1,R.drawable.loisir_2,R.drawable.loisir_3,R.drawable.loisir_4,R.drawable.loisir_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5}};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_service_choices);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");

        TextView text = (TextView)findViewById(R.id.title_choices);
        text.setText(getIntent().getStringExtra("title"));

        set_slider_parameters();
        set_layout_choices();
    }

    private void set_layout_choices() {
        final Intent intent = new Intent(this,ResultsActivity.class);
        int service = getIntent().getIntExtra(getString(R.string.service_id),0);
        intent.putExtra(getString(R.string.service_id),service);

        LinearLayout layout_choice_1 = (LinearLayout) findViewById(R.id.layout_choice_1);
        LinearLayout layout_choice_2 = (LinearLayout) findViewById(R.id.layout_choice_2);
        LinearLayout layout_choice_3 = (LinearLayout) findViewById(R.id.layout_choice_3);
        LinearLayout layout_choice_4 = (LinearLayout) findViewById(R.id.layout_choice_4);
        LinearLayout layout_choice_5 = (LinearLayout) findViewById(R.id.layout_choice_5);
        LinearLayout layout_choice_6 = (LinearLayout) findViewById(R.id.layout_choice_6);

        layout_choice_1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        layout_choice_2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        layout_choice_3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        layout_choice_4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        layout_choice_5.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
        layout_choice_6.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(intent);
            }
        });
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
}
