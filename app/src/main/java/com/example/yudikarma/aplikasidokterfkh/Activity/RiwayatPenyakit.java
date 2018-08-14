package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudikarma.aplikasidokterfkh.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class RiwayatPenyakit extends AppCompatActivity {
    private TextView tnamapemilik,tnohp,tnamadokter,tgejala,tdugaan,ttanggalrekam,talamatpemilik,tnamahewan,tjenishewan,tjeniskelamin,tras,twarnabulu,tumur,tttl,txtdiagnosapenyakit,txtterapipenyakit,txtdokterperiksa,txttanggalperiksa;
    private DatabaseReference mRootDatabaseReference;
    private TextInputEditText penyakitdiderita,terapi;
    private Button simpan,kembali;
    private String sterapi,spenyakitdiderita;
    private LinearLayout lpenyakit,lterapi;
    private TextView diisisaat;
    private Toolbar toolbar;
    private String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
    private  String namadokter_periksa;
    private String idriwayat;
    private String diagnosa ;
    private String tanggalperiksa ;
    private String getSterapi;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_riwayat_penyakit);

        mRootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        toolbar = findViewById(R.id.toolbardetailverifikasi);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        penyakitdiderita = findViewById(R.id.penyakit_di_derita);
        terapi = findViewById(R.id.terapi);
        simpan = findViewById(R.id.simpanrekammedis);
        kembali = findViewById(R.id.kembali);

        tnamapemilik = findViewById(R.id.dnamapemilik);
        tnohp = findViewById(R.id.dnohp);
        tnamadokter = findViewById(R.id.dnamadokter);
        tgejala = findViewById(R.id.dgejala);
        tdugaan = findViewById(R.id.ddugaan);
        ttanggalrekam = findViewById(R.id.dtanggal_rekam);
        talamatpemilik = findViewById(R.id.dalamatpemilik);
        tnamahewan = findViewById(R.id.dnama_hewan);
        tjenishewan= findViewById(R.id.djenis_hewan);
        tjeniskelamin = findViewById(R.id.djenis_lk);
        tras = findViewById(R.id.dras);
        twarnabulu = findViewById(R.id.dwarnabulu);
        tumur = findViewById(R.id.dumur);
        tttl = findViewById(R.id.dttl);


        diisisaat = findViewById(R.id.isisaarpemeriksaan);
        txtdiagnosapenyakit = findViewById(R.id.dtxtdiagnosapenyakit);
        txtterapipenyakit = findViewById(R.id.dtxtterapipenyakit);
        txtdokterperiksa = findViewById(R.id.dtxtnamadokterperiksa);
        txttanggalperiksa = findViewById(R.id.dtxttanggalperiksa);


        final String idriwayatpenyakit = getIntent().getStringExtra("id_riwayatpenyakit");
        final  String id_pasien = getIntent().getStringExtra("id_pasien");


        FirebaseDatabase.getInstance().getReference().child("Users").child("Dokters").child(uid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                namadokter_periksa = dataSnapshot.child("name").getValue().toString();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               /* Intent intent = new Intent(RiwayatPenyakit.this,ListVerivikasiHewanBerobat.class);
                intent.putExtra("user_id", id_pasien);
                startActivity(intent);*/
                finish();

            }
        });






        FirebaseDatabase.getInstance().getReference().child("RiwayatPenyakit").child(id_pasien).child(idriwayatpenyakit).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                final String snamapemilik = dataSnapshot.child("nama_pemilik").getValue().toString();
                final String snohp = dataSnapshot.child("no_hp").getValue().toString();
                final String snamadokter = dataSnapshot.child("nama_dokter").getValue().toString();
                final String sgejala = dataSnapshot.child("gejala").getValue().toString();
                final String sdugaan = dataSnapshot.child("dugaan").getValue().toString();
                final String salamatpemilik = dataSnapshot.child("alamat_pemilik").getValue().toString();
                final String stanggalrekam  = dataSnapshot.child("tanggal_rekam").getValue().toString();
                final String idhewan = dataSnapshot.child("idhewan").getValue().toString();
                final String terapi2 = dataSnapshot.child("terapi").getValue().toString();



                FirebaseDatabase.getInstance().getReference().child("Hewan").child(id_pasien).child(idhewan).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        final String snamahewan = dataSnapshot.child("nama_hewan").getValue().toString();
                        final String sjenishewan = dataSnapshot.child("jenis_hewan").getValue().toString();
                        final String sjeniskelamin = dataSnapshot.child("jenis_lk").getValue().toString();
                        final String sras = dataSnapshot.child("ras").getValue().toString();
                        final String swarnabulu = dataSnapshot.child("warna_bulu").getValue().toString();
                        final String sumur = dataSnapshot.child("umur").getValue().toString();
                        final String sttl = dataSnapshot.child("ttl").getValue().toString();
                        final String salamathewan = dataSnapshot.child("alamat").getValue().toString();

                        final String currentDate = DateFormat.getDateTimeInstance().format(new Date());


                        tnamapemilik.setText(snamapemilik);
                        tnohp.setText(snohp);
                        tnamadokter.setText(snamadokter);
                        tgejala.setText(sgejala);
                        tdugaan.setText(sdugaan);
                        talamatpemilik.setText(salamatpemilik);
                        ttanggalrekam.setText(stanggalrekam);
                        tnamahewan.setText(snamahewan);
                        tjenishewan.setText(sjenishewan);
                        tjeniskelamin.setText(sjeniskelamin);
                        tras.setText(sras);
                        twarnabulu.setText(swarnabulu);
                        tumur.setText(sumur);
                        tttl.setText(sttl);
                        /*talamathewan.setText(salamathewan);*/
                        FirebaseDatabase.getInstance().getReference().child("RiwayatPenyakit").child(id_pasien).child(idriwayatpenyakit).addValueEventListener(new ValueEventListener() {
                            @Override
                            public void onDataChange(DataSnapshot dataSnapshot) {
                                namadokter_periksa = dataSnapshot.child("namadokter_periksa").getValue().toString();
                                tanggalperiksa = dataSnapshot.child("tanggal_periksa").getValue().toString();
                                diagnosa = dataSnapshot.child("penyakit_di_derita").getValue().toString();
                                getSterapi = dataSnapshot.child("terapi").getValue().toString();

                                txtdokterperiksa.setText(namadokter_periksa);
                                txttanggalperiksa.setText(tanggalperiksa);
                                txtdiagnosapenyakit.setText(diagnosa);
                                txtterapipenyakit.setText(getSterapi);


                            }

                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });



                        kembali.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(RiwayatPenyakit.this,ListVerivikasiHewanBerobat.class);
                                intent.putExtra("user_id", id_pasien);
                                startActivity(intent);
                                finish();
                            }
                        });




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
