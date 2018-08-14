package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.yudikarma.aplikasidokterfkh.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class ChangeStatus extends AppCompatActivity {
    private EditText status;
    private Button simpan,back;
    private DatabaseReference dokterdatabase;
    private Toolbar mToolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_status);

        mToolbar = (Toolbar) findViewById(R.id.toolbarstatus);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("Change Bio");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(ChangeStatus.this,SettingActivity.class);

                startActivity(intent);*/
                finish();
            }
        });
        status = findViewById(R.id.edittextstatus);
        simpan = findViewById(R.id.simpan);

        final String userid = getIntent().getStringExtra("iduser");
        String statuss = getIntent().getStringExtra("status");
       /* status.setText(statuss);*/

        dokterdatabase = FirebaseDatabase.getInstance().getReference().child("Users").child("Dokters").child(userid);
        simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newsstatus = status.getText().toString();
               if (!newsstatus.isEmpty()){
                   Map update = new HashMap();
                   update.put("status",newsstatus );
                   dokterdatabase.updateChildren(update, new DatabaseReference.CompletionListener() {
                       @Override
                       public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                           if (databaseError == null){
                               Intent intent = new Intent(ChangeStatus.this,SettingActivity.class);
                              /* intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);*//*

                               startActivity(intent);*/
                               finish();

                           }else {
                               String messageerror = databaseError.getMessage();
                               Toast.makeText(ChangeStatus.this,"Database Error",Toast.LENGTH_SHORT).show();

                           }
                       }
                   });

               }else {
                   Toast.makeText(ChangeStatus.this, "tidak boleh kosong", Toast.LENGTH_SHORT).show();
               }


            }
        });


    }
}
