package com.example.simplechatapp;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class ChatAdapter  extends RecyclerView.Adapter<ChatAdapter.myholder> {


    ArrayList<ChatView> datalist;
    Context context;



    public ChatAdapter(Context context,ArrayList<ChatView> datalist) {
        this.datalist = datalist;
        this.context=context;
    }

    @NonNull
    @Override
    public myholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View v=inflater.inflate(R.layout.userviewlayout,parent,false);

        return new myholder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull myholder holder, int position) {

        ChatView user =datalist.get(position);
//        holder.name.setText(datalist.get(position));
        holder.name.setText(user.getName());
        holder.status.setText("online");
      String uri=user.getImage();
      Picasso.get().load(uri).into(holder.profile);
//        holder.name.setText(datalist.get(position).getName());
//        String uri=datalist.get(position).getImage().toString();
//        Picasso.get().load(uri).into(holder.profile);

        holder.status.setText(datalist.get(position).getStatus());
//        String uri=datalist.get(position).getImage().toString();
//        Picasso.get().load(uri).into(holder.profile);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(context,ChattingActivity.class);
                intent.putExtra("name",user.getName());
                intent.putExtra("image",user.getImage());
                intent.putExtra("status",user.getStatus());
                intent.putExtra("uid",user.getUid());

                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);

                context.startActivity(intent);



            }
        });
    }

    @Override
    public int getItemCount() {
        return datalist.size();
    }



    public class myholder extends  RecyclerView.ViewHolder{

        ImageView profile;
        TextView name,status;

        public myholder(@NonNull View itemView) {
            super(itemView);

            profile=itemView.findViewById(R.id.viewsetuserprofilepic);
            name=itemView.findViewById(R.id.showname);
            status=itemView.findViewById(R.id.showstatus);


        }



    }
}
