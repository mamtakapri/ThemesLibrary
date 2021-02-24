package com.example.retrofitexample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.net.Uri;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Toast;

import androidx.annotation.NonNull;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.retrofitexample.model.Datum;
import java.util.List;
import java.util.Objects;

import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import static androidx.recyclerview.widget.RecyclerView.*;
import static java.util.Objects.*;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.CustomViewHolder>{


    public List<Datum> themeDataList;
    public Context context;
    OnButtonClickListener listener;

    public CustomAdapter(Context context, List<Datum> themeData,OnButtonClickListener listener)
    {
        this.context = context;
        this.themeDataList = themeData;
        this.listener = listener;
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
        String imgName = themeDataList.get(position).getName();

        if(themeDataList.get(position).getIsDownloaded())
        {
            holder.download_btn.setVisibility(View.GONE);
        }

        else {
            holder.download_btn.setVisibility(View.VISIBLE);
        }

        if(url!=null)
        {
            Glide.with(context).asBitmap().optionalCenterCrop()
                    .load(Uri.parse(url.toString())).thumbnail(0.04f)
                .into(holder.themeData);

        }
        holder.download_btn.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.download_btn.setVisibility(GONE);
                holder.download_progress.setVisibility(VISIBLE);
                Toast.makeText(context,"Download has started",Toast.LENGTH_SHORT).show();
                listener.onDownloadBtnClicked(themeDataList.get(position).getImgUrl().toString(),themeDataList.get(position).getName(),holder.download_progress);
            }
        });
        holder.download_progress.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.download_progress.setVisibility(GONE);
                holder.download_btn.setVisibility(VISIBLE);
                Toast.makeText(context,"Download Cancelled",Toast.LENGTH_SHORT).show();
            }
        });

    }



    @Override
    public int getItemCount() {
        return themeDataList.size();
    }


    public class CustomViewHolder extends RecyclerView.ViewHolder {
        ImageView themeData;
        Button download_btn;
        ProgressBar download_progress;
        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            themeData = itemView.findViewById(R.id.theme_img_view);
            download_btn = itemView.findViewById(R.id.download_btn);
            download_progress = itemView.findViewById(R.id.download_progress);

        }
    }



}
