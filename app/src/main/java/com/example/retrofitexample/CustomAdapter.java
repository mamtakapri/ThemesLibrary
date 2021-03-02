package com.example.retrofitexample;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
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

import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Locale;
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
    public List<ThemesEntity> dbDataList;
    public Context context;
    OnButtonClickListener listener;
    ThemesRoomDatabase db = ThemesRoomDatabase.getInstance(context);
    ThemesEntity entity = new ThemesEntity();


    public CustomAdapter(Context context, List<Datum> themeData,List<ThemesEntity> dbDataList,OnButtonClickListener listener)
    {
        this.context = context;
        this.listener = listener;

        if(dbDataList==null)
        {
            this.themeDataList = themeData;
        }
        else {
            this.dbDataList = dbDataList;
        }

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
        String url;
        String imgName;
        boolean isDownloaded;
        int id;
        String mode;
        if(dbDataList==null)
        {

            url = String.valueOf(themeDataList.get(position).getImgUrl());
            imgName = themeDataList.get(position).getName();
            isDownloaded = themeDataList.get(position).getIsDownloaded();
            id = themeDataList.get(position).getId();
            mode = themeDataList.get(position).getMode();

            if(((db.themesDao().getThemesStatusList(url,true))>0) || url.equals("null"))
            {
                holder.download_btn.setVisibility(View.GONE);
            }

            else {
                holder.download_btn.setVisibility(View.VISIBLE);
                holder.download_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.download_btn.setVisibility(GONE);
                        holder.download_progress.setVisibility(VISIBLE);

                        //Toast.makeText(context,/*dateText*/"Downloading Started",Toast.LENGTH_SHORT).show();
                        listener.onDownloadBtnClicked(url,imgName ,holder.download_btn,holder.download_progress,isDownloaded);
                    }
                });
            }

            if(url!=null) {

                Glide.with(context).asBitmap().optionalCenterCrop()
                        .load(Uri.parse(url)).thumbnail(0.04f)
                        .into(holder.themeData);

            /*Date date = new Date(System.currentTimeMillis());
            SimpleDateFormat df2 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                df2 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss");
            }
            String dateText = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                dateText = df2.format(date);
            }*/
////////////////////////////////////////////////////
                //String string_date = db.themesDao().getCreationTime(url);



/*
            String sDate1 = db.themesDao().getCreationTime(url);
            Date date1 = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                try {
                    date1 = new SimpleDateFormat("dd-MMM-yyyy hh:mm:ss").parse(sDate1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
            }
            Toast.makeText(context, String.valueOf(date1), Toast.LENGTH_SHORT).show();*/
//////////////////////////////////////////////////////
//            entity.setCreationDate(date);

                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                    SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:sss");
                }
                Date dateobj = new Date();
                entity.setCreationDate(dateobj);
                entity.setUpdationDate(dateobj);
                entity.setDownload_status(isDownloaded);
                entity.setThemeUrl(url);
                entity.setTheme_id(id);
                entity.setPath("");
                entity.setTheme_name(imgName);
                entity.setMode(mode);
                db.themesDao().insertTheme(entity);

            }
            else if(url.isEmpty()){
                holder.download_btn.setVisibility(GONE);
                entity.setDownload_status(isDownloaded);
                entity.setThemeUrl("");
                entity.setTheme_id(id);
                entity.setPath("null");
                db.themesDao().insertTheme(entity);
            }


        }
        else {
            url = String.valueOf(dbDataList.get(position).getThemeUrl());
            imgName = dbDataList.get(position).getTheme_name();
            isDownloaded = dbDataList.get(position).isDownload_status();
            id = dbDataList.get(position).getTheme_id();
            mode = dbDataList.get(position).getMode();

            if(url.equals("null")==false) {

                Glide.with(context).asBitmap().optionalCenterCrop()
                        .load(Uri.parse(url)).thumbnail(0.04f)
                        .into(holder.themeData);
            }

            if(isDownloaded || url.equals("null")/*((db.themesDao().getThemesStatusList(url,true))>0)*/)
            {
                holder.download_btn.setVisibility(View.GONE);
            }

            else {
                holder.download_btn.setVisibility(View.VISIBLE);
                holder.download_btn.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        holder.download_btn.setVisibility(GONE);
                        holder.download_progress.setVisibility(VISIBLE);

                        //Toast.makeText(context,/*dateText*/"Downloading Started",Toast.LENGTH_SHORT).show();
                        listener.onDownloadBtnClicked(url,imgName ,holder.download_btn,holder.download_progress,isDownloaded);
                    }
                });
            }

            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
                SimpleDateFormat df = new SimpleDateFormat("dd/MM/yy HH:mm:ss:sss");
            }
            Date dateobj = new Date();
            db.themesDao().updateTime(dateobj, url);

        }

    }



    @Override
    public int getItemCount() {
        if(dbDataList==null) {
            return themeDataList.size();
        }
        else{
            return dbDataList.size();
        }
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