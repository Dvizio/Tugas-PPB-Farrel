package com.example.facereguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Button btnLogin,btnFindUser,btnReg;

    private String toDo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg= findViewById(R.id.btnRegister);
        btnLogin = findViewById(R.id.btnLogin);
        btnFindUser = findViewById(R.id.findBTN);
        toDo = "FindBtn";


        btnFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanFoto.class);
                toDo = "Find";
                intent.putExtra("BTN" , toDo);
                intent.putExtra("Nrp" , "");
                intent.putExtra("Name" , "");
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NameData.class);
                toDo = "Register";
                intent.putExtra("BTN" , toDo);
                startActivity(intent);
            }
        });
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NameData.class);
                toDo= "Verify";
                intent.putExtra("BTN" , toDo);
                startActivity(intent);
            }
        });
    }
}