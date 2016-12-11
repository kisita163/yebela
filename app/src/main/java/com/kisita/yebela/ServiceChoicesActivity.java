package com.kisita.yebela;

import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.daimajia.slider.library.Animations.DescriptionAnimation;
import com.daimajia.slider.library.SliderLayout;
import com.daimajia.slider.library.SliderTypes.BaseSliderView;
import com.daimajia.slider.library.SliderTypes.TextSliderView;
import com.daimajia.slider.library.Tricks.ViewPagerEx;

import java.util.HashMap;

public class ServiceChoicesActivity extends AppCompatActivity implements BaseSliderView.OnSliderClickListener, ViewPagerEx.OnPageChangeListener{
    private SliderLayout mDemoSlider;
    private int[][] slider_resources = {{R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
                                         {R.drawable.restoration_1,R.drawable.restoration_2,R.drawable.restoration_3,R.drawable.restoration_4,R.drawable.restoration_5},
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

        mDemoSlider = (SliderLayout)findViewById(R.id.slider);
        TextView text = (TextView)findViewById(R.id.title_choices);
        text.setText(getIntent().getStringExtra("title"));

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
