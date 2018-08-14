package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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

public class ListHewanRiwayatPenyakit extends AppCompatActivity {

    private Toolbar mToolbar;
    private RecyclerView mListView;
    private String nambahaja;
    private DatabaseReference mDatabaseReference;
    private DatabaseReference userDatabase;
    //private FirebaseListAdapter adapter;
    private FirebaseDatabase mFirebaseDatabase;
    private FirebaseRecyclerAdapter<Hewan, ListHewanRiwayatPenyakit.ListDaftarHolder> adapter;
    private FirebaseRecyclerAdapter<Hewan, ListVerivikasiHewanBerobat.ListDaftarHolder> adapter1;

    private ProgressDialog mProgressDialog;

    private Users users1;
    private String muser;
    private Query query;
    private String idhewan;
    private TextView notifnull;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_hewan_riwayat_penyakit);

        muser = FirebaseAuth.getInstance().getUid();

        mToolbar = (Toolbar) findViewById(R.id.toolbarriwayat);
        setSupportActionBar(mToolbar);
        getSupportActionBar().setTitle("List Hewan Peliharaan");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ListHewanRiwayatPenyakit.this,ListVerivikasiBerobat.class);

                startActivity(intent);
                finish();
            }
        });
        notifnull = findViewById(R.id.notifnullriwayat);


        mListView = (RecyclerView) findViewById(R.id.listhewanriwayat);
        mListView.setHasFixedSize(true);
        mListView.setLayoutManager(new LinearLayoutManager(this));
        final String id_pasien = getIntent().getStringExtra("user_id");

        //QUERY
        Query query = FirebaseDatabase.getInstance().getReference().child("RiwayatPenyakit").child(id_pasien).limitToLast(50);






        final FirebaseRecyclerOptions<Hewan> options =
                new FirebaseRecyclerOptions.Builder<Hewan>()
                        .setQuery(query, Hewan.class)
                        .setLifecycleOwner(this)
                        .build();

        adapter = new FirebaseRecyclerAdapter<Hewan, ListHewanRiwayatPenyakit.ListDaftarHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull final ListHewanRiwayatPenyakit.ListDaftarHolder holder, int position, @NonNull Hewan model) {
                final String idriwayatpenyakit = getRef(position).getKey();

                FirebaseDatabase.getInstance().getReference().child("RiwayatPenyakit").child(id_pasien).child(idriwayatpenyakit).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        /*idhewan = FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).getKey();
                        Log.i("id hewan", idhewan);*/

                        final String idhewan = dataSnapshot.child("idhewan").getValue().toString();



                        String namapenyakit = dataSnapshot.child("gejala").getValue().toString();
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
                                Intent intent = new Intent(ListHewanRiwayatPenyakit.this,RiwayatPenyakit.class);
                                intent.putExtra("id_riwayatpenyakit", idriwayatpenyakit);
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
            public ListHewanRiwayatPenyakit.ListDaftarHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View mView = LayoutInflater.from(parent.getContext()).inflate(R.layout.hewan_single_layout,parent,false);
                return new ListDaftarHolder(mView);
            }
        };
        Log.i("adapter count :",""+adapter.getItemCount() );
       FirebaseDatabase.getInstance().getReference().child("RiwayatPenyakit").child(id_pasien).addValueEventListener(new ValueEventListener() {
           @Override
           public void onDataChange(DataSnapshot dataSnapshot) {
               long jumlah = dataSnapshot.getChildrenCount();
               if (jumlah>0){
                 /*  Toast.makeText(ListHewanRiwayatPenyakit.this, ""+jumlah, Toast.LENGTH_LONG).show();*/

                   mListView.setAdapter(adapter);
               }else {
                   notifnull.setVisibility(View.VISIBLE);
               }

           }

           @Override
           public void onCancelled(DatabaseError databaseError) {

           }
       });





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


    public class ListDaftarHolder extends RecyclerView.ViewHolder {
        View mView;
        TextView dugaanpenyakit,jenishewan,namehewan ;

        public ListDaftarHolder(View itemView) {
            super(itemView);
            mView = itemView;

            dugaanpenyakit = mView.findViewById(R.id.namaHewan);
            namehewan = mView.findViewById(R.id.jenis_hewan);
            jenishewan = mView.findViewById(R.id.Keluhan);
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
