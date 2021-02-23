package com.example.retrofitexample;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.retrofitexample.model.Datum;
import com.example.retrofitexample.model.RetroData;
import com.example.retrofitexample.network.RequestInterface;
import com.example.retrofitexample.network.RetrofitClient;

import java.io.File;
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
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
            //progressDialog = new ProgressDialog(MainActivity.this);
            //progressDialog.setMessage("Loading....");
            //progressDialog.show();

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
        recyclerView = findViewById(R.id.show_theme_data_recycler_view);
        CustomAdapter adapter = new CustomAdapter(this,themeList,this );
        RecyclerView.LayoutManager layoutManager = new GridLayoutManager(MainActivity.this, 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

    }



    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    public void onDownloadBtnClicked(String url, String name) {
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.M)
        {
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
            File file = new File(getExternalFilesDir(null), "ThemesDownload");
            DownloadManager.Request request = new DownloadManager.Request(Uri.parse(url))
                    .setTitle("Theme "+name)
                    .setDescription("Downloading "+ name)
                    .setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE| DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    .setDestinationUri(Uri.fromFile(file))
                    .setRequiresCharging(false)
                    .setAllowedOverMetered(true)
                    .setAllowedOverRoaming(true);

            DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
            long downloadID = downloadManager.enqueue(request);


        }
    }


}