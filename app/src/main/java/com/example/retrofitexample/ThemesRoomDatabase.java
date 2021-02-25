package com.example.retrofitexample;

import android.content.Context;
import android.util.Log;

import androidx.room.Database;
import androidx.room.Entity;
import androidx.room.Room;
import androidx.room.RoomDatabase;

@Database(entities = ThemesEntity.class, exportSchema = false, version = 1)
public abstract class ThemesRoomDatabase extends RoomDatabase{

    public static final String DATABASE_NAME = "THEME_DB";
    private static  ThemesRoomDatabase sInstance;
    private static final Object LOCK = new Object();

    public static ThemesRoomDatabase getInstance(Context context) {
        if (sInstance == null) {
            synchronized (LOCK) {
                Log.d("THEME DB", "Creating new database instance");
                sInstance = Room.databaseBuilder(context.getApplicationContext(),
                        ThemesRoomDatabase.class, ThemesRoomDatabase.DATABASE_NAME).allowMainThreadQueries()
                        .build();
            }
        }

        return sInstance;
    }


    public abstract ThemesDAO themesDao();

}
