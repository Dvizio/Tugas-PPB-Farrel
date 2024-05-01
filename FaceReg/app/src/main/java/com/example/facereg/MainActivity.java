package com.example.facereg;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    private Button btnReg;
    private Button btnCheckuser;
    private Button btnDeleteUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        btnReg= findViewById(R.id.btnRegister);
        btnCheckuser = findViewById(R.id.btnCheckUser);
        btnDeleteUser = findViewById(R.id.btnDeleteUser);

        btnReg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, ScanFoto.class);
                startActivity(intent);
            }
        });
        btnCheckuser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, DisplayAllUser.class);
                startActivity(intent);
            }
        });
        btnDeleteUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, NameData.class);
                startActivity(intent);
            }
        });
    }
}