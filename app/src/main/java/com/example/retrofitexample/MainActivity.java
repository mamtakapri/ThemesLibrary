package com.example.retrofitexample;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.retrofitexample.model.Datum;
import com.example.retrofitexample.model.RetroData;
import com.example.retrofitexample.network.RequestInterface;
import com.example.retrofitexample.network.RetrofitClient;
import com.facebook.stetho.Stetho;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
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
    public final String downloadURL = RetrofitClient.getRetrofitInstance().baseUrl().toString();
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    String url = "";
    String name = "";
    ProgressBar progressBar;
    ThemesDAO dao;
    URL ImageUrl = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            //progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setMessage("Loading....");
            //progressDialog.show();
        ThemesRoomDatabase db = ThemesRoomDatabase.getInstance(getApplicationContext());

        dao = db.themesDao();


//        Stetho.InitializerBuilder initializerBuilder =
//                Stetho.newInitializerBuilder(this);// Enable Chrome DevTools
//        initializerBuilder.enableWebKitInspector(
//                Stetho.defaultInspectorModulesProvider(this)
//        );// Enable command line interface
//        initializerBuilder.enableDumpapp(
//                Stetho.defaultDumperPluginsProvider(this)
//        );// Use the InitializerBuilder to generate an Initializer
//        Stetho.Initializer initializer = initializerBuilder.build();// Initialize Stetho with the Initializer
//        Stetho.initialize(initializer);

        allStationData();

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
                if (model != null) {
                    //progressDialog.dismiss();
                    //generateDataList(model.getData());
                    generateDataList((model.getData()));
                    Log.d("data",String.valueOf(model.getData().size()));

                }

            }

            @Override
            public void onFailure(Call<RetroData> call, Throwable t) {
                //progressDialog.dismiss();
                Toast.makeText(MainActivity.this,"onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<Datum> themeList) {
        /*ASYNC_TASK
        for(int i=0;i<themeList.size();i++)
        {
            if(dao.matchUrl(themeList.get(i).getImgUrl().toString())!=null)
            {
                themeList.get(i).setIsDownloaded(true);
            }
        }*/


        recyclerView = findViewById(R.id.show_theme_data_recycler_view);
        CustomAdapter adapter = new CustomAdapter(this,themeList,this );
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        //AsyncTaskExample run = new AsyncTaskExample();
        //run.execute(themeList);
    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDownloadBtnClicked(String url, String name, ProgressBar download_progress) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
            progressBar = download_progress;
            if(ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            {
                this.url = url;
                this.name = name;
                beginDownload();
            }
            else
            {
                requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,Manifest.permission.READ_EXTERNAL_STORAGE},REQUEST_CODE_FOR_PERMISSIONS);
            }
        }
        else
        {
            beginDownload();
            //download without permission
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
        if (url != "")
        {

            File folder = new File(getExternalFilesDir(null),"Themes");
            if(!folder.exists())
                folder.mkdir();
            File file = new File(folder.getAbsolutePath(), name);

            BroadcastReceiver onComplete=new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                        progressBar.setVisibility(View.GONE);
                        ThemesEntity entity = new ThemesEntity();
                        entity.setThemeUrl(url);
                        //dao.insertTheme(entity);// your code
                }
            };

            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle("Theme "+name)
                    .setDescription("Downloading "+ name)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE| DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);
            registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

        }
    }

//    private class AsyncTaskExample extends AsyncTask<List<Datum>, String, Bitmap> {
//
//        @Override
//
//        protected Bitmap doInBackground(List<Datum> lists) {
//            Bitmap bmImg = null;
//            if(dao.matchUrl(lists.get(0).getImgUrl().toString())!=null) {
//                try {
//                    ImageUrl = new URL(lists.get(0).getImgUrl().toString());
//                    HttpURLConnection conn = (HttpURLConnection) ImageUrl.openConnection();
//                    conn.setDoInput(true);
//                    conn.connect();
//                    InputStream is = conn.getInputStream();
//                    BitmapFactory.Options options = new BitmapFactory.Options();
//                    bmImg = BitmapFactory.decodeStream(is, null, options);
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
//            }
//            return bmImg;
//
//        }
//
//        @Override
//        protected Bitmap doInBackground(List<Datum>... lists) {
//            return null;
//        }
//
//        @Override
//        protected void onPostExecute(Bitmap bitmap) {
//            super.onPostExecute(bitmap);
//
//        }
//    }



}