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
import android.widget.LinearLayout;
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
    private Button btnEdit, btnFindUser, btnVerifyUser, btnLogout;
    private String toDo, nrp, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_loggedin);
        btnLogger = findViewById(R.id.btnLogger);
        btnEdit = findViewById(R.id.btnEdit);
        btnDelete = findViewById(R.id.btnDelete);
        btnLogout = findViewById(R.id.btnLogout); // Initialize the logout button

        Intent intent = getIntent();
        nrp = intent.getStringExtra("Nrp");
        nama = intent.getStringExtra("Nama");
        toDo = "FindBtn";

        btnLogger.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(UserLoggedIn.this, ShowLogger.class);
                intent.putExtra("Nrp", nrp);
                intent.putExtra("Nama", nama);
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

        // Set onClickListener for logout button
        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AlertDialog.Builder(UserLoggedIn.this)
                        .setTitle("Logging Out")
                        .setMessage("Are you sure you want to Logout?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the Yes button, proceed with deletion
                                Intent intent = new Intent(UserLoggedIn.this, MainActivity.class);
                                startActivity(intent);
                                finish(); // Optionally call finish() to close the current activity
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

        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        dialogBuilder.setView(inputnya);
        txtinput = inputnya.findViewById(R.id.edittext);

        // Create the dialog
        AlertDialog dialog = dialogBuilder.create();

        // Set the button click listeners
        dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cancel", (dialogInterface, i) -> {
            // Negative button (Cancel) click handler
            dialog.dismiss();
        });

        dialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Send", (dialogInterface, i) -> {
            // Neutral button (Send) click handler
            ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
            ResponseApiName responseapiname = new ResponseApiName(nrp, txtinput.getText().toString());
            Call<HeaderApiIMG> call = api.editUsername(nrp, responseapiname);
            call.enqueue(new Callback<HeaderApiIMG>() {
                @Override
                public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                    if (response.isSuccessful()) {
                        Log.d("Patch", "Username updated successfully");
                        Toast.makeText(getBaseContext(), "Username updated successfully", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    } else {
                        Log.d("Patch", "Failed to update Username");
                        Toast.makeText(getBaseContext(), "Failed to update Username", Toast.LENGTH_LONG).show();
                        dialog.dismiss();
                    }
                }

                @Override
                public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                    Log.d("Patch", "API call failed: " + t.getMessage());
                    Toast.makeText(getBaseContext(), "API call failed", Toast.LENGTH_LONG).show();
                    dialog.dismiss();
                }
            });
        });

        dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Change Picture", (dialogInterface, i) -> {
            // Positive button (Change Picture) click handler
            Intent intent = new Intent(UserLoggedIn.this, ScanFoto.class);
            toDo = "Edit";
            intent.putExtra("BTN", toDo);
            intent.putExtra("Nrp", nrp);
            intent.putExtra("Nama", txtinput.getText().toString());
            startActivity(intent);
        });

        dialog.show();

        // Ensure buttons are ordered as Cancel, Send, Change Picture
        Button negativeButton = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        Button neutralButton = dialog.getButton(DialogInterface.BUTTON_NEUTRAL);
        Button positiveButton = dialog.getButton(DialogInterface.BUTTON_POSITIVE);

        // Adjust the layout to ensure buttons are in desired order
        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) positiveButton.getLayoutParams();
        params.weight = 10;
        positiveButton.setLayoutParams(params);

        LinearLayout parent = (LinearLayout) negativeButton.getParent();
        parent.removeView(neutralButton);
        parent.removeView(positiveButton);
        parent.addView(positiveButton);
        parent.addView(neutralButton);
    }
}
