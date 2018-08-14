package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.example.yudikarma.aplikasidokterfkh.R;

public class Panduan extends AppCompatActivity {
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_panduan);
        mToolbar = (Toolbar) findViewById(R.id.toolbarpanduan);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Panduan Pengguna");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotomain = new Intent(Panduan.this,MainActivity.class);
                gotomain.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(gotomain);
                finish();
            }
        });
    }
}
