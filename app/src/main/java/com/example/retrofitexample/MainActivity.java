package com.example.retrofitexample;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
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
    ProgressBar progressBar;

    ThemesRoomDatabase db;
    ThemesEntity entity = new ThemesEntity();

    boolean isDownload = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         db = ThemesRoomDatabase.getInstance(MainActivity.this);
            //progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setMessage("Loading....");
            //progressDialog.show();
        /*ThemesRoomDatabase db = ThemesRoomDatabase.getInstance(getApplicationContext());
        ThemesEntity themesEntity=new ThemesEntity();
        themesEntity.setThemeUrl(url);
        themesEntity.setPath(path);
        dao = db.themesDao();
        dao.insertTheme(themesEntity);
        dao.getThemesUrlList();*/




        /*Stetho.InitializerBuilder initializerBuilder =
                Stetho.newInitializerBuilder(this);// Enable Chrome DevTools
        initializerBuilder.enableWebKitInspector(
                Stetho.defaultInspectorModulesProvider(this)
        );// Enable command line interface
        initializerBuilder.enableDumpapp(
                Stetho.defaultDumperPluginsProvider(this)
        );// Use the InitializerBuilder to generate an Initializer
        Stetho.Initializer initializer = initializerBuilder.build();// Initialize Stetho with the Initializer
        Stetho.initialize(initializer);*/

        allStationData();

    }

    private static boolean doesDatabaseExist(Context context, String dbName) {
        File dbFile = context.getDatabasePath(dbName);
        Toast.makeText(context.getApplicationContext(),dbFile.getAbsolutePath().toString(),Toast.LENGTH_SHORT).show();
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

        recyclerView = findViewById(R.id.show_theme_data_recycler_view);
        CustomAdapter adapter = new CustomAdapter(this,themeList,this);
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

             new AsyncTaskExample().execute();

           /* BroadcastReceiver onComplete=new BroadcastReceiver() {
                public void onReceive(Context ctxt, Intent intent) {
                        progressBar.setVisibility(View.GONE);
                        ThemesEntity entity = new ThemesEntity();
                        entity.setThemeUrl(url);
                        entity.setDownload_status(true);
                        entity.setThemeUrl(file.toString());
                        dao.insertTheme(entity);// your code
                }
            };*/

           /* DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle("Theme "+name)
                    .setDescription("Downloading "+ name)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE| DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            downloadManager.enqueue(request);*/
            //registerReceiver(onComplete, new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE));

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
            if(!TextUtils.isEmpty(url)) {
                bmImg =getBitmap(url);
            }
            ByteArrayOutputStream bytes = new ByteArrayOutputStream();
            bmImg.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
            File file = new File(Environment.getExternalStorageDirectory()+ File.separator + Environment.DIRECTORY_DCIM + File.separator +name);
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

        @Override
        protected void onPostExecute(File file) {
            if(file.exists()) {


                if (db.themesDao().getThemesUrlList(url).size()==0 && !isDownload) {

                    Toast.makeText(MainActivity.this, file.getAbsolutePath(), Toast.LENGTH_SHORT).show();
                    super.onPostExecute(file);

                    entity.setThemeUrl(url);
                    entity.setDownload_status(true);
                    entity.setPath(MainActivity.this.getDatabasePath(ThemesRoomDatabase.DATABASE_NAME).toString() + "/" + name);
                    db.themesDao().insertTheme(entity);
                    entity.setDownload_status(true);
                    progressBar.setVisibility(View.GONE);
                }
                else {
                    Toast.makeText(MainActivity.this, "Already Existed", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.GONE);
                }
            }
        }
    }
}