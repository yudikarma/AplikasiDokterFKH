package com.example.yudikarma.aplikasidokterfkh.Activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.yudikarma.aplikasidokterfkh.Model.Users;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.example.yudikarma.aplikasidokterfkh.R;

public class LoginActivity extends AppCompatActivity {

    /**
     * Id to identity READ_CONTACTS permission request.
     */
    private static final int REQUEST_READ_CONTACTS = 0;

    /**
     * A dummy authentication store containing known user names and passwords.
     * TODO: remove after connecting to a real authentication system.
     */

    /**
     * Keep track of the login task to ensure we can cancel it if requested.
     */


    // UI references.
    private EditText mEmailView;
    private EditText mPasswordView;
    private View mProgressView;
    private View mLoginFormView;
    private LinearLayout regiSt;
    private Button mEmailSignInButton;
    private ProgressDialog mpProgressDialog;

    //Firebase
    private FirebaseAuth mAuth;
    //firebase user database
    private DatabaseReference muserDatabaseReference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // Set up the login form.
        mEmailView = (EditText) findViewById(R.id.email);
        mPasswordView = (EditText) findViewById(R.id.password);


        regiSt =  findViewById(R.id.tvRegister);
        mpProgressDialog = new ProgressDialog(this);


        //firebase
        muserDatabaseReference = FirebaseDatabase.getInstance().getReference().child("Users").child("Dokters");
        mAuth = FirebaseAuth.getInstance();
        mEmailSignInButton = (Button) findViewById(R.id.email_sign_in_button);
        mEmailSignInButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                final String email = mEmailView.getText().toString();
                final String password = mPasswordView.getText().toString();
                if (!TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    mpProgressDialog.setTitle("login..");
                    mpProgressDialog.setMessage("we are try connect your acount..");
                    mpProgressDialog.setCanceledOnTouchOutside(false);
                    mpProgressDialog.show();

                    muserDatabaseReference.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            if (dataSnapshot.exists()){

                                for (DataSnapshot dataSnapshot1 : dataSnapshot.getChildren()){
                                    Users users = dataSnapshot1.getValue(Users.class);
                                    if (users.getEmail().equals(email)){
                                        loginUser(email, password);
                                    }else{
                                        mpProgressDialog.hide();
                                        Toast.makeText(LoginActivity.this, "Not found User", Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }else{
                                mpProgressDialog.hide();
                                Toast.makeText(LoginActivity.this, "Database not exies", Toast.LENGTH_SHORT).show();

                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {
                            mpProgressDialog.hide();
                            Toast.makeText(LoginActivity.this, "Database Error", Toast.LENGTH_SHORT).show();

                        }
                    });

                }else {
                    mpProgressDialog.hide();
                    Toast.makeText(LoginActivity.this,"please insert your email and password login",Toast.LENGTH_SHORT).show();
                }

            }
        });

        regiSt.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(i);
               // finish();
            }
        });



    }
    private void loginUser(String email, String password) {
        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){

                    String device_token = FirebaseInstanceId.getInstance().getToken();
                    String current_user_id = mAuth.getCurrentUser().getUid();
                    muserDatabaseReference.child(current_user_id).child("device_token").setValue(device_token).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void aVoid) {
                            mpProgressDialog.dismiss();
                            Intent intent = new Intent(LoginActivity.this,MainActivity.class);
                            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            startActivity(intent);
                            finish();
                        }
                    });


                }else{
                    mpProgressDialog.hide();
                    Toast.makeText(LoginActivity.this,"Something error !!!",Toast.LENGTH_SHORT).show();
                }
            }
        });

    }
}
