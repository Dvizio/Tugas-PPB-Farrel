package com.example.facereg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.HeaderApi;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.ResponseApiUserData;
import com.example.facereg.server.serverApi;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.reflect.TypeToken;

import androidx.recyclerview.widget.LinearLayoutManager;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllUser extends AppCompatActivity {
    private RecyclerView rvMain;
    private RecyclerViewAdapterForUserData adapter;
    private ArrayList<ResponseApiUserData> imageList;
    private HeaderApi HeaderServerRespond;
    String txt,st,ps;


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayuserdata);
        rvMain = findViewById(R.id.svMain);

        imageList = new ArrayList<>();

        adapter = new RecyclerViewAdapterForUserData(DisplayAllUser.this, imageList);

        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setHasFixedSize(true);
        remoteGetUsers();

    }

    public void setDataToAdapter(List<ResponseApiUserData> data) {
        adapter.setData(data);
    }

    public void remoteGetUsers() {
        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.PROGRESS_TYPE);
        Call<HeaderApi> call = api.getUsers();
        call.enqueue(new Callback<HeaderApi>() {
            @Override
            public void onResponse(Call<HeaderApi> call, Response<HeaderApi> response) {
                if (response.isSuccessful() && response.body() != null) {
                    HeaderServerRespond = response.body();
                    List<ResponseApiUserData> userDataList = HeaderServerRespond.getData();
                    if (userDataList != null && !userDataList.isEmpty()) {
                        // Update the RecyclerView adapter with the list of user data
                        setDataToAdapter(userDataList);
                    } else {
                        // Handle case where userDataList is null or empty
                        Toast.makeText(getBaseContext(), "No user data received", Toast.LENGTH_LONG).show();
                    }
                } else {
                    // Handle unsuccessful response
                    Toast.makeText(getBaseContext(), "Failed to get user data", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<HeaderApi> call, Throwable t) {
                Log.d("Error", t.toString());
                // Handle the failure here
            }
        });
    }

//    private void deleteItem(String subjectId) {
//        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
//        Call<HeaderApi> call = api.deleteItem(subjectId);
//        call.enqueue(new Callback<ArrayList<ResponseApi>>() {
//            @Override
//            public void onResponse(Call<ArrayList<ResponseApi>> call, Response<ArrayList<ResponseApi>> response) {
//                if (response.isSuccessful()) {
//                    // Item deleted successfully, update the RecyclerView
//                    // Fetch updated data and update the adapter
//                    Log.d("Delete", "Item deleted successfully");
//                    imageList = response.body(); // Assuming the server returns updated list after deletion
//                    setDataToAdapter(imageList);
//                } else {
//                    // Show error message if deletion fails
//                    Log.d("Delete", "Failed to delete item");
//                    new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.WARNING_TYPE)
//                            .setTitleText("Failed")
//                            .setContentText("failed")
//                            .setConfirmText("OK")
//                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                                @Override
//                                public void onClick(SweetAlertDialog sDialog) {
//                                    sDialog.dismiss();
//                                    //finish();
//                                }
//                            }).show();
//                    // You can show an error dialog or toast here
//                }
//            }
//
//            @Override
//            public void onFailure(Call<ArrayList<ResponseApi>> call, Throwable t) {
//                // Show error message if network request fails
//                new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.WARNING_TYPE)
//                        .setTitleText("Failed")
//                        .setContentText("Network Error")
//                        .setConfirmText("OK")
//                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
//                            @Override
//                            public void onClick(SweetAlertDialog sDialog) {
//                                sDialog.dismiss();
//                                //finish();
//                            }
//                        }).show();
//                Log.d("Delete", "Network request failed: " + t.getMessage());
//                // You can show an error dialog or toast here
//            }
//        });
//    }
}
