package com.example.homedecor;

import com.firebase.ui.database.FirebaseRecyclerAdapter;

import android.content.Context;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.collection.ArraySet;
import androidx.recyclerview.widget.RecyclerView;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.google.firebase.installations.Utils;
import com.squareup.picasso.Picasso;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.database.FirebaseRecyclerOptions;

import static android.widget.Toast.makeText;

import java.util.ArrayList;
import java.util.List;


//This class is used to populate the Recycler card view present at bottom of screen .it will keep on check thing database value while scrolling horizontally.
public class MyAdapter extends FirebaseRecyclerAdapter <Models,MyAdapter.myviewholder > {
    private static OnItemClickListener mListener;
    final String TAG="Myadapter";



    public MyAdapter(@NonNull  FirebaseRecyclerOptions<Models> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull myviewholder holder, int position, @NonNull final Models model) {
        Log.d(TAG, "This is my message text"+model.getText());
        Log.d(TAG, "This is my message img"+model.getImage());
        Log.d(TAG, "This is my message ID "+model.getId());

        mListener.sendstring(model.getId());
        Picasso.get().load(model.getImage()).into(holder.image);
        String ID=model.getId();
        MainActivity main=new MainActivity();
        main.listmodel2.add(ID);
       /* myviewholder view1=new myviewholder();
        view1.addmodel(model.getId());*/
       // myviewholder.addmodel("wow");
       // Glide.with(holder.image.getContext()).load(model.getImg_url()).into(holder.image);

   /*  holder.image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.d(TAG, "This is my id "+model.getId());
             */ /* ARFragment arobj= new ARFragment(model.getId());*/
           //     name=model.getId();
         /*   MainActivity main= new MainActivity();
            main.name="out";*/
  /*  main.armaker();*/
       //    main.onItemClick(1,"out4");
/*

                AppCompatActivity activity=(AppCompatActivity)view.getContext();
                activity.getSupportFragmentManager().beginTransaction().replace(R.id.arfrag,new ARFragment(model.getId())).addToBackStack(null).commit();
*/


       //    arobj.OnCreate(savedInstanceState);
//            }
//        });
    }
 /*   @Override
    public int getItemCount() {
        return listmodel.size();
    }
*/
    @Override
    public myviewholder onCreateViewHolder( ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.img_3d,parent,false);
        return new myviewholder(view);
    }

    public void setOnItemClickListener(OnItemClickListener Listener) {
        mListener=  Listener;

    }

    public interface OnItemClickListener {
        void onItemClick(int position);
        void sendstring(String name);
    }


//myviewhilder is useo populate the image present in cardview
    public static class myviewholder extends RecyclerView.ViewHolder implements View.OnClickListener
    {
        public static ArraySet<String> listmodel;
        ImageView image;
        TextView text;



        public myviewholder(@NonNull View itemView) {
            super(itemView);
            text=itemView.findViewById(R.id.text);
            image=itemView.findViewById(R.id.image);


            image.setOnClickListener(this::onClick);

        }



//onclick will check which model is click and tell its position for downloading
       @Override
        public void onClick(View v){
            if (mListener!=null){
                Log.d("myadapter","mlistner"+mListener);
                int position = getAdapterPosition();
                if (position!= RecyclerView.NO_POSITION){

                    mListener.onItemClick(position);
                }
            }



    }


    }

}




