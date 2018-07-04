package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.Application;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.OkHttpDownloader;
import com.squareup.picasso.Picasso;

public class TanyaDokterHewanChat extends Application {

    private DatabaseReference mUserDatabaseReference;
    private FirebaseAuth mAuth;


    @Override
    public void onCreate() {
        super.onCreate();
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        //PICASSO
        Picasso.Builder builder = new Picasso.Builder(this);
        builder.downloader(new OkHttpDownloader(this, Integer.MAX_VALUE));
        Picasso build = builder.build();
        build.setLoggingEnabled(true);
        Picasso.setSingletonInstance(build);

        mAuth = FirebaseAuth.getInstance();
        if (mAuth.getCurrentUser() != null){
            mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dokters").child(mAuth.getCurrentUser().getUid());
            mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if (dataSnapshot != null){
                        mUserDatabaseReference.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }
        /*mUserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child(mAuth.getCurrentUser().getUid());
        mUserDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot != null) {

                    mUserDatabaseReference.child("online").onDisconnect().setValue(ServerValue.TIMESTAMP);
                   // mUserDatabaseReference.child("online").setValue(true);

                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
*/

    }

}
