package com.example.yudikarma.aplikasidokterfkh.Activity;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.yudikarma.aplikasidokterfkh.Adapter.SliderAdapter;
import com.example.yudikarma.aplikasidokterfkh.R;
import com.google.firebase.auth.FirebaseAuth;
public class FlashScreen extends AppCompatActivity {

    private ViewPager slideview;
    private LinearLayout dotslayout;

    private TextView[] mDotslayout;

    private SliderAdapter sliderAdapter;
    private Button txtFinish;
    private int mCurrentPage;

    //fIREBASE
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        slideview = (ViewPager) findViewById(R.id.slideview);
        dotslayout = (LinearLayout) findViewById(R.id.dotslayout);

        sliderAdapter = new SliderAdapter(this);
        slideview.setAdapter(sliderAdapter);
        addDotsIndikator(0);

        slideview.addOnPageChangeListener(viewListener);
        txtFinish = (Button) findViewById(R.id.btnFinish);
        txtFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(FlashScreen.this,LoginActivity.class);
                startActivity(intent);
                finish();

            }
        });
        //fIREBASE
        mAuth = FirebaseAuth.getInstance();
    }

    public void addDotsIndikator(int position){
        mDotslayout = new TextView[3];
        dotslayout.removeAllViews();
        for(int i=0;i<mDotslayout.length;i++){
            mDotslayout[i] = new TextView(this);
            mDotslayout[i].setText(Html.fromHtml("&#8226;"));
            mDotslayout[i].setTextSize(35);
            mDotslayout[i].setTextColor(getResources().getColor(R.color.colorTransparentWhite));

            dotslayout.addView(mDotslayout[i]);

        }

        if(mDotslayout.length > 0){
            mDotslayout[position].setTextColor(getResources().getColor(R.color.colorWhite));
        }

    }

    ViewPager.OnPageChangeListener viewListener = new ViewPager.OnPageChangeListener() {
        @Override
        public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

        }

        @Override
        public void onPageSelected(int position) {
            addDotsIndikator(position);
            mCurrentPage = position;
            if (position == mDotslayout.length -1){
                txtFinish.setEnabled(true);
                txtFinish.setVisibility(View.VISIBLE);
                txtFinish.setText("Finish");


            } else{
                txtFinish.setEnabled(false);
                txtFinish.setVisibility(View.INVISIBLE);
                //btnFinish.setText("Finish");
            }
        }

        @Override
        public void onPageScrollStateChanged(int state) {

        }
    };
}
