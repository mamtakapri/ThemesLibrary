package com.example.retrofitexample;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.Index;
import androidx.room.PrimaryKey;

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

}
