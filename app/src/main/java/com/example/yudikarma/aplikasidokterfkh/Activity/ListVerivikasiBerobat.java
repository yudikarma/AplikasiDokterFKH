package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudikarma.aplikasidokterfkh.Model.Users;
import com.example.yudikarma.aplikasidokterfkh.R;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;

import de.hdodenhof.circleimageview.CircleImageView;

public class ListVerivikasiBerobat extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mListView;
    private String nambahaja;
    private DatabaseReference mDatabaseReference;
    //private FirebaseListAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseRecyclerAdapter<Users,ListVerivikasiBerobat.UserviewHolder> adapter;

    private ProgressDialog mProgressDialog;
    private ImageView statusOnline;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_verivikasi_user_berobat);

        String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();

        mProgressDialog = new ProgressDialog(this);

        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Pasien");
        mDatabaseReference.keepSynced(true);

        mToolbar = (Toolbar) findViewById(R.id.backmain);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("List Pasien");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListVerivikasiBerobat.this,MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });
        statusOnline = (ImageView) findViewById(R.id.user_single_online_icon);

        mListView = (RecyclerView) findViewById(R.id.listVerifikasi);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        Query query = FirebaseDatabase.getInstance().getReference().child("RekamMedis").limitToLast(50);


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Users, UserviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final UserviewHolder holder, final int position, @NonNull final Users model) {

                final String list_user_id = getRef(position).getKey();

                mDatabaseReference.child(list_user_id).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        holder.setNama(dataSnapshot.child("name").getValue().toString());
                        holder.setstatus(dataSnapshot.child("address").getValue().toString());
                        holder.setMcCircleImageView(dataSnapshot.child("thumb_image").getValue().toString());




                        mProgressDialog.dismiss();
                        final String user_id = getRef(position).getKey();

                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent i = new Intent(ListVerivikasiBerobat.this, ListVerivikasiHewanBerobat.class);
                                i.putExtra("user_id", user_id);

                                startActivity(i);
                                //Toast.makeText(ListVerivikasiBerobat.this, user_id, Toast.LENGTH_SHORT).show();

                            }
                        });


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }

            @NonNull
            @Override
            public UserviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_singgle_layout, parent, false);
                return new UserviewHolder(mView);
            }
        };
        mListView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
        mProgressDialog = new ProgressDialog(ListVerivikasiBerobat.this);
        mProgressDialog.setTitle("load all user data..");
        mProgressDialog.setMessage("please wait..");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();


        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public class UserviewHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView mdisplayname,mstatus ;
        CircleImageView mcCircleImageView;
        public UserviewHolder(View itemView) {
            super(itemView);
            mView = itemView;

            mdisplayname = (TextView) mView.findViewById(R.id.user_singgle_name);
            mstatus  = (TextView) mView.findViewById(R.id.user_single_status);
            mcCircleImageView = (CircleImageView) mView.findViewById(R.id.profil_single_layout);



        }
        public void setNama(String display_name){

            mdisplayname.setText(display_name);

        }
        public void setstatus(String status){
            mstatus.setText(status);
        }
        public  void setMcCircleImageView(final String img_uri){
            //Picasso.with(UsersActivity.this).load(img_uri).placeholder(R.drawable.user).into(mcCircleImageView);
            if (!img_uri.equals("default")){
                Picasso.with(ListVerivikasiBerobat.this).load(img_uri).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_avatar).into(mcCircleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(ListVerivikasiBerobat.this).load(img_uri).placeholder(R.drawable.default_avatar).into(mcCircleImageView);

                    }
                });

            }else{
                Picasso.with(ListVerivikasiBerobat.this).load(R.drawable.default_avatar).into(mcCircleImageView);

            }
        }
    }
}
