package com.example.simplechatapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.hbb20.CountryCodePicker;

public class MainActivity extends AppCompatActivity {

    EditText phone;
    Button sentotp;
    CountryCodePicker ccp;
    private FirebaseAuth mauth;
    protected void onStart()
    {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent intent =new Intent(this,ChatActiviy.class);
            startActivity(intent)
            ;
            finish();
        }
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        getSupportActionBar().hide();
        phone =findViewById(R.id.phone);
        sentotp=findViewById(R.id.otpsend);

        ccp=findViewById(R.id.ccp);
        mauth=FirebaseAuth.getInstance();
//        FirebaseUser user=mauth.getCurrentUser();

        sentotp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if(phone.toString().isEmpty())
                {
                    Toast.makeText(getApplicationContext(), "enter phone number", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, ManageOtp.class);

                    String number = phone.getText().toString();
                    intent.putExtra("mobile", "+91" + number);
//                intent.putExtra("mobile",ccp.getFullNumberWithPlus().toString());
                    startActivity(intent);
                    finish();
                }
            }
        });



    }
}