//package com.example.hitungluas;
//
//import androidx.appcompat.app.AppCompatActivity;
//
//import android.os.Bundle;
//
//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//    }
//}
package com.example.hitungluas;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {
    private EditText edtPanjang, edtLebar;
    private Button btnHitung;
    private TextView txtLuas;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//        Objects.requireNonNull(getSupportActionBar()).setTitle("Hitung Luas Persegi Panjang");

        edtPanjang = findViewById(R.id.edt_panjang);
        edtLebar = findViewById(R.id.edt_lebar);
        btnHitung = findViewById(R.id.btn_hitung);
        txtLuas = findViewById(R.id.txt_luas);

        btnHitung.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String panjang = edtPanjang.getText().toString().trim();
                String lebar = edtLebar.getText().toString().trim();

                double p = Double.parseDouble(panjang);
                double l = Double.parseDouble(lebar);

                double luas = p * l;

                txtLuas.setText("Luas : "+luas);
            }
        });
    }

}