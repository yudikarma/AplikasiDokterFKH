package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudikarma.aplikasidokterfkh.R;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class DetailVerifikasi extends AppCompatActivity {

    private TextView tnamapemilik,tnohp,tnamadokter,tgejala,tdugaan,ttanggalrekam,talamatpemilik,tnamahewan,tjenishewan,tjeniskelamin,tras,twarnabulu,tumur,tttl,talamathewan;
    private DatabaseReference mRootDatabaseReference;
    private TextInputEditText penyakitdiderita,terapi;
    private Button simpan,kembali;
    private String sterapi,spenyakitdiderita;
    private LinearLayout lpenyakit,lterapi;
    private TextView diisisaat;
    private Toolbar toolbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail_verifikasi);
/*        getSupportActionBar().setDisplayHomeAsUpEnabled(true);*/
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
        talamathewan = findViewById(R.id.dalamat);
        lpenyakit = findViewById(R.id.lpenyakitdiderita);
        lterapi = findViewById(R.id.lterapi);
        lpenyakit.setVisibility(View.GONE);
        lterapi.setVisibility(View.GONE);
        simpan.setVisibility(View.GONE);
        diisisaat = findViewById(R.id.isisaarpemeriksaan);
        diisisaat.setVisibility(View.GONE);

        final String idrekammedis = getIntent().getStringExtra("id_rekam_medis");
        final  String id_pasien = getIntent().getStringExtra("id_pasien");

        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DetailVerifikasi.this,ListVerivikasiHewanBerobat.class);
                intent.putExtra("user_id", id_pasien);
                startActivity(intent);
                finish();

            }
        });






        FirebaseDatabase.getInstance().getReference().child("RekamMedis").child(id_pasien).child(idrekammedis).addValueEventListener(new ValueEventListener() {
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
                        String snamahewan = dataSnapshot.child("nama_hewan").getValue().toString();
                        String sjenishewan = dataSnapshot.child("jenis_hewan").getValue().toString();
                        String sjeniskelamin = dataSnapshot.child("jenis_lk").getValue().toString();
                        String sras = dataSnapshot.child("ras").getValue().toString();
                        String swarnabulu = dataSnapshot.child("warna_bulu").getValue().toString();
                        String sumur = dataSnapshot.child("umur").getValue().toString();
                        String sttl = dataSnapshot.child("ttl").getValue().toString();
                        String salamathewan = dataSnapshot.child("alamat").getValue().toString();

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
                        talamathewan.setText(salamathewan);

                        if (terapi2.equals("default")) {

                            lpenyakit.setVisibility(View.VISIBLE);
                            lterapi.setVisibility(View.VISIBLE);
                            simpan.setVisibility(View.VISIBLE);
                            diisisaat.setVisibility(View.VISIBLE);


                            simpan.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    spenyakitdiderita = penyakitdiderita.getText().toString();
                                    sterapi = terapi.getText().toString();

                                    if (!spenyakitdiderita.isEmpty() && !sterapi.isEmpty()) {


                                        DatabaseReference newNotificationRef = mRootDatabaseReference.child("RiwayatPenyakit").child(id_pasien).push();
                                        String idriwayatpenyakit = newNotificationRef.getKey();

                                        HashMap<String, String> riwayatpenyakit = new HashMap<>();
                                        riwayatpenyakit.put("nama_pemilik", snamapemilik);
                                        riwayatpenyakit.put("no_hp", snohp);
                                        riwayatpenyakit.put("nama_dokter", snamadokter);
                                        riwayatpenyakit.put("gejala", sgejala);
                                        riwayatpenyakit.put("dugaan", sdugaan);
                                        riwayatpenyakit.put("alamat_pemilik", salamatpemilik);
                                        riwayatpenyakit.put("tanggal_rekam", stanggalrekam);
                                        riwayatpenyakit.put("idhewan", idhewan);
                                        riwayatpenyakit.put("penyakit_di_derita", spenyakitdiderita);
                                        riwayatpenyakit.put("terapi", sterapi);
                                        riwayatpenyakit.put("tanggal_periksa", currentDate);

                                        Map pussMap = new HashMap();
                                        Map simpanriwayatpenyakit = new HashMap();
                                    /*simpanriwayatpenyakit.put("RekamMedis/"+id_pasien+"/"+idrekammedis, null);*
                                    //BIKIN ERROR LISTVERIVIKASIHEWAN BEROBAT wkwkkwk
                                     */
                                        simpanriwayatpenyakit.put("RekamMedis/" + id_pasien + "/" + idrekammedis, riwayatpenyakit);
                                        simpanriwayatpenyakit.put("RiwayatPenyakit/" + id_pasien + "/" + idriwayatpenyakit, riwayatpenyakit);



                                        mRootDatabaseReference.updateChildren(simpanriwayatpenyakit, new DatabaseReference.CompletionListener() {
                                            @Override
                                            public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                if (databaseError == null) {
                                               /* ListVerivikasiHewanBerobat listVerivikasiHewanBerobat = new ListVerivikasiHewanBerobat();
                                                listVerivikasiHewanBerobat.remove();*/
                                                    /* FirebaseDatabase.getInstance().getReference().child("RekamMedis").child(id_pasien).child(idrekammedis).getRef().removeValue();*/
                                                    Toast.makeText(DetailVerifikasi.this, "Berhasil Menyimpan Riwayat Penyakit", Toast.LENGTH_SHORT).show();
                                                    Intent intent = new Intent(DetailVerifikasi.this, MainActivity.class);
                                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                    startActivity(intent);
                                                    finish();

                                                } else {
                                                    String error = databaseError.getMessage();
                                                    Toast.makeText(DetailVerifikasi.this, error, Toast.LENGTH_LONG).show();
                                                }


                                            }
                                        });

                                    } else {
                                        Toast.makeText(DetailVerifikasi.this, "Semua field harus di isi", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                        }else{

                        }

                        kembali.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(DetailVerifikasi.this,ListVerivikasiHewanBerobat.class);
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
