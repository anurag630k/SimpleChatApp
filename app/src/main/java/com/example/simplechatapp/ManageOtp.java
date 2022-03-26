package com.example.simplechatapp;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthOptions;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class ManageOtp extends AppCompatActivity {
    EditText otp;
    Button btn;
    TextView phone;
    String msg;
    String otpid;
    FirebaseAuth auth;
    ProgressBar mprogress;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_otp);

        getSupportActionBar().hide();
        otp=findViewById(R.id.otp);
        btn=findViewById(R.id.button);
        phone=findViewById(R.id.shownumber);
        mprogress=findViewById(R.id.progressbar);
        Intent i=getIntent();
        msg=i.getStringExtra("mobile");
        phone.setText(msg);
//        Toast.makeText(ManageOtp.this, "in otp activity  number is ,OTp has been sent", Toast.LENGTH_LONG).show();
        auth = FirebaseAuth.getInstance();
        initiateotp();

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(otp.getText().toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "blank field", Toast.LENGTH_SHORT).show();
                }
                else if(otp.getText().toString().length()!=6)
                {
                    Toast.makeText(getApplicationContext(), "Invalid otp", Toast.LENGTH_SHORT).show();
                }
                else
                {
                    mprogress.setVisibility(View.VISIBLE);

                    PhoneAuthCredential credential=PhoneAuthProvider.getCredential(otpid,otp.getText().toString());
                    signInWithPhoneAuthCredential(credential);

                }
            }
        });
//            if(otp.getText().toString().isEmpty())
//            {
//                Toast.makeText(getApplicationContext(), "blank field", Toast.LENGTH_SHORT).show();
//            }
//            else if(otp.getText().toString().length()!=6)
//            {
//                Toast.makeText(getApplicationContext(), "Invalid otp", Toast.LENGTH_SHORT).show();
//            }
//            else
//            {
//                PhoneAuthCredential=PhoneAuthProvider.getCredential((otpid,otp,getText().toString()));
//                signInWithPhoneAuthCredential();
//              }


    }

//    FirebaseAuth auth = FirebaseAuth.getInstance();



    public void initiateotp()
    {
        PhoneAuthOptions options =
                PhoneAuthOptions.newBuilder(auth)
                        .setPhoneNumber(msg)       // Phone number to verify
                        .setTimeout(60L, TimeUnit.SECONDS) // Timeout and unit
                        .setActivity(this)                 // Activity (for callback binding)
                        .setCallbacks (new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                            @Override
                            public void onCodeSent(String verificationId, PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                                // Save the verification id somewhere
                                // ...
                                otpid=verificationId;
                                // The corresponding whitelisted code above should be used to complete sign-in.
//                                ManageOtp.this.enableUserManuallyInputCode();
                            }

                            @Override
                            public void onVerificationCompleted(PhoneAuthCredential phoneAuthCredential) {

                                signInWithPhoneAuthCredential(phoneAuthCredential);
                                // Sign in with the credential
                                // ...
                            }

                            @Override
                            public void onVerificationFailed(FirebaseException e) {
                                Toast.makeText(getApplicationContext(), "Error occured"+e.getMessage(), Toast.LENGTH_LONG).show();
                                // ...
                            }
                        })          // OnVerificationStateChangedCallbacks
                        .build();
        PhoneAuthProvider.verifyPhoneNumber(options);


//        (new PhoneAuthProvider.OnVerificationStateChangedCallbacks()


    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {
        auth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
                            if(user!=null)
//                            {
////                                FirebaseDatabase muserdb=FirebaseDatabase.getInstance();
//                                final DatabaseReference mmuserdb=FirebaseDatabase.getInstance().getReference().child("user").child(user.getUid());
//                               mmuserdb.addListenerForSingleValueEvent(new ValueEventListener() {
//                                   @Override
//                                   public void onDataChange(@NonNull DataSnapshot snapshot) {
//                                       if(!snapshot.exists())
//                                       {
//                                           Map<String,Object>usermap=new HashMap<>() ;
////                                           usermap.put("name","kumar");
//
//                                           usermap.put("phone",user.getPhoneNumber());
////                                           usermap.put("name","kumar");
//                                           mmuserdb.updateChildren(usermap);
//
//                                       }
//                                   }
//
//                                   @Override
//                                   public void onCancelled(@NonNull DatabaseError error) {
//
//                                   }
//                               });
//                            }


                                Toast.makeText(ManageOtp.this, "Successfull", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(ManageOtp.this,SetProfile.class));
                            finish();
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");

//                            FirebaseUser user = task.getResult().getUser();
                            // Update UI
                        } else {
                            Toast.makeText(ManageOtp.this, "signin failed", Toast.LENGTH_LONG).show();
                            // Sign in failed, display a message and update the UI
//                            Log.w(TAG, "signInWithCredential:failure", task.getException());

                        }
                    }
                });
    }


}



