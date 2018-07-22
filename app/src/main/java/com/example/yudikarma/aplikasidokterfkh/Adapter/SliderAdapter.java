package com.example.yudikarma.aplikasidokterfkh.Adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v4.view.PagerAdapter;
import android.view.View;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yudikarma.aplikasidokterfkh.R;

public class SliderAdapter extends PagerAdapter {
    Context context;
    LayoutInflater layoutInflater;

    public SliderAdapter(Context context) {
        this.context = context;

    }
    //Array image
    public int[] slideImages= {
            R.drawable.doctor6,
            R.drawable.nurse1,
            R.drawable.medicalhistory8
    };

    //Array Heading
    public String[] slideHeading = {
            "Konsultasi",
            "Daftar Berobat",
            "Rekam Medis "
    };
    public String[] slideDesc = {
            "Konsultasikan Penyakit yang  di derita hewan peliharaan kamu kepada dokter kami, temukan manfaatnya dengan biaya RP 0 rupiah",
            "Daftarkan Hewan Peliharaan kamu untuk berobat pada Klinik Fakultas Kedokteran Hewan setelah berkonsultasi dengan dokter kami",
            "Rekam Riwayat Penyakit yang pernah diderita hewan peliharaan kamu untuk mempermudah proses pemeriksaan penyakit dimasa mendatang"
    };


    @Override
    public int getCount() {
        return slideHeading.length;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == (RelativeLayout) object;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {

        layoutInflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);
        View view = layoutInflater.inflate(R.layout.slide_layout,container, false);

        ImageView slideImageview = (ImageView) view.findViewById(R.id.slideImage);
        TextView slideTextviewHeading = (TextView) view.findViewById(R.id.slideHeading);
        TextView slideTextviewDesc = (TextView) view.findViewById(R.id.slideDesc);

        slideImageview.setImageResource(slideImages[position]);
        slideTextviewHeading.setText(slideHeading[position]);
        slideTextviewDesc.setText(slideDesc[position]);

        container.addView(view);
        return view;
    };

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {

        container.removeView((RelativeLayout)object);
    }

}
