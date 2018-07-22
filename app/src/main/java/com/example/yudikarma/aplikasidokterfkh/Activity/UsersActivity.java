package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yudikarma.aplikasidokterfkh.Model.Users;
import com.example.yudikarma.aplikasidokterfkh.R;
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

import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;
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


public class UsersActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private RecyclerView mListView;
    private String nambahaja;
    private DatabaseReference mDatabaseReference;
    //private FirebaseListAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseRecyclerAdapter<Users,UserviewHolder> adapter;

    private ProgressDialog mProgressDialog;
    private ImageView statusOnline;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users");
        mDatabaseReference.keepSynced(true);

        mToolbar = (Toolbar) findViewById(R.id.user_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("List Pasien");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        statusOnline = (ImageView) findViewById(R.id.user_single_online_icon);

        mListView = (RecyclerView) findViewById(R.id.user_list);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        Query query = FirebaseDatabase.getInstance().getReference().child("Users").child("Pasien").limitToLast(50);


        FirebaseRecyclerOptions<Users> options =
                new FirebaseRecyclerOptions.Builder<Users>()
                        .setQuery(query, Users.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Users, UserviewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull UserviewHolder holder, int position, @NonNull Users model) {


                holder.setNama(model.getName());
                final String nama = model.getName();
                holder.setstatus(model.getAddress());
                holder.setMcCircleImageView(model.getThumb_image());


                mProgressDialog.dismiss();


                final String user_id = getRef(position).getKey();

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        CharSequence options[] = new CharSequence[]{"Mengirim Pesan","Lihat Profile","Lihat Riwayat penyakit"};
                        AlertDialog.Builder builder = new AlertDialog.Builder(UsersActivity.this);

                        builder.setTitle("Select Options");
                        builder.setItems(options, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                //click event for each item
                                if (i == 1){
                                    Intent profilIntent = new Intent(UsersActivity.this,DetailUser.class);
                                    // i.putExtra("user_id",list_user_id);
                                    profilIntent.putExtra("user_id",user_id );
                                    startActivity(profilIntent);

                                }
                                if (i == 0){
                                    Intent profilIntent = new Intent(UsersActivity.this,Tampung_chatActivity.class);
                                    // i.putExtra("user_id",list_user_id);
                                    profilIntent.putExtra("user_id",user_id );
                                    profilIntent.putExtra("user_name",nama);
                                    startActivity(profilIntent);

                                }
                                if (i == 2){
                                    FirebaseDatabase.getInstance().getReference().child("RekamMedis").addValueEventListener(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(DataSnapshot dataSnapshot) {
                                            if (dataSnapshot.hasChild(user_id)){
                                                Intent intent = new Intent(UsersActivity.this,ListVerivikasiHewanBerobat.class);
                                                intent.putExtra("user_id", user_id);
                                                startActivity(intent);

                                            }else {
                                                Toast.makeText(UsersActivity.this, "Maaf, User ini belum memiliki catatan Rekam Medis", Toast.LENGTH_LONG).show();
                                            }
                                        }

                                        @Override
                                        public void onCancelled(DatabaseError databaseError) {

                                        }
                                    });

                                }
                            }
                        });
                        builder.show();
                        /*builder.show();

                        Intent i = new Intent(UsersActivity.this, ProfilActivity.class);
                        i.putExtra("user_id", user_id);
                        startActivity(i);*/

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
        mProgressDialog = new ProgressDialog(UsersActivity.this);
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
                Picasso.with(UsersActivity.this).load(img_uri).networkPolicy(NetworkPolicy.OFFLINE)
                        .placeholder(R.drawable.default_avatar).into(mcCircleImageView, new Callback() {
                    @Override
                    public void onSuccess() {

                    }

                    @Override
                    public void onError() {
                        Picasso.with(UsersActivity.this).load(img_uri).placeholder(R.drawable.default_avatar).into(mcCircleImageView);

                    }
                });

            }else{
                Picasso.with(UsersActivity.this).load(R.drawable.default_avatar).into(mcCircleImageView);

            }
        }
    }

}
