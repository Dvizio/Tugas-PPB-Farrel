package com.example.facereg;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class GetUserData extends AppCompatActivity {
    private EditText nrpUser;
    //    private EditText nama;
    private Button btnGetUser;
    String nrpSTR;
//    String namaSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getuserdata);


        nrpUser = findViewById(R.id.nrpuser);

        btnGetUser = findViewById(R.id.btngetuser);

        btnGetUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrpSTR = nrpUser.getText().toString();
//                namaSTR = nama.getText().toString();

                if (nrpSTR.trim().length() > 5) {
//                    if (namaSTR.length() == 3) {
                    Intent intent = new Intent(GetUserData.this, DisplayUserData.class);
                    intent.putExtra("nrp", nrpSTR);

                    startActivity(intent);
//                    }
//                    else {
                    Toast.makeText(GetUserData.this, "Periksa Nrp", Toast.LENGTH_SHORT).show();
                }
//                }
//                else{
//                    Toast.makeText(getBaseContext(),"Lengkapi NRP",Toast.LENGTH_LONG).show();
////                    nrp.requestFocus();
//                }
            }
        });
    }
}
