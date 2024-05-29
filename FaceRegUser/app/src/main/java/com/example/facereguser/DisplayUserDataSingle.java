package com.example.facereguser;

import android.app.AlertDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereguser.server.*;


import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayUserDataSingle extends AppCompatActivity {

    private Bitmap decodeFromBase64(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private UserData userData;
    private UserResponse apiResponse;
    private String nrpUser, toDo = "Unpressed";
    private Button loginButton;
    private Button homeBtn;
    private ImageView userImage;
    private TextView userNrpTxt, userNameTxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayuserdatasingle);
        loginButton = findViewById(R.id.userdeletebutton);
        homeBtn = findViewById(R.id.homebtn);
        userNrpTxt = findViewById(R.id.userdatanrp);
        userNameTxt = findViewById(R.id.userdataname);
        userImage = findViewById(R.id.userImageData);

        Intent intent = getIntent();
        toDo = intent.getStringExtra("BTN");

        remoteGetUsers();

        homeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayUserDataSingle.this, MainActivity.class);
                startActivity(intent);
            }
        });

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(DisplayUserDataSingle.this, NameData.class);
                toDo = "Verify";
                intent.putExtra("BTN", toDo);
                startActivity(intent);
            }
        });
    }

    public void remoteGetUsers() {
        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayUserDataSingle.this);
        alertDialogBuilder.setMessage("Loading...");
        alertDialogBuilder.setCancelable(true);
        final AlertDialog alertDialog = alertDialogBuilder.create();
        alertDialog.show();

        final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.PROGRESS_TYPE);
        Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);
        String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
        ResponseApi responseApi = new ResponseApi("", "", "data:image/jpeg;base64," + myBase64Image);
        Call<UserResponse> upload = api.find(responseApi);

        upload.enqueue(new Callback<UserResponse>() {
            @Override
            public void onResponse(Call<UserResponse> call, Response<UserResponse> response) {
                alertDialog.dismiss();
                pDialog.dismiss();

                if (response.isSuccessful() && response.body() != null) {
                    apiResponse = response.body();
                    userData = apiResponse.getData();
                    if (userData != null) {
                        userNameTxt.setText(userData.getName());
                        userNrpTxt.setText("Found!");
                        userImage.setImageBitmap(decodeFromBase64(userData.getImage()));
                    } else {
                        showErrorDialog("No data found");
                    }
                } else {
                    showErrorDialog("Error: " + (response.body() != null ? response.body().toString() : "Unknown error"));
                }
            }

            @Override
            public void onFailure(Call<UserResponse> call, Throwable t) {
                pDialog.dismiss();
                Log.e("API Failure", t.getMessage(), t);
                new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("Error")
                        .setContentText("Network failure: " + t.getMessage())
                        .setConfirmText("OK")
                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                            @Override
                            public void onClick(SweetAlertDialog sDialog) {
                                sDialog.dismiss();
                                Intent intent = new Intent(DisplayUserDataSingle.this, MainActivity.class);
                                startActivity(intent);
                            }
                        }).show();
            }
        });
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void showErrorDialog(String message) {
        new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .setConfirmText("OK")
                .show();
    }
}
