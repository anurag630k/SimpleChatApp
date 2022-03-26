package com.example.simplechatapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.media.Image;
import android.nfc.Tag;
import android.os.Bundle;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChattingActivity extends AppCompatActivity {



    FirebaseAuth mauth;
    ImageView chatprofile;
    TextView chatname;
    TextView chatstatus;
    ImageView backbutton;

    EditText emessage;

    RecyclerView recyclerView;
    MessageAdapter messageAdapter;
    message message;
    ArrayList<message> list;
    ImageButton sendmessage;
    FirebaseDatabase firebaseDatabase;




    String senderid,receiverid,senderroom,receiverroom;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatting);
        getSupportActionBar().hide();



        senderid=FirebaseAuth.getInstance().getUid().toString();
        backbutton=findViewById(R.id.backbutton);

        chatprofile = findViewById(R.id.chatprofile);
        chatname = findViewById(R.id.chatname);
        chatstatus = findViewById(R.id.chatstatus);

        emessage=findViewById(R.id.emessage);
        recyclerView=findViewById(R.id.chatrecyclerview);
        sendmessage=findViewById(R.id.sendbutton);

        firebaseDatabase=FirebaseDatabase.getInstance();

        list=new ArrayList<>();
        messageAdapter=new MessageAdapter(list,this);

        LinearLayoutManager  linearLayoutManager=new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(messageAdapter);


        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String status = intent.getStringExtra("status");
        String image = intent.getStringExtra("image");
        Picasso.get().load(image).into(chatprofile);
        chatname.setText(name);
        chatstatus.setText(status);






        backbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();

            }
        });


//        senderid=mauth.getUid().toString();

        receiverid=intent.getStringExtra("uid");
        senderroom=senderid+receiverid;
        receiverroom=receiverid+senderid;

        retrivemsg();

//        Toast.makeText(this,  " id "+senderid, Toast.LENGTH_SHORT).show();
        sendmessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                sendmsg();



            }
        });






    }



    private void retrivemsg() {
        firebaseDatabase.getReference().child("chats").child(senderroom).child("messages").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot snap :snapshot.getChildren())
                {
                    message msg=snap.getValue(message.class);
                    list.add(msg);

                }

                messageAdapter.notifyDataSetChanged();;
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }


    private void sendmsg() {

        String enteredmsg = emessage.getText().toString();
        if (enteredmsg.isEmpty())
        {
            Toast.makeText(this, "enter message first..", Toast.LENGTH_SHORT).show();
        }
        else
        {


            message = new message(enteredmsg, senderid);


//            Toast.makeText(this, "msg "+enteredmsg, Toast.LENGTH_SHORT).show();
            firebaseDatabase.getReference().child("chats").child(senderroom).child("messages").push().setValue(message)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            firebaseDatabase.getReference().child("chats").child(receiverroom).child("messages").push().setValue(message).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                                    Toast.makeText(ChattingActivity.this, "msg in recieveroom", Toast.LENGTH_SHORT).show();
                                }
                            });

//                            Toast.makeText(ChattingActivity.this, "msg sendrrrom", Toast.LENGTH_SHORT).show();

                        }
                    });


        }
    }




}