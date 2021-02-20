package com.example.retrofitexample;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitexample.model.Datum;
import com.example.retrofitexample.model.RetroData;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static androidx.recyclerview.widget.RecyclerView.*;
import static java.util.Objects.*;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{

    private List<Datum> themeDataList;
    private Context context;

    public CustomAdapter(Context context, List<Datum> themeData)
    {
        this.context = context;
        this.themeDataList = themeData;
    }

    @NonNull
    @Override
    public CustomViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View v = inflater.inflate(R.layout.theme_data, parent, false);
        CustomViewHolder vh = new CustomViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull CustomViewHolder holder, int position) {
        Object url = themeDataList.get(position).getImgUrl();
        if(url!=null)
        {
            Glide.with(context).asBitmap().optionalCenterCrop()
                    .load(Uri.parse(url.toString())).thumbnail(0.04f)
                .into(holder.themeData);
        }

    }

    @Override
    public int getItemCount() {
        return themeDataList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView themeData;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            themeData = (ImageView) itemView.findViewById(R.id.theme_img_view);
        }
    }
}
