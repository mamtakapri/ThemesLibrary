package com.example.retrofitexample;

import android.content.res.Resources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ThemesDAO {

    @Query("SELECT * FROM themes_table")
    List<ThemesEntity> getThemesUrlList();

//    @Query("SELECT * From themes_table where themeUrl=:url")
//    ThemesEntity matchUrl(String url);

    @Insert
    void insertTheme(ThemesEntity themes_table);

    @Delete
    void deleteTheme(ThemesEntity themes_table);
}
