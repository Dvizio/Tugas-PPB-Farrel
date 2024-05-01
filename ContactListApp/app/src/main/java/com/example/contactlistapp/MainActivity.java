package com.example.contactlistapp;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private ListView lv;
    private ImageView add;
    private SQLiteDatabase dbku;
    private SQLiteOpenHelper dbopen;
    private kontakAdapter kAdapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        lv = (ListView) findViewById(R.id.listView);
        add = (ImageView) findViewById(R.id.tambah);
        add.setOnClickListener(operasi);

        ArrayList<kontak> listKontak = new ArrayList<kontak>();
        kAdapter= new kontakAdapter(this,0,listKontak);
        lv.setAdapter(kAdapter);

        dbopen = new SQLiteOpenHelper(this, "kontak.db", null, 1) {
            @Override
            public void onCreate(SQLiteDatabase sqLiteDatabase) {

            }

            @Override
            public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

            }
        };
        dbku=dbopen.getWritableDatabase();
        dbku.execSQL("CREATE TABLE IF NOT EXISTS kontak(nama TEXT, nohp TEXT);");
        ambildata();
    }

    View.OnClickListener operasi = new View.OnClickListener(){
        @Override
        public void onClick(View v){
            switch (v.getId()){
                case R.id.tambah:tambah_data();break;
            }
        }
    };

    private void add_item(String nm, String hp){
        ContentValues datanya = new ContentValues();
        datanya.put("nama",nm);
        datanya.put("nohp", hp);
        dbku.insert("kontak", null, datanya);
        kontak newKontak = new kontak(nm,hp);
        kAdapter.add(newKontak);
    }
    private void insertKontak(Stringnm,String hp){
        kontak newKontak = new kontak(nm,hp);
        kAdapter.add(newKontak);
    }

    private void ambildata(){
        Cursor cur = dbku.rawQuery("SELECT * FROM kontak",null);
        Toast.makeText(this,"Terdapat sejumlah " + cur.getCount(),Toast.LENGTH_LONG).show();
        int i=0;if(cur.getCount() > 0) cur.moveToFirst();
        while(i<cur.getCount()){
            insertKontak(cur.getString(cur.getColumnIndex("nama")),cur.getString(cur.getColumnIndex("nohp")));
            cur.moveToNext();i++;
        }
    }
    private void tambah_data(){
        AlertDialog.Builder buat = new AlertDialog.Builder(this);
        buat.setTitle("Add Kontak");

        View vAdd = LayoutInflater.from(this).inflate(R.layout.add_kontak,null);
        final EditText nm = (EditText) vAdd.findViewById(R.id.nm);
        final EditText hp = (EditText) vAdd.findViewById(R.id.hp);

        buat.setView(vAdd);
        buat.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                add_item(nm.getText().toString(),hp.getText().toString());
                Toast.makeText(getBaseContext(),"Data Tersimpan", Toast.LENGTH_LONG).show();
                dialog.dismiss();
            }
        });
        buat.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialog.cancel();
            }
        });
        buat.show();
    }
}