package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.ProgressDialog;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.example.yudikarma.aplikasidokterfkh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;


public class DetailUser extends AppCompatActivity {
    private TextView mDisplayname;
    private TextView mstatus;
    private TextView jumlahteman,jumlahrekammedis,email_setting,nohp_setting,address_setting,status;
    private FloatingActionButton btn_changeimage_setting;
    private ProgressDialog mProgressDialog;
    private CircleImageView mCircleImageView;
    private String iduser ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail_user);
        mDisplayname = findViewById(R.id.display_name_detail);
        jumlahteman = findViewById(R.id.jumlahtemandetail);
        jumlahrekammedis = findViewById(R.id.jumlahrequestriend);
        email_setting = findViewById(R.id.email_detail);
        nohp_setting = findViewById(R.id.nohp_detail);
        address_setting = findViewById(R.id.address_detail);
        status = findViewById(R.id.status);

        mCircleImageView = findViewById(R.id.circleImageViewdetail);

        iduser = getIntent().getStringExtra("user_id");
        FirebaseDatabase.getInstance().getReference().child("Users").child("Pasien").child(iduser).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String image = dataSnapshot.child("image").getValue().toString();
                String displayname = dataSnapshot.child("name").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                String nohp = dataSnapshot.child("no_hp").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String statuss = dataSnapshot.child("status").getValue().toString();
                if (!image.equals("default")){
                    Picasso.with(DetailUser.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mCircleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(DetailUser.this).load(image).placeholder(R.drawable.default_avatar).into(mCircleImageView);

                        }
                    });

                }else{
                    Picasso.with(DetailUser.this).load(R.drawable.default_avatar).into(mCircleImageView);

                }


                mDisplayname.setText(displayname);
                email_setting.setText(email);
                nohp_setting.setText(nohp);
                address_setting.setText(address);
                status.setText(statuss);

                FirebaseDatabase.getInstance().getReference().child("Friends").child(iduser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long jumlah = dataSnapshot.getChildrenCount();
                        jumlahteman.setText(""+jumlah);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("RekamMedis").child(iduser).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long jumlah = dataSnapshot.getChildrenCount();
                        jumlahrekammedis.setText(""+jumlah);
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


    }
}
