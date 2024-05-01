package com.example.facereg;



import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facereg.databinding.ActivityMainBinding;
import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayUserData extends AppCompatActivity {
    RecyclerView svMain;
    private RecyclerViewAdapterForUserData adapter;
    private ArrayList<ResponseApi> imageList;
    private String nrpUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayuserdata);
        svMain = findViewById(R.id.svMain);

        imageList = new ArrayList<>();

        adapter = new RecyclerViewAdapterForUserData(DisplayUserData.this, imageList);

        svMain.setAdapter(adapter);
        svMain.setLayoutManager(new LinearLayoutManager(this));
        svMain.setHasFixedSize(true);
        Intent intent = getIntent();
        nrpUser = intent.getStringExtra("nrp");

        remoteGetUsers();

    }

    public void setDataToAdapter(ArrayList<ResponseApi> data){
        adapter.setData(data);
    }
    public void remoteGetUsers() {
        ApiClientFaceReg api =  serverApi.builder().create(ApiClientFaceReg.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayUserData.this, SweetAlertDialog.PROGRESS_TYPE);
        Call<ArrayList<ResponseApi>> call = api.getUser(nrpUser);
        call.enqueue(new Callback<ArrayList<ResponseApi>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseApi>> call, Response<ArrayList<ResponseApi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    imageList = response.body();
                    setDataToAdapter(imageList);
                    svMain.setAdapter(adapter);
                    // Handle the data here
                } else {
                    pDialog.dismiss();
                    new SweetAlertDialog(DisplayUserData.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Error")
                            .setContentText(response.body().get(1).getImage().toString())
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    //finish();
                                }
                            }).show();
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseApi>> call, Throwable t) {
                Log.d("Error", t.toString());
                // Handle the failure here
            }
        });
    }



}
