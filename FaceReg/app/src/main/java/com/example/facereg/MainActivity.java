package com.example.facereg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnReg;
    private Button btnCheckuser;
    private Button btnDisplayalluser,btnFindUser,btnVerifyUser;
    private String toDo = "FindBtn";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg= findViewById(R.id.btnRegister);
        btnCheckuser = findViewById(R.id.btnCheckUser);
        btnDisplayalluser = findViewById(R.id.btnDeleteUser);
        btnFindUser = findViewById(R.id.findBTN);
        btnVerifyUser = findViewById(R.id.verifyBTN);

        btnVerifyUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanFoto.class);
                toDo = "Verify";
                intent.putExtra("BTN", toDo);
                startActivity(intent);
            }
        });
        btnFindUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ScanFoto.class);
                toDo = "Find";
                intent.putExtra("BTN" , toDo);
                startActivity(intent);
            }
        });

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanFoto.class);
                intent.putExtra("BTN" , toDo);
                startActivity(intent);
            }
        });
        btnCheckuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, GetUserData.class);
                intent.putExtra("BTN" , toDo);
                startActivity(intent);
            }
        });
        btnDisplayalluser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayAllUser.class);
                startActivity(intent);
            }
        });
    }
}