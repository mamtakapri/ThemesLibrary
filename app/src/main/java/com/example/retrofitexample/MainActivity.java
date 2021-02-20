package com.example.retrofitexample;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.retrofitexample.model.Datum;
import com.example.retrofitexample.model.RetroData;
import com.example.retrofitexample.network.RequestInterface;
import com.example.retrofitexample.network.RetrofitClient;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity {
    public static final String Authorization = "S^e#r7#&01)b8r*(#%^@T";
    public static final String  contentType ="application/json";
    ProgressDialog progressDialog;
    RecyclerView recyclerView;
    Button fetchButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //fetchButton = (Button) findViewById(R.id.btn_1);
        //TextView errorText = (TextView) findViewById(R.id.text_show);

       // progressDialog = new ProgressDialog(MainActivity.this);
      //  progressDialog.setMessage("Loading....");
      //  progressDialog.show();

        allStationData();
/*fetchButton.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
        allStationData();
    }
});*/
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
                   // progressDialog.dismiss();
                    //generateDataList(model.getData());
                    generateDataList((model.getData()));
                    Log.d("data",String.valueOf(model.getData().size()));
                    Toast.makeText(MainActivity.this,model.getData().get(1).getName(),Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<RetroData> call, Throwable t) {
                Toast.makeText(MainActivity.this,"onFailure",Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void generateDataList(List<Datum> themeList) {
        recyclerView = findViewById(R.id.show_theme_data_recycler_view);
        CustomAdapter adapter = new CustomAdapter(this,themeList );
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(MainActivity.this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
    }

}