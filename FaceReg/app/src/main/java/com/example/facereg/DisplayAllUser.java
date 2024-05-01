package com.example.facereg;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;
import androidx.recyclerview.widget.LinearLayoutManager;

import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayAllUser extends AppCompatActivity {
    private RecyclerView rvMain;
    private RecyclerViewAdapter adapter;
    private ArrayList<ResponseApi> imageList;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;
    String txt,st,ps;
    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(String subjectId);
    }

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayuser);
        rvMain = findViewById(R.id.rvMain);

        imageList = new ArrayList<>();

        adapter = new RecyclerViewAdapter(DisplayAllUser.this, imageList, new RecyclerViewAdapter.OnDeleteButtonClickListener() {
            @Override
            public void onDeleteButtonClick(String subjectId) {
                deleteItem(subjectId);
            }
        });

        rvMain.setAdapter(adapter);
        rvMain.setLayoutManager(new LinearLayoutManager(this));
        rvMain.setHasFixedSize(true);
        remoteGetUsers();

    }

    public void setDataToAdapter(ArrayList<ResponseApi> data){
        adapter.setData(data);
    }

    public void remoteGetUsers() {
        ApiClientFaceReg api =  serverApi.builder().create(ApiClientFaceReg.class);
        final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.PROGRESS_TYPE);
        Call<ArrayList<ResponseApi>> call = api.getUsers();
        call.enqueue(new Callback<ArrayList<ResponseApi>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseApi>> call, Response<ArrayList<ResponseApi>> response) {
                if (response.isSuccessful() && response.body() != null) {
                    imageList = response.body();
                    setDataToAdapter(imageList);
                    // Handle the data here
                } else {
                    pDialog.dismiss();
                    new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.WARNING_TYPE)
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

    private void deleteItem(String subjectId) {
        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
        Call<ArrayList<ResponseApi>> call = api.deleteItem(subjectId);
        call.enqueue(new Callback<ArrayList<ResponseApi>>() {
            @Override
            public void onResponse(Call<ArrayList<ResponseApi>> call, Response<ArrayList<ResponseApi>> response) {
                if (response.isSuccessful()) {
                    // Item deleted successfully, update the RecyclerView
                    // Fetch updated data and update the adapter
                    Log.d("Delete", "Item deleted successfully");
                    imageList = response.body(); // Assuming the server returns updated list after deletion
                    setDataToAdapter(imageList);
                } else {
                    // Show error message if deletion fails
                    Log.d("Delete", "Failed to delete item");
                    new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.WARNING_TYPE)
                            .setTitleText("Failed")
                            .setContentText("failed")
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    //finish();
                                }
                            }).show();
                    // You can show an error dialog or toast here
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ResponseApi>> call, Throwable t) {
                // Show error message if network request fails
                new SweetAlertDialog(DisplayAllUser.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Failed")
                        .setContentText("Network Error")
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                //finish();
                            }
                        }).show();
                Log.d("Delete", "Network request failed: " + t.getMessage());
                // You can show an error dialog or toast here
            }
        });
    }
}
