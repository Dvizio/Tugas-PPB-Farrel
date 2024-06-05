package com.example.facereguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class NameData extends AppCompatActivity {
    private EditText nrp;
    private EditText nama;
    private Button btnSend;
    String nrpSTR;
    String namaSTR;
    private String toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namedata);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.your_color));

        Intent intent = getIntent();
        toDo = intent.getStringExtra("BTN");
        nrp = findViewById(R.id.nrp);
        nama = findViewById(R.id.nama);
        btnSend = findViewById(R.id.btnsend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrpSTR = nrp.getText().toString();
                namaSTR = nama.getText().toString();

                if (nrpSTR.trim().length() > 5) {
                    if (namaSTR.length() > 6) {
                        Intent intent = new Intent(NameData.this, ScanFoto.class);
                        intent.putExtra("BTN" , toDo);
                        intent.putExtra("Nrp" , nrpSTR);
                        intent.putExtra("Nama" , namaSTR);
                        startActivity(intent);
                    }
                    else {
                        Toast.makeText(NameData.this, "Periksa Nama", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Lengkapi NRP",Toast.LENGTH_LONG).show();
//                    nrp.requestFocus();
                }
            }
        });
    }


}
