package com.example.facereguser;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereguser.server.ApiClientFaceReg;
import com.example.facereguser.server.HeaderApiIMG;
import com.example.facereguser.server.ResponseApiName;
import com.example.facereguser.server.serverAPI;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserLoggedIn extends AppCompatActivity {
    private Button btnLogger;
    private Button btnDelete;
    private EditText txtinput;
    private SweetAlertDialog pDialog;
    private Button btnEdit,btnFindUser,btnVerifyUser;
    private String toDo,nrp,nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loggedin);
        btnLogger = findViewById(R.id.btnLogger);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        Intent intent  = getIntent();
        nrp = intent.getStringExtra("Nrp");
        nama = intent.getStringExtra("Nama");
        toDo = "FindBtn";


        btnLogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoggedIn.this, ShowLogger.class);
                intent.putExtra("Nrp" , nrp);
                startActivity(intent);
            }
        });

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tampil_input();
            }
        });
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a confirmation dialog
                new AlertDialog.Builder(UserLoggedIn.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the Yes button, proceed with deletion
                                performDeleteAction(nrp);
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });
    }
    private void performDeleteAction(String nrp) {
        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
        Call<HeaderApiIMG> call = api.deleteItem(nrp);
        call.enqueue(new Callback<HeaderApiIMG>() {
            @Override
            public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                // Handle response
                if (response.isSuccessful()) {
                    // Item deleted successfully
                    Log.d("Delete", "Item deleted successfully");
                    Toast.makeText(getBaseContext(), "Item deleted successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(UserLoggedIn.this, MainActivity.class);
                    startActivity(intent);
                } else {
                    // Show error message if deletion fails
                    Log.d("Delete", "Failed to delete item");
                    Toast.makeText(getBaseContext(), "Failed to delete item", Toast.LENGTH_LONG).show();
                    // You can show an error dialog or toast here
                }
            }

            @Override
            public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                // Handle failure to make the API call
                Log.e("Delete", "Failed to delete item", t);
            }
        });
    }
    private void tampil_input() {
        LayoutInflater li = LayoutInflater.from(this);
        View inputnya = li.inflate(R.layout.activity_getusername, null);

        androidx.appcompat.app.AlertDialog.Builder dialognya = new androidx.appcompat.app.AlertDialog.Builder(this);
        dialognya.setView(inputnya);
        txtinput = inputnya.findViewById(R.id.edittext);

        dialognya.setCancelable(false)
                .setPositiveButton("Change Picture", oknya)
                .setNegativeButton("Cancel", oknya)
                .setNeutralButton("Send",  oknya);


        dialognya.show();
    }
    DialogInterface.OnClickListener oknya = new DialogInterface.OnClickListener() {
        @Override
        public void onClick(DialogInterface dialog, int which) {
            switch (which) {
                case DialogInterface.BUTTON_POSITIVE:
                    Intent intent = new Intent(UserLoggedIn.this, ScanFoto.class);
                    toDo="Edit";
                    intent.putExtra("BTN", toDo);
                    intent.putExtra("Nrp", nrp);
                    intent.putExtra("Nama", txtinput.getText().toString());
                    startActivity(intent);
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // You can add actions for negative button click here if needed
                    dialog.dismiss();
                    break;
                case DialogInterface.BUTTON_NEUTRAL:
                    ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
                    ResponseApiName responseapiname = new ResponseApiName(nrp, txtinput.getText().toString());
                    Call<HeaderApiIMG> call = api.editUsername(nrp, responseapiname);
                    call.enqueue(new Callback<HeaderApiIMG>() {
                        @Override
                        public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                            if (response.isSuccessful()) {
                                // Item deleted successfully
                                Log.d("Patch", "Username updated successfully");
                                Toast.makeText(getBaseContext(), "Username updated successfully", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                            } else {
                                // Show error message if deletion fails
                                Log.d("Patch", "Failed to update Username");
                                Toast.makeText(getBaseContext(), "Failed to update Username", Toast.LENGTH_LONG).show();
                                dialog.dismiss();
                                // You can show an error dialog or toast here
                            }
                        }

                        @Override
                        public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                            Log.d("Patch", "API call failed: " + t.getMessage());
                            Toast.makeText(getBaseContext(), "API call failed", Toast.LENGTH_LONG).show();
                            dialog.dismiss();
                        }
                    });
                    break;
            }
        }
    };
}
