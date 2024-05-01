package com.example.simpledialogbox;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class MainActivity extends AppCompatActivity {
    private TextView hasil;
    private EditText txtinput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        hasil = findViewById(R.id.result);
        Button btn = findViewById(R.id.button);
        btn.setOnClickListener(op);
    }

    View.OnClickListener op = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.button) {
                tampil_input();
            }
        }
    };

    private void tampil_input() {
        LayoutInflater li = LayoutInflater.from(this);
        View inputnya = li.inflate(R.layout.input_dialog, null);

        AlertDialog.Builder dialognya = new AlertDialog.Builder(this);
        dialognya.setView(inputnya);
        txtinput = inputnya.findViewById(R.id.edittext);

        dialognya.setCancelable(false).setPositiveButton("OK", oknya).setNegativeButton("batal", oknya);
        dialognya.show();
    }

    DialogInterface.OnClickListener oknya = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    hasil.setText(txtinput.getText().toString());
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // You can add actions for negative button click here if needed
                    break;
            }
        }
    };
}
