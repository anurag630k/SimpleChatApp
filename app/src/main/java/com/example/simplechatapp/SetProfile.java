package com.example.simplechatapp;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.ByteArrayOutputStream;
import java.io.IOException;

public class SetProfile extends AppCompatActivity {

    private static  int pick = 1;
    private ImageView userprofilepic;
    private EditText username;
    private Button save_profile;
    private Uri imagepath;
    private FirebaseAuth mauth;


    private FirebaseDatabase database;
    private DatabaseReference mdataref;

    private StorageReference mstorageref;
    private FirebaseStorage firebaseStorage;


    private FirebaseFirestore mfirestore;

    private String name;
    private String imageuritoken;

    private ProgressBar mprogressbar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_profile);

        username = findViewById(R.id.setname);
        save_profile = findViewById(R.id.saveprofile);
        mprogressbar = findViewById(R.id.progressbar);
        userprofilepic=findViewById(R.id.setuserprofilepic);
//        database=FirebaseDatabase.getInstance();

        getSupportActionBar().hide();;
        mauth = FirebaseAuth.getInstance();
        firebaseStorage = FirebaseStorage.getInstance();
        mfirestore = FirebaseFirestore.getInstance();
        userprofilepic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.INTERNAL_CONTENT_URI);

                i.setType("image/*");
                startActivityForResult(i, pick);
            }
        });


        save_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                name = username.getText().toString();
                if (name.isEmpty()) {
                    username.setError("Type a name");
//                    Toast.makeText(getApplicationContext(), "name is empty ", Toast.LENGTH_LONG).show();

                }
//                else if(imagepath.equals(null))
//                {
//                    Toast.makeText(getApplicationContext(),"image is empty ",Toast.LENGTH_LONG).show();
//                }

                else {
                    mprogressbar.setVisibility(View.VISIBLE);
                    senddatatodatabase();

                    startActivity(new Intent(getApplicationContext(),ChatActiviy.class));
//                    Toast.makeText(getApplicationContext(), "Saved your profile", Toast.LENGTH_SHORT).show();
                 finishAffinity();

                }
            }
        });


    }

    private void senddatatodatabase() {
        database= FirebaseDatabase.getInstance();
          mdataref = database.getReference(mauth.getUid());
        String uid=mauth.getUid().toString();
        String phonenumber=mauth.getCurrentUser().getPhoneNumber().toString();
        UserProfile userdata=new UserProfile(name,uid,phonenumber);
//        Toast.makeText(getApplicationContext(), phonenumber+ "   mobile   "+ mauth.getCurrentUser().getPhoneNumber(), Toast.LENGTH_SHORT).show();
        mdataref.setValue(userdata);

//        Toast.makeText(this, "uid "+mauth.getUid().toString(), Toast.LENGTH_SHORT).show();
        mstorageref=firebaseStorage.getReference().child("Photos").child(mauth.getUid()).child("ProfilePic");

//        Toast.makeText(this, "uid "+mauth.getUid().toString(), Toast.LENGTH_SHORT).show();

        //image to storage

        Bitmap bitmap=null;
        try {
            bitmap =MediaStore.Images.Media.getBitmap(getContentResolver(),imagepath);

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }



        ByteArrayOutputStream byteArrayOutputStream=new ByteArrayOutputStream();
        ( bitmap).compress(Bitmap.CompressFormat.JPEG,30,byteArrayOutputStream);
        byte[] data=byteArrayOutputStream.toByteArray();

        UploadTask uploadTask=mstorageref.putBytes(data);
//        UploadTask uploadTask1=mstorageref.putFile(imagepath);

        Toast.makeText(this, "send data ", Toast.LENGTH_SHORT).show();

        uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
                mstorageref.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(@NonNull Uri uri) {
                        imageuritoken=uri.toString();
                        Toast.makeText(getApplicationContext(), "Uri success..", Toast.LENGTH_SHORT).show();
                        senddatatocloud();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getApplicationContext(), "uri failed", Toast.LENGTH_SHORT).show();
                    }
                });
//                Toast.makeText(getApplicationContext(), "image uploaded", Toast.LENGTH_SHORT).show();



            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getApplicationContext(), "Image not uploaded", Toast.LENGTH_SHORT).show();
            }
        });




//        mdataref.setValue(name);

//        mdataref.child("username").setValue(name);



    }


    private void senddatatocloud() {
//        Toast.makeText(getApplicationContext(), "Inside senddatacloud ", Toast.LENGTH_SHORT).show();
        DocumentReference documentReference=mfirestore.collection("Users").document(mauth.getUid());


        ChatView user = new ChatView();
//        Map<String,Object> userdata=new HashMap<>();
//        userdata.put("name",name);
//
//        userdata.put("image",imageuritoken);
//        userdata.put("uid",mauth.getUid());
//        userdata.put("status","online");
        user.setImage(imageuritoken);
        user.setName(name);
        user.setStatus("time");
        user.setUid(mauth.getUid().toString());
//        Toast.makeText(this, "send data to cloud", Toast.LENGTH_SHORT).show();


        documentReference.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(@NonNull Void unused) {
                Toast.makeText(getApplicationContext(), "Data uploaded on cloud storage", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getApplicationContext(), "Data not uploaded on google...", Toast.LENGTH_SHORT).show();
            }
        });






    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        if(requestCode==pick && resultCode==RESULT_OK)
        {
            imagepath=data.getData();
            userprofilepic.setImageURI(imagepath);

            Toast.makeText(this, "Image is choosen", Toast.LENGTH_SHORT).show();
//            mstorageref=firebaseStorage.getReference("photos").child(imagepath.getLastPathSegment());
//
//
//
//            mstorageref.putFile(imagepath).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                @Override
//                public void onSuccess(@NonNull UploadTask.TaskSnapshot taskSnapshot) {
//                    Toast.makeText(getApplicationContext(), "Upload done", Toast.LENGTH_SHORT).show();
//
//                }
//            }).addOnFailureListener(new OnFailureListener() {
//                @Override
//                public void onFailure(@NonNull Exception e) {
//                    Toast.makeText(getApplicationContext(), "Failed", Toast.LENGTH_SHORT).show();
//                }
//            });


        }
        else
        {

            Toast.makeText(getApplicationContext(), "Image not choosen", Toast.LENGTH_SHORT).show();
        }

        super.onActivityResult(requestCode, resultCode, data);
    }




}