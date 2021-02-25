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
    @PrimaryKey(autoGenerate = true)
    private int theme_id;

    private String themeUrl;

    private boolean download_status;
    private String path;


    public ThemesEntity()
    {

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
