package com.example.simplechatapp;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;

import java.util.ArrayList;

public class MessageAdapter extends  RecyclerView.Adapter  {

    ArrayList<message> datalist;
    Context context;

    int key_send=1;
    int key_receive=2;
    public MessageAdapter() {

    }

    public MessageAdapter(ArrayList<message> datalist, Context context) {
        this.datalist = datalist;
        this.context = context;

    }



    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if(viewType==key_send)
        {

            View v=LayoutInflater.from(context).inflate(R.layout.sender_layout,parent,false);
            return new sentviewholder(v);
        }
        else
        {
            View v=LayoutInflater.from(context).inflate(R.layout.receiver_layout,parent,false);
            return new receivedviewholder(v);

        }


    }
    @Override
    public int getItemViewType(int position) {

        message messages=datalist.get(position);
        String uid=FirebaseAuth.getInstance().getCurrentUser().getUid().toString();
        if(uid.equals(messages.getSenderid()))
            return key_send;
        else
            return key_receive;


    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {


        message messages= datalist.get(position);
        if(holder.getClass()==sentviewholder.class)
        {
            sentviewholder viewholder=(sentviewholder)holder;
            viewholder.sendmessage.setText(messages.getMessage());
        }

        else
        {
            receivedviewholder viewholder=(receivedviewholder) holder;
            viewholder.receivedmessage.setText(messages.getMessage());
        }
    }

    @Override
    public int getItemCount() {
      return datalist.size();

    }



    public class sentviewholder extends RecyclerView.ViewHolder {
        TextView sendmessage;

        public sentviewholder(@NonNull View itemView) {
            super(itemView);
            sendmessage = itemView.findViewById(R.id.sentmessage);


        }
    }


    public class receivedviewholder extends RecyclerView.ViewHolder {

        TextView receivedmessage;

        public receivedviewholder(@NonNull View itemView) {
            super(itemView);
            receivedmessage = itemView.findViewById(R.id.receivedmessage);
        }
    }



}