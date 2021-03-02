package com.example.retrofitexample;

import android.content.res.Resources;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.TypeConverters;

import java.util.Date;
import java.util.List;


@Dao
public interface ThemesDAO {

    @Query("SELECT * FROM themes_table")
    List<ThemesEntity> getThemesAllList();

    @Query("SELECT * FROM themes_table WHERE "+"`THEME URL`= :url")
    List<ThemesEntity> getThemesUrlList(String url);

    @Query("SELECT COUNT(*) FROM themes_table")
    int getTotalFields();

   /* @Query("SELECT `Date of Creation` FROM themes_table WHERE `THEME URL` = :url")
    Long getCreationTime(String url);

    @Query("SELECT `Date of Creation` FROM themes_table WHERE `THEME URL` = :url")
    Long getUpdationTime(String url);*/

    /*@Query("UPDATE themes_table  SET `Date of Updation` = :date WHERE `THEME URL` = :url")
    void updateUpdationTime(String url, Date date);*/

    @Query("SELECT * From themes_table where `THEME URL`= :url")
    ThemesEntity matchUrl(String url);

    @Query("SELECT COUNT(`DOWNLOAD STATUS`) FROM themes_table WHERE `THEME URL` = :url AND `DOWNLOAD STATUS` = :stat")
    int getThemesStatusList(String url, boolean stat);

    @Query("UPDATE themes_table SET `DOWNLOAD STATUS` = :isDownloaded, `LOCAL PATH` = :path WHERE `THEME URL` = :url")
    void updateDownloadStatus(boolean isDownloaded,String path, String url);

    @TypeConverters(DateTypeConverter.class)
    @Query("UPDATE themes_table SET `Time of Updation` = :updated WHERE `THEME URL` = :url")
    void updateTime(Date updated, String url);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertTheme(ThemesEntity... themes_table);

    @Delete
    void deleteTheme(ThemesEntity themes_table);
}
