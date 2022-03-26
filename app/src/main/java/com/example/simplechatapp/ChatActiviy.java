package com.example.simplechatapp;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class ChatActiviy extends AppCompatActivity {

    FirebaseAuth mauth;
    FirebaseFirestore db;
    CollectionReference cdref;
    DocumentReference dbref;
   RecyclerView recyclerView;
    ChatAdapter myadapter;


//    public static final String TAG = "ChatActivity";


    @Override
    public void onBackPressed() {

        finish();
//        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat_activiy);

        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cdref = db.collection("Users");

        recyclerView = findViewById(R.id.recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ArrayList<ChatView> list=new ArrayList<>();

        cdref.get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {

                List<DocumentSnapshot> alllist=queryDocumentSnapshots.getDocuments();
                for(DocumentSnapshot snapshot: alllist)
                {
                    ChatView obj =snapshot.toObject(ChatView.class);

                    if(FirebaseAuth.getInstance().getUid().equals(obj.getUid()))
                    {
                        continue;
                    }
                    else {
                        list.add(obj);
                    }

                }
//                for(int i=1;i<list.size();i++) {
//                    Log.e(TAG, "obji " + list.get(i).getImage());
//                    Log.e(TAG,"name "+list.get(i).name);
//                    Log.e(TAG, "objstatus " + list.get(i).getStatus());
//                    Log.e(TAG, "objuid " + list.get(i).getUid());
//
//
//                }
                recyclerView.setAdapter(new ChatAdapter(getApplicationContext(),list));
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
             Toast.makeText(ChatActiviy.this, "failed to get userlist " + e.getMessage(), Toast.LENGTH_SHORT).show();


            }
        });


//        myadapter=new ChatAdapter();


//        recyclerView.setLayoutManager(new LinearLayoutManager(this));
//        recyclerView.setAdapter(new ChatAdapter(list));




        /*
        mauth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        cdref = db.collection("Users");
        s4 = mauth.getUid().toString();


        dbref=db.document("Users/"+mauth.getUid().toString());
//        dbref = db.collection("Users").document("FVeQ7MqA14arWgkY2VWGnp2eSsj2");

        Toast.makeText(this, "uid " + s4, Toast.LENGTH_LONG).show();
//                         Log.e(TAG,"onsucces+ "+snapshot.getData().toString());
//        Log.e(TAG,"uid "+s4 +"FVeQ7MqA14arWgkY2VWGnp2eSsj2");

        namet = findViewById(R.id.name1);
        statust = findViewById(R.id.status1);
        uidt = findViewById(R.id.uid1);
        profilet = findViewById(R.id.image1);

//        cdref.whereEqualTo("uid", s4)
//                .get()
//                .addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
//            @Override
//            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
//                namet.setText(queryDocumentSnapshots.getDocuments().);
//
//
//            }
//        }).addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Toast.makeText(ChatActiviy.this, "failed to fetch data " + e.getMessage(), Toast.LENGTH_SHORT).show();
//
//
//            }
//        });



        dbref.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                if(documentSnapshot.exists())
                {

                    namet.setText(documentSnapshot.getString("name"));
                    statust.setText(documentSnapshot.getString("status"));
                    uidt.setText(documentSnapshot.getString("uid"));
                   s1=documentSnapshot.getString("image");

                    Picasso.get().load(s1).into(profilet);

//                    Toast.makeText(ChatActiviy.this, "success to fetch data ", Toast.LENGTH_LONG).show();
//                    Log.e(TAG,"onsucces+ "+documentSnapshot.getData().toString());

                }

                else
                {
//                    Log.e(TAG,"onsucces+ "+documentSnapshot.getData().toString()+"  "+s4);

                    Toast.makeText(ChatActiviy.this, "row not found", Toast.LENGTH_SHORT).show();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(ChatActiviy.this, "failed to fetch data "+e.getMessage(), Toast.LENGTH_SHORT).show();

            }
        });


         */





    }


}