package com.example.employefy.Adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;
import com.example.employefy.Models.Employe;
import com.example.employefy.R;
import com.example.employefy.ViewProfileActivity;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.ConcurrentModificationException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class EmployeAdapter extends RecyclerView.Adapter<EmployeAdapter.EmployeViewHolder>
{

    Context context;
    List<Employe> employeList;

    View view;

    public EmployeAdapter(Context context, List<Employe> employeList) {
        this.context = context;
        this.employeList = employeList;
    }

    @NonNull
    @Override
    public EmployeViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        view= LayoutInflater.from(context).inflate(R.layout.employeitem,parent,false);
        return new EmployeViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final EmployeViewHolder holder, int position) {
       Picasso.get().load(employeList.get(position).getProfileimage()).into(holder.profileImage);
       // Glide.with(context).load(employeList.get(position).getProfileimage()).into(holder.profileImage);
//        Glide.with(context).load(employeList.get(position).getProfileimage()).listener(new RequestListener<Drawable>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
//                Toast.makeText(context, "Error loading image"+e.getMessage(), Toast.LENGTH_LONG).show();
//                Log.v("glide", "Error loading image", e);
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
//                return false;
//            }
//        }).into(holder.profileImage);
        holder.firstName.setText(employeList.get(position).getFirstname());
        holder.lastName.setText(employeList.get(position).getLastname());

        final Map<String,String> map=new HashMap<>();
        map.put("PROFILEIMAGE",employeList.get(position).getProfileimage());
        map.put("FIRSTNAME",employeList.get(position).getFirstname());
        map.put("LASTNAME",employeList.get(position).getLastname());
        map.put("CONTACTNO",employeList.get(position).getContactno());
        map.put("EMAIL",employeList.get(position).getEmail());
        map.put("ADDRESS",employeList.get(position).getAddress());
        map.put("CITY",employeList.get(position).getCity());
        map.put("STATE",employeList.get(position).getState());
        map.put("COUNTRY",employeList.get(position).getCountry());
        holder.viewprofileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                context.startActivity(new Intent(context, ViewProfileActivity.class).putExtra("EmployeData", (Serializable) map));
            }
        });
    }

    @Override
    public int getItemCount() {
        return employeList.size();
    }

    public class EmployeViewHolder extends RecyclerView.ViewHolder
    {
        TextView firstName,lastName;
        ImageView profileImage;
        Button viewprofileButton;
        public EmployeViewHolder(@NonNull View itemView) {
            super(itemView);
            profileImage=itemView.findViewById(R.id.setProfileImage);
            firstName=itemView.findViewById(R.id.setFirstname);
            lastName=itemView.findViewById(R.id.setLastname);
            viewprofileButton=itemView.findViewById(R.id.viewProfile_Btn);

        }
    }
}
