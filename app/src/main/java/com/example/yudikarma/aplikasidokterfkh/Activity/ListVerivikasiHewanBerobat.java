package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.example.yudikarma.aplikasidokterfkh.Model.Hewan;
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

public class ListVerivikasiHewanBerobat extends AppCompatActivity {


    private Toolbar mToolbar;
    private RecyclerView mListView;
    private String nambahaja;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference userDatabase;
    //private FirebaseListAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseRecyclerAdapter<Hewan, ListDaftarHolder> adapter;
    private FirebaseRecyclerAdapter<Hewan, ListDaftarHolder> adapter1;

    private ProgressDialog mProgressDialog;

    private Users users1;
    private String muser;
    private Query query;
    private String idhewan;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_verivikasi_hewan_berobat);

        muser = FirebaseAuth.getInstance().getUid();

        mToolbar = (Toolbar) findViewById(R.id.user_appbar);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("List Hewan Peliharaan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListVerivikasiHewanBerobat.this,ListVerivikasiBerobat.class);

                startActivity(intent);
                finish();
            }
        });


        mListView = (RecyclerView) findViewById(R.id.listhewan);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        final String id_pasien = getIntent().getStringExtra("user_id");

        //QUERY
        Query query = FirebaseDatabase.getInstance().getReference().child("RekamMedis").child(id_pasien).limitToLast(50);






        final FirebaseRecyclerOptions<Hewan> options =
                new FirebaseRecyclerOptions.Builder<Hewan>()
                        .setQuery(query, Hewan.class)
                        .setLifecycleOwner(this)
                        .build();



        /*adapter1 = new FirebaseRecyclerAdapter<Hewan, ListDaftarHolder>(options1) {
            @Override
            protected void onBindViewHolder(@NonNull ListDaftarHolder holder, int position, @NonNull Hewan model) {
                final String idhewan = getRef(position).getKey();
                FirebaseDatabase.getInstance().getReference().child("Hewan")


            }

            @NonNull
            @Override
            public ListDaftarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                return null;
            }
        };*/

        adapter = new FirebaseRecyclerAdapter<Hewan, ListDaftarHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListDaftarHolder holder, int position, @NonNull Hewan model) {
                final String idrekammedis = getRef(position).getKey();
               /* FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                       *//*idhewan = dataSnapshot.getKey().toString();
                       Log.i("idhwannnnnnn", idhewan);*//*

                       for (DataSnapshot child : dataSnapshot.getChildren()){
                        String data1 = child.getKey();
                           String data2 = child.getChildren().toString();
                           String data3 = child.getValue().toString();

                           holder.setNamehewan(data1);

                           Log.i("data2", ""+data2);
                           Log.i("data3", ""+data3);


                       }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });*/
              // final String idhewan = FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).push().getKey();

                FirebaseDatabase.getInstance().getReference().child("RekamMedis").child(id_pasien).child(idrekammedis).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        /*idhewan = FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).getKey();
                        Log.i("id hewan", idhewan);*/

                        final String idhewan = dataSnapshot.child("idhewan").getValue().toString();



                        String namapenyakit = dataSnapshot.child("dugaan").getValue().toString();
                        holder.setDugaanpenyakit(namapenyakit);


                       /* holder.setNamehewan(nama);
                        holder.setJenishewan(jenis);
                        holder.setRashewan(Ras);*/


                        FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).child(idhewan).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                final String nama = dataSnapshot.child("nama_hewan").getValue().toString();
                                String jenis = dataSnapshot.child("jenis_hewan").getValue().toString();
                                Log.i("namaaaaaaaa", nama);
                                holder.setNamehewan(nama);
                                holder.setJenishewan(jenis);

                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });
                        Log.i("id hewan",""+ idhewan);
                        holder.mView.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(ListVerivikasiHewanBerobat.this,DetailVerifikasi.class);
                                intent.putExtra("id_rekam_medis", idrekammedis);
                                intent.putExtra("id_pasien", id_pasien);
                                startActivity(intent);
                               /* CharSequence options[] = new CharSequence[]{"Daftar Berobat","Lihat Detail","Hapus"};
                                AlertDialog.Builder builder = new AlertDialog.Builder(ListVerivikasiHewanBerobat.this);

                                builder.setTitle("Select Options");
                                builder.setItems(options, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        //click event for each item
                                        if (i == 0){

                                            //DAFTAR BEROBAT LANJUT
                                            Intent profilIntent = new Intent(ListVerivikasiHewanBerobat.this,DetailVerifikasi.class);
                                            // i.putExtra("user_id",list_user_id);
                                            profilIntent.putExtra("namahewan",listNamaHewan );
                                            startActivity(profilIntent);
                                        }
                                        if (i == 1){
                                            //LIHAT DETAIL
                                            Intent profilIntent = new Intent(ListDaftarBerobat.this,DetailHewan.class);
                                            // i.putExtra("user_id",list_user_id);
                                            profilIntent.putExtra("namahewan",listNamaHewan );

                                            startActivity(profilIntent);

                                        }
                                        if (i == 2){
                                            //HAPUS RECORD
                                        }
                                    }
                                });
                                builder.show();*/

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
            public ListDaftarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hewan_single_layout,parent,false);
                return new ListDaftarHolder(mView);
            }
        };
        mListView.setAdapter(adapter);
    }


    @Override
    protected void onStart() {
        super.onStart();
       /* mProgressDialog = new ProgressDialog(UsersActivity.this);
        mProgressDialog.setTitle("load all user data..");
        mProgressDialog.setMessage("please wait..");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();*/


        adapter.startListening();



    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }

    public void remove(){
        RelativeLayout relativeLayout = findViewById(R.id.hewan_single_layout);
        relativeLayout.setVisibility(View.GONE);

    }

    public class ListDaftarHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView dugaanpenyakit,jenishewan,namehewan ;

        public ListDaftarHolder(View itemView) {
            super(itemView);
            mView = itemView;

            dugaanpenyakit = mView.findViewById(R.id.dugaanpenyakit);
            namehewan = mView.findViewById(R.id.namahewan);
            jenishewan = mView.findViewById(R.id.jenishewan);
        }

        public void setNamehewan (String namahewan1){

            namehewan.setText(namahewan1);

        }
        public void setJenishewan(String jenishewan1){
            jenishewan.setText(jenishewan1);
        }
        public void setDugaanpenyakit(String rashewan1){
            dugaanpenyakit.setText(rashewan1);
        }




    }
}
