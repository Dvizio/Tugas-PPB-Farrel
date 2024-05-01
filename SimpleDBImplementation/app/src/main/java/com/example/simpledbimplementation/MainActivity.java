package com.example.simpledbimplementation;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private static final int BUTTON_SIMPAN = 1;
    private static final int BUTTON_AMBILDATA = 2;
    private static final int BUTTON_UPDATE = 3;
    private static final int BUTTON_DELETE = 4;

    private EditText nrp, nama;
    private Button buttonSimpan, buttonAmbilData, buttonUpdate, buttonDelete;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper openDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        nrp = findViewById(R.id.nrp);
        nama = findViewById(R.id.nama);
        buttonSimpan = findViewById(R.id.simpan);
        buttonAmbilData = findViewById(R.id.ambildata);
        buttonUpdate = findViewById(R.id.update);
        buttonDelete = findViewById(R.id.delete);

        buttonSimpan.setTag(BUTTON_SIMPAN);
        buttonAmbilData.setTag(BUTTON_AMBILDATA);
        buttonUpdate.setTag(BUTTON_UPDATE);
        buttonDelete.setTag(BUTTON_DELETE);

        buttonSimpan.setOnClickListener(operasi);
        buttonAmbilData.setOnClickListener(operasi);
        buttonUpdate.setOnClickListener(operasi);
        buttonDelete.setOnClickListener(operasi);

        openDb = new SQLiteOpenHelper(this, "db .sql", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase db) {}

            @Override
            public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {}
        };

        dbku = openDb.getWritableDatabase();
        dbku.execSQL("CREATE TABLE IF NOT EXISTS mhs(nrp TEXT, nama TEXT);");
    }

    @Override
    protected void onStop() {
        dbku.close();
        openDb.close();
        super.onStop();
    }

    View.OnClickListener operasi = new View.OnClickListener() {
        @Override
        public void onClick(View view) {
            int id = (int) view.getTag();
            switch (id) {
                case BUTTON_SIMPAN:
                    simpan();
                    break;
                case BUTTON_AMBILDATA:
                    ambildata();
                    break;
                case BUTTON_UPDATE:
                    update();
                    break;
                case BUTTON_DELETE:
                    delete();
                    break;
            }
        }
    };

    private void simpan() {
        String nrpValue = nrp.getText().toString();
        String namaValue = nama.getText().toString();

        // Check if the NRP already exists in the database
        Cursor cursor = dbku.rawQuery("SELECT * FROM mhs WHERE nrp=?", new String[]{nrpValue});
        if (cursor.getCount() > 0) {
            Toast.makeText(this, "NRP already exists", Toast.LENGTH_LONG).show();
        } else {
            // NRP is unique, proceed with insertion
            ContentValues dataku = new ContentValues();
            dataku.put("nrp", nrpValue);
            dataku.put("nama", namaValue);
            dbku.insert("mhs", null, dataku);
            Toast.makeText(this, "Data Tersimpan", Toast.LENGTH_LONG).show();
        }
        cursor.close();
    }

    private void ambildata() {
        Cursor cur = dbku.rawQuery("SELECT nama FROM mhs WHERE nrp='" + nrp.getText().toString() + "'", null);
        if (cur.moveToFirst()) {
            String nama = cur.getString(cur.getColumnIndex("nama"));
            Toast.makeText(this, "Nama: " + nama, Toast.LENGTH_LONG).show();
        } else {
            Toast.makeText(this, "Data Tidak Ditemukan", Toast.LENGTH_LONG).show();
        }
        cur.close();
    }

    private void update() {
        ContentValues dataku = new ContentValues();
        dataku.put("nrp", nrp.getText().toString());
        dataku.put("nama", nama.getText().toString());
        dbku.update("mhs", dataku, "nrp='" + nrp.getText().toString() + "'", null);
        Toast.makeText(this, "Data Telah Diupdate", Toast.LENGTH_LONG).show();
    }

    private void delete() {
        dbku.delete("mhs", "nrp='" + nrp.getText().toString() + "'", null);
        Toast.makeText(this, "Data Telah Terhapus", Toast.LENGTH_LONG).show();
    }
}
