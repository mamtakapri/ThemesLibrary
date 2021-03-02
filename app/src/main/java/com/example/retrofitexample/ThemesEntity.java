package com.example.retrofitexample;

import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.icu.util.TimeZone;
import android.os.Build;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;
import androidx.room.TypeConverter;
import androidx.room.TypeConverters;

import java.util.Date;
import java.text.ParseException;
import java.util.Locale;

@Entity(tableName = "themes_table")


public class ThemesEntity {
    @NonNull
    @PrimaryKey
    @ColumnInfo(name="ID")
    private int theme_id;


    @ColumnInfo(name = "NAME")
    private String theme_name;

    @ColumnInfo(name = "MODE")
    private String mode;

    @ColumnInfo(name = "THEME URL")
    private String themeUrl;

    @ColumnInfo(name = "DOWNLOAD STATUS")
    private boolean download_status;

    @ColumnInfo(name = "LOCAL PATH")
    private String path;

    @TypeConverters(DateTypeConverter.class)
    @ColumnInfo(name = "Time of Creation")
    private Date creationDate;

    @TypeConverters(DateTypeConverter.class)
    @ColumnInfo(name = "Time of Updation")
    private Date updationDate;





    public ThemesEntity()
    {

    }

    public String getTheme_name() {
        return theme_name;
    }
    public void setTheme_name(String theme_name) {
        this.theme_name = theme_name;
    }

    public String getMode() {
        return mode;
    }
    public void setMode(String mode) {
        this.mode = mode;
    }


    public void setTheme_id(int theme_id) {
        this.theme_id = theme_id;
    }
    public int getTheme_id() {
        return theme_id;
    }


    public void setDownload_status(boolean download_status) {
        this.download_status = download_status;
    }
    public boolean isDownload_status() {
        return download_status;
    }


    public void setPath(String path) {
        this.path = path;
    }
    public String getPath() {
        return path;
    }


    public void setThemeUrl(String url) {
        this.themeUrl = url;
    }
    public String getThemeUrl() {
        return themeUrl;
    }

    public void setCreationDate(Date creationDate) { this.creationDate = creationDate; }
    public Date getCreationDate() { return creationDate; }

    public void setUpdationDate(Date updationDate) { this.updationDate = updationDate; }
    public Date getUpdationDate() { return updationDate; }
}
