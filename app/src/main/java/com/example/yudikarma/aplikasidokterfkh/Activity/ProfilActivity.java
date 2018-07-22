package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.app.ProgressDialog;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.yudikarma.aplikasidokterfkh.R;

import de.hdodenhof.circleimageview.CircleImageView;

public class ProfilActivity extends AppCompatActivity {
    TextView profil_displayname,profil_status,profil_totalfriend;
    CircleImageView profil_image;
    Button btn_sendrequest;

    private ProgressDialog mProgressDialog;

    //FIrebase database for user information
    private DatabaseReference mDatabaseReference;

    //firebase database for request friend
    private DatabaseReference mRequestDatabaseReference;

    //firebase database to get userid userlogin or user add friend
    private FirebaseUser mFirebaseUser;

    //firebase database for Friends
    private DatabaseReference mFriendsDatabaseReference;

    //Firebase database for Notification
    private DatabaseReference mNotificationDatabaseReference;

    //Firebase database for Notification
    private DatabaseReference mRootDatabaseReference;

    //string for status friend, "send","accep","friend",
    private String mCurrentState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        //get userid onclick user or user yang diklik
        final String userid = getIntent().getStringExtra("user_id");
        //default status friend
        mCurrentState = "not_friend";

        //inisialisasi firebase
        mDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Pasien").child(userid);   //firebase database user
        mRequestDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friend_request");  //firebase database reqfriend
        mFriendsDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Friends");             //inisial database for friend
        mNotificationDatabaseReference = FirebaseDatabase.getInstance().getReference().child("notifications"); //firebase database inisial for notification
        mRootDatabaseReference = FirebaseDatabase.getInstance().getReference();
        mFirebaseUser = FirebaseAuth.getInstance().getCurrentUser();

        //layout
        profil_displayname = (TextView) findViewById(R.id.profil_displayname);
        profil_status = (TextView) findViewById(R.id.profil_satus);
        profil_totalfriend = (TextView) findViewById(R.id.profil_totalfriend);
        profil_image =  findViewById(R.id.profil_image);
        btn_sendrequest = (Button) findViewById(R.id.profil_btn_sendrequest);
       /* btn_declineFriend = (Button) findViewById(R.id.profil_btn_decline);
        btn_declineFriend.setVisibility(View.INVISIBLE);
        btn_declineFriend.setEnabled(false);*/
        mProgressDialog = new ProgressDialog(ProfilActivity.this);
        mProgressDialog.setTitle("loading user data");
        mProgressDialog.setMessage("please wait..");
        mProgressDialog.setCanceledOnTouchOutside(false);
        mProgressDialog.show();

        //User onclick information
        //firebase information user
        mDatabaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                //get data from firebase
                String display_name = dataSnapshot.child("name").getValue().toString();
                String status = dataSnapshot.child("status").getValue().toString();
                String image = dataSnapshot.child("image").getValue().toString();
                String thumb_image = dataSnapshot.child("thumb_image").getValue().toString();
                String address = dataSnapshot.child("address").getValue().toString();
                String no_hp = dataSnapshot.child("no_hp").getValue().toString();

                //Display Information
                profil_displayname.setText(display_name);
                profil_status.setText(status);
                Picasso.with(ProfilActivity.this).load(image).placeholder(R.drawable.colapsing_bacground).into(profil_image);

                // ==================== FRIEND LIST / Request Feature =====//

                if(mFirebaseUser.getUid().equals(userid)){

                   /* btn_declineFriend.setEnabled(false);
                    btn_declineFriend.setVisibility(View.INVISIBLE);*/

                    btn_sendrequest.setEnabled(false);
                    btn_sendrequest.setVisibility(View.INVISIBLE);

                }

                //firebase database request friend
                mRequestDatabaseReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() { //selection child user.getuid();
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        //Menampilkan informasi load infromasi user, apakah statusnya send request = btn.setttext("cencel friend request"), // dan accep friend;
                        if (dataSnapshot.hasChild(userid)){
                            String req_type = dataSnapshot.child(userid).child("request_type").getValue().toString();
                            if (req_type.equals("received")){
                                mCurrentState = "req_received";
                                btn_sendrequest.setText("Accept Friend Request");
                              /*  btn_declineFriend.setVisibility(View.VISIBLE);
                                btn_declineFriend.setEnabled(true);*/

                            }else if (req_type.equals("send")){
                                mCurrentState = "req_send";
                                btn_sendrequest.setText("Cancel Request Friend");
                               /* btn_declineFriend.setVisibility(View.INVISIBLE);
                                btn_declineFriend.setEnabled(false);*/
                            }
                            mProgressDialog.dismiss();
                        }else{
                            mFriendsDatabaseReference.child(mFirebaseUser.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(DataSnapshot dataSnapshot) {
                                    if (dataSnapshot.hasChild(userid)){
                                        mCurrentState = "friend";
                                        btn_sendrequest.setText("Unfriend this person");
                                      /*  btn_declineFriend.setVisibility(View.INVISIBLE);
                                        btn_declineFriend.setEnabled(false);*/
                                    }
                                    mProgressDialog.dismiss();
                                }

                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                    mProgressDialog.dismiss();

                                }
                            });
                        }

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


        //=========ONclick SEND REQUEST FRIEND ===///

        btn_sendrequest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View view) {
                btn_sendrequest.setEnabled(false);
               /* btn_declineFriend.setVisibility(View.INVISIBLE);
                btn_declineFriend.setEnabled(false);*/
                mProgressDialog.setTitle("wait a moment..");
                mProgressDialog.setCanceledOnTouchOutside(false);
                mProgressDialog.show();


                // ===== IF NOT FRIEND STATE
                if (mCurrentState.equals("not_friend")){

                    DatabaseReference newNotificationRef = mRootDatabaseReference.child("notifications").child(userid).push();
                    String newNotificationID = newNotificationRef.getKey();

                    HashMap<String,String> notificationdata = new HashMap<>();
                    notificationdata.put("from",mFirebaseUser.getUid());
                    notificationdata.put("type","request");

                    Map requestMap = new HashMap();
                    requestMap.put("Friend_request/"+mFirebaseUser.getUid()+"/"+userid+"/request_type", "send");
                    requestMap.put("Friend_request/"+userid+"/"+mFirebaseUser.getUid()+"/request_type", "received");
                    requestMap.put("notifications/"+userid+"/"+newNotificationID, notificationdata);

                    mRootDatabaseReference.updateChildren(requestMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError != null){
                                Toast.makeText(ProfilActivity.this, "there was some error", Toast.LENGTH_LONG).show();

                            }
                            btn_sendrequest.setEnabled(true);
                            mCurrentState = "req_send";
                            btn_sendrequest.setText("Cancel Friend Request");
                            mProgressDialog.dismiss();

                        }
                    });


                }
                //============ IF CANCEL FRIEND REQUEST STATE//

                if (mCurrentState.equals("req_send")){
                    mRequestDatabaseReference.child(mFirebaseUser.getUid()).child(userid).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mRequestDatabaseReference.child(userid).child(mFirebaseUser.getUid()).removeValue().addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    mProgressDialog.dismiss();
                                    mCurrentState = "not_friend";
                                    btn_sendrequest.setText("Send Friend Request");
                                   /* btn_declineFriend.setVisibility(View.INVISIBLE);
                                    btn_declineFriend.setEnabled(false);*/
                                    btn_sendrequest.setEnabled(true);
                                }
                            });
                        }
                    });
                }

                //============ IF state is RECEIVED or ACCEP REQUEST ==========//

                if (mCurrentState.equals("req_received")){

                    final String currentDate = DateFormat.getDateTimeInstance().format(new Date());
                    Map friendsMap = new HashMap();
                    friendsMap.put("Friends/"+mFirebaseUser.getUid()+"/"+userid+"/date",currentDate);
                    friendsMap.put("Friends/"+userid+"/"+mFirebaseUser.getUid()+"/date", currentDate);

                    friendsMap.put("Friend_request/"+mFirebaseUser.getUid()+"/"+userid,null );
                    friendsMap.put("Friend_request/"+userid+"/"+mFirebaseUser.getUid(), null);

                    mRootDatabaseReference.updateChildren(friendsMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null){
                                btn_sendrequest.setEnabled(true);
                                mCurrentState = "friend";
                                btn_sendrequest.setText("Unfriend This person");
/*
                                btn_declineFriend.setVisibility(view.INVISIBLE);
                                btn_declineFriend.setEnabled(false);*/
                                mProgressDialog.dismiss();
                            }else{
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfilActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                        }
                    });


                }
                //============ IF UNFRIEND FRIEND REQUEST STATE//

                if (mCurrentState.equals("friend")){
                    Map unfriendMap = new HashMap();
                    unfriendMap.put("Friends/"+mFirebaseUser.getUid()+"/"+userid,null );
                    unfriendMap.put("Friends/"+userid+"/"+mFirebaseUser.getUid(), null);

                    mRootDatabaseReference.updateChildren(unfriendMap, new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {
                            if (databaseError == null){

                                mCurrentState = "not_friend";
                                btn_sendrequest.setText("Send Friends Request");

                               /* btn_declineFriend.setVisibility(view.INVISIBLE);
                                btn_declineFriend.setEnabled(false);*/
                                mProgressDialog.dismiss();
                            }else{
                                String error = databaseError.getMessage();
                                Toast.makeText(ProfilActivity.this, error, Toast.LENGTH_LONG).show();
                            }
                            btn_sendrequest.setEnabled(true);
                        }
                    });


                }
            }
        });

       /* btn_declineFriend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Map declineMap = new HashMap();

                declineMap.put("Friend_req/" + mFirebaseUser.getUid() + "/" + userid, null);
                declineMap.put("Friend_req/" + userid + "/" + mFirebaseUser.getUid(), null);

                mRootDatabaseReference.updateChildren(declineMap, new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError, DatabaseReference databaseReference) {

                        if(databaseError == null)
                        {

                            mCurrentState = "not_friend";
                            btn_sendrequest.setText("Send Friend Request");

                            //mDiclineBnt.setVisibility(View.INVISIBLE);
                            //mDiclineBnt.setEnabled(false);
                        }else{
                            String error = databaseError.getMessage();
                            Toast.makeText(ProfilActivity.this, error, Toast.LENGTH_LONG).show();
                        }

                        btn_sendrequest.setEnabled(true);
                    }
                });
            }
        });
*/

    }

            }
