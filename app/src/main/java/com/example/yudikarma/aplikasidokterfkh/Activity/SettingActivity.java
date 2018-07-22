package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Callback;
import com.squareup.picasso.NetworkPolicy;
import com.squareup.picasso.Picasso;
import com.theartofdev.edmodo.cropper.CropImage;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.util.HashMap;
import java.util.Map;

import de.hdodenhof.circleimageview.CircleImageView;
import id.zelory.compressor.Compressor;

import com.example.yudikarma.aplikasidokterfkh.R;

public class SettingActivity extends AppCompatActivity {
    private DatabaseReference mDatabaseReference;
    private DatabaseReference mUsercampurDatabase;
    private FirebaseUser mCurrentuser;

    private static final int GALLERY_PICK = 1;

    //Android Layout
    private CircleImageView mCircleImageView;
    private TextView mDisplayname;
    private TextView mstatus;
    private TextView jumlahteman,jumlahrekammedis,email_setting,nohp_setting,address_setting;
    private FloatingActionButton btn_changeimage_setting;
    private ImageView btn_changestatus_settinng;
    private ProgressDialog mProgressDialog;

    private Compressor compressedImageBitmap;

    //Firebase
    private StorageReference mStorageReference;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);

        mCircleImageView = (CircleImageView) findViewById(R.id.circleImageView);
        mDisplayname = (TextView) findViewById(R.id.display_name_setting);
        mstatus = findViewById(R.id.tstatus);
        jumlahteman = findViewById(R.id.jumlahteman);
        jumlahrekammedis = findViewById(R.id.jumlahrequest);
        email_setting = findViewById(R.id.email_setting);
        nohp_setting = findViewById(R.id.nohp_setting);
        address_setting = findViewById(R.id.address_settting);
        mProgressDialog = new ProgressDialog(this);
        btn_changeimage_setting = (FloatingActionButton) findViewById(R.id.change_image_setting);
        mStorageReference = FirebaseStorage.getInstance().getReference();

        mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = mCurrentuser.getUid();
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dokters").child(uid);
        mUsercampurDatabase = FirebaseDatabase.getInstance().getReference().child("UserCampur").child(uid);
        mDatabaseReference.keepSynced(true);
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                String display_name = dataSnapshot.child("name").getValue().toString();
               final String status = dataSnapshot.child("status").getValue().toString();
                final String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String no_hp = dataSnapshot.child("no_hp").getValue().toString();
                String email = dataSnapshot.child("email").getValue().toString();
                email_setting.setText(email);
                nohp_setting.setText(no_hp);
                address_setting.setText(address);

                mDisplayname.setText(display_name);
                mstatus.setText(status);
                btn_changestatus_settinng = findViewById(R.id.changestatus_settinng);
                btn_changestatus_settinng.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(SettingActivity.this,ChangeStatus.class);
                        intent.putExtra("iduser", uid);
                        intent.putExtra("status", status);
                        /*intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);*/
                        startActivity(intent);

                    }
                });
                if (!image.equals("default")) {
                    Picasso.with(SettingActivity.this).load(image).networkPolicy(NetworkPolicy.OFFLINE)
                            .placeholder(R.drawable.default_avatar).into(mCircleImageView, new Callback() {
                        @Override
                        public void onSuccess() {

                        }

                        @Override
                        public void onError() {
                            Picasso.with(SettingActivity.this).load(image).placeholder(R.drawable.default_avatar).into(mCircleImageView);

                        }
                    });

                } else {
                    Picasso.with(SettingActivity.this).load(R.drawable.default_avatar).into(mCircleImageView);

                }
                FirebaseDatabase.getInstance().getReference().child("Friends").child(uid).addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        Long jumlah = dataSnapshot.getChildrenCount();
                        jumlahteman.setText(""+jumlah);

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
                FirebaseDatabase.getInstance().getReference().child("Friend_request").child(uid).addValueEventListener(new ValueEventListener() {
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

        //onclik image change
        btn_changeimage_setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent galery_intent = new Intent();
                galery_intent.setType("image/*");
                galery_intent.setAction(Intent.ACTION_GET_CONTENT);

                startActivityForResult(Intent.createChooser(galery_intent, "Select Image"), GALLERY_PICK);
            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == GALLERY_PICK && resultCode == RESULT_OK) {
            Uri url = data.getData();
            CropImage.activity(url)
                    .setAspectRatio(1, 1)
                    .start(SettingActivity.this);
            //Toast.makeText(SettingActivity.this,url,Toast.LENGTH_SHORT).show();
        }
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE) {
            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            if (resultCode == RESULT_OK) {
                mProgressDialog.setTitle("Change Profil Images");
                mProgressDialog.setMessage("we wil try change your profil images");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();

                Uri resultUri = result.getUri();
                mCurrentuser = FirebaseAuth.getInstance().getCurrentUser();
                String uid = mCurrentuser.getUid();

                File thum_file_path = new File(resultUri.getPath());


                Bitmap thum_bitmap = new Compressor(this)
                        .setMaxHeight(200)
                        .setMaxWidth(200)
                        .setQuality(75)
                        .compressToBitmap(thum_file_path);

                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                thum_bitmap.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                final byte[] thumb_byte = baos.toByteArray();

                StorageReference filepath = mStorageReference.child("Profile_images").child(uid + "Profile_images.jpg");
                final StorageReference thumb_filepath = mStorageReference.child("Profile_images").child("thumbnails_Profile_images").child(uid + "thumbs_profil_images.jpg");
                filepath.putFile(resultUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                        if (task.isSuccessful()) {
                            //Toast.makeText(SettingActivity.this,"working",Toast.LENGTH_LONG).show();
                            final String downloadUri = task.getResult().getDownloadUrl().toString();

                            //upload task thumb
                            UploadTask uploadTask = thumb_filepath.putBytes(thumb_byte);
                            uploadTask.addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> thumb_task) {
                                    String doanload_thumb_uri = thumb_task.getResult().getDownloadUrl().toString();
                                    if (thumb_task.isSuccessful()) {

                                        //MAP untuk upload
                                        Map update_hasmap = new HashMap();
                                        update_hasmap.put("image", downloadUri);
                                        update_hasmap.put("thumb_image", doanload_thumb_uri);

                                        final Map updateHasMapUserCampur = new HashMap();
                                        updateHasMapUserCampur.put("image", downloadUri);
                                        updateHasMapUserCampur.put("thumb_image", doanload_thumb_uri);
                                        mDatabaseReference.updateChildren(update_hasmap).addOnCompleteListener(new OnCompleteListener<Void>() {
                                            @Override
                                            public void onComplete(@NonNull final Task<Void> task) {
                                                if (task.isSuccessful()) {
                                                    mUsercampurDatabase.updateChildren(updateHasMapUserCampur, new DatabaseReference.CompletionListener() {
                                                        @Override
                                                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                                                            if (task.isSuccessful()) {
                                                                mProgressDialog.dismiss();
                                                                Toast.makeText(SettingActivity.this, "Succes Upload", Toast.LENGTH_LONG).show();
                                                            }else{
                                                                mProgressDialog.dismiss();
                                                                Toast.makeText(SettingActivity.this, "Upload Error", Toast.LENGTH_LONG).show();
                                                            }
                                                        }
                                                    });

                                                }
                                            }
                                        });
                                    } else {
                                        Toast.makeText(SettingActivity.this, "Error upload thumb", Toast.LENGTH_LONG).show();
                                        mProgressDialog.dismiss();
                                    }
                                }
                            });

                        } else {
                            Toast.makeText(SettingActivity.this, "Error upload", Toast.LENGTH_LONG).show();
                            mProgressDialog.dismiss();
                        }

                    }
                });
            } else if (resultCode == CropImage.CROP_IMAGE_ACTIVITY_RESULT_ERROR_CODE) {
                mProgressDialog.hide();
                Exception error = result.getError();
            }
        }
    }
}
