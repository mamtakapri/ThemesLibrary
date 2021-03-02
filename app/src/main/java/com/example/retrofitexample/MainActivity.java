package com.example.retrofitexample;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.icu.text.SimpleDateFormat;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.retrofitexample.model.Datum;
import com.example.retrofitexample.model.RetroData;
import com.example.retrofitexample.network.RequestInterface;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements  OnButtonClickListener{
    public static final String Authorization = "S^e#r7#&01)b8r*(#%^@T";
    public static final String  contentType ="application/json";
    public final int  REQUEST_CODE_FOR_PERMISSIONS = 123;
    RecyclerView recyclerView;
    String url = "";
    String name = "";
    ProgressBar progress;
    Button downloadBtn;


    ThemesRoomDatabase db;
    ThemesEntity entity = new ThemesEntity();

    boolean isDownload = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        db = ThemesRoomDatabase.getInstance(MainActivity.this);
        Date dateobj = new Date();




        SharedPreferences sharedPref = getSharedPreferences("userData",MODE_PRIVATE);
        String loadFirstTime = sharedPref.getString("loadFirstTime","");
        long time = sharedPref.getLong("time", 0);

        if(!TextUtils.isEmpty(loadFirstTime) && (dateobj.getTime() - time )<= 30000) {

            List<ThemesEntity> dbThemesList = db.themesDao().getThemesAllList();
            generateDataList(null,dbThemesList);

            Toast.makeText(getApplicationContext(),(dateobj.getTime()-time)+"\n Before 30 seconds",Toast.LENGTH_SHORT).show();


        }
        else{

            long date = dateobj.getTime();
            SharedPreferences.Editor edit = sharedPref.edit();
            allStationData();
            edit.putString("loadFirstTime", "No");
            edit.putLong("time", date);
            edit.commit();
            Toast.makeText(getApplicationContext(),"after 30 seconds",Toast.LENGTH_SHORT).show();
        }

    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        Toast.makeText(context.getApplicationContext(),dbFile.getAbsolutePath(),Toast.LENGTH_SHORT).show();
        return dbFile.exists();
    }



    private Bitmap getBitmap(String urls){
      Bitmap image = null;

              URL url = null;
              try {
                  url = new URL(urls);
              } catch (MalformedURLException e) {
                  e.printStackTrace();
              }
              try {
                  image = BitmapFactory.decodeStream(url.openConnection().getInputStream());
              } catch (IOException e) {
                  e.printStackTrace();
              }

        return image;

  }


    private void allStationData(){
        Retrofit retrofit=new Retrofit.Builder().baseUrl("http://api.rocksplayer.com/")
                .addConverterFactory(GsonConverterFactory.create()).build();
        RequestInterface requestInterface=retrofit.create(RequestInterface.class);
        Call<RetroData> call= requestInterface.getData(Authorization,contentType,"1");
        call.enqueue(new Callback<RetroData>() {
            @Override
            public void onResponse(Call<RetroData> call, Response<RetroData> response) {
                RetroData model = response.body();
                if (model != null) { generateDataList((model.getData()), null); }
                else
                { Toast.makeText(getApplicationContext(),"No data to show", Toast.LENGTH_SHORT).show(); }

            }

            @Override
            public void onFailure(Call<RetroData> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void generateDataList(List<Datum> themeList,List<ThemesEntity> dbThemeList) {
        CustomAdapter adapter;
        recyclerView = findViewById(R.id.show_theme_data_recycler_view);

        //List<ThemesEntity> nList = new ArrayList<>();
        /*for(ThemesEntity item : dbThemeList)
        {
            if(item.getThemeUrl()!=null && !item.getThemeUrl().equals("null"))
            {
                nList.add(item);
            }
        }*/
        if(dbThemeList==null) {
            List<Datum> mList = new ArrayList<>();
            for(Datum item : themeList)
            {
                if(item.getImgUrl()!=null && !item.getImgUrl().equals("null"))
                {
                    mList.add(item);
                }
            }
            adapter = new CustomAdapter(this, mList, null, this);
        }
        else {
            adapter = new CustomAdapter(this, null, dbThemeList, this);
        }
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDownloadBtnClicked(String url, String name, Button downloadBtn, ProgressBar progress, boolean isDownloaded) {
        this.url = url;
        this.name = name;
        this.downloadBtn = downloadBtn;
        this.isDownload = isDownloaded;
        this.progress = progress;
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                beginDownload();
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_FOR_PERMISSIONS);
            }
        }
        else
        {
            beginDownload();//download without permission
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode == REQUEST_CODE_FOR_PERMISSIONS)
        {
            if(grantResults.length > 0 && grantResults[0]==PackageManager.PERMISSION_GRANTED) {

                beginDownload();

            }
            else
            {
                Toast.makeText(this,"Permission Denied",Toast.LENGTH_SHORT).show();
            }
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.N)
    private void beginDownload() {
        if (url != null)
        {
            new AsyncTaskExample().execute();
        }
        else
        {
            Toast.makeText(MainActivity.this, "File Not Exist", Toast.LENGTH_SHORT).show();
        }
    }



    private class AsyncTaskExample extends AsyncTask<String, String, File> {


        @NonNull
        @Override
        protected Object clone() throws CloneNotSupportedException {
            return super.clone();
        }

        @Override
        protected File doInBackground(String... strings) {
            Bitmap bmImg = null;
            if (!TextUtils.isEmpty(url)) {
                bmImg = getBitmap(url);
            }

            if(bmImg!=null) {
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bmImg.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
                File file = new File(Environment.getExternalStorageDirectory() + File.separator + Environment.DIRECTORY_DCIM + File.separator + name);
                try {
                    FileOutputStream fo = new FileOutputStream(file);
                    fo.write(bytes.toByteArray());
                    fo.flush();
                    fo.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                return file;
            }
                return null;


        }

        @Override
        protected void onPostExecute(File file) {
            if(file!=null && file.exists()) {


                if (!isDownload) {

                    Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    super.onPostExecute(file);
                    isDownload = true;
                    entity.setDownload_status(true);
                    //entity.setPath(MainActivity.this.getDatabasePath(ThemesRoomDatabase.DATABASE_NAME).toString());
                    db.themesDao().updateDownloadStatus(isDownload,file.getAbsolutePath(),url);
                    progress.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(MainActivity.this, "Already Existed", Toast.LENGTH_SHORT).show();
                    progress.setVisibility(View.GONE);
                }
            }

        }
    }
}