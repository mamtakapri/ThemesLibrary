package com.example.retrofitexample;

import android.content.res.Resources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;


@Dao
public interface ThemesDAO {

    @Query("SELECT * FROM themes_table")
    List<ThemesEntity> getThemesUrlList();

    @Query("SELECT * FROM themes_table WHERE "+"themeUrl= :url")
    List<ThemesEntity> getThemesUrlList(String url);

//    @Query("SELECT * From themes_table where themeUrl=:url")
//    ThemesEntity matchUrl(String url);

    @Insert(onConflict = OnConflictStrategy.ABORT)
    void insertTheme(ThemesEntity themes_table);

    @Delete
    void deleteTheme(ThemesEntity themes_table);
}
