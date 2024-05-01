package com.example.facereg;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Switch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preview extends AppCompatActivity {
    private ImageView imageView;
    private Button btn;
    private Button btnExit;
    private Switch switchBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        imageView = findViewById(R.id.imageView5);
        imageView.setImageBitmap(ScanFoto.bitmap);
//        switchBtn = (Switch) findViewById(R.id.switch1);

        btn = findViewById(R.id.btnnext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (switchBtn.isChecked()) {
//                    status = "mask";
//                } else {
//                    status = "nomask";
//                }
                Intent intent = new Intent(Preview.this, NameData.class);
                startActivity(intent);
            }
        });
        btnExit = findViewById(R.id.btntake);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
