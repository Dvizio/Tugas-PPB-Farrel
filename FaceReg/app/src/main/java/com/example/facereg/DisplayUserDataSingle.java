package com.example.facereg;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.app.AlertDialog;
import android.content.DialogInterface;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.HeaderApi;
import com.example.facereg.server.HeaderApiIMG;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.ResponseApiUserData;
import com.example.facereg.server.serverApi;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DisplayUserDataSingle extends AppCompatActivity {

    private Bitmap decodeFromBase64(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    private ResponseApi imageList;
    private HeaderApiIMG APIRespond;
    private String nrpUser,toDo = "Unpressed";
    private Button DeleteBtn;
    private Button HomeBtn;
    private ImageView userImage;
    private TextView userNrptxt, userNametxt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_displayuserdatasingle);
        DeleteBtn = findViewById(R.id.userdeletebutton);
        HomeBtn = findViewById(R.id.homebtn);
        userNrptxt = findViewById(R.id.userdatanrp);
        userNametxt = findViewById(R.id.userdataname);
        userImage = findViewById(R.id.userImageData);



//        svMain = findViewById(R.id.svMain);

//        imageList = new ArrayList<>();

//        adapter = new RecyclerViewAdapterForUserData(DisplayUserData.this, imageList);
//
//        svMain.setAdapter(adapter);
//        svMain.setLayoutManager(new LinearLayoutManager(this));
//        svMain.setHasFixedSize(true);
        Intent intent = getIntent();
        nrpUser = intent.getStringExtra("nrp");
        toDo = intent.getStringExtra("BTN");
        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
        remoteGetUsers(nrpUser);

        HomeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayUserDataSingle.this, MainActivity.class);
                startActivity(intent);
            }
        });

        DeleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Create a confirmation dialog
                new AlertDialog.Builder(DisplayUserDataSingle.this)
                        .setTitle("Confirmation")
                        .setMessage("Are you sure you want to delete this item?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                // User clicked the Yes button, proceed with deletion
                                performDeleteAction();
                            }
                        })
                        .setNegativeButton(android.R.string.no, null)
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        });

// Method to perform the delete action
        



    }




    public void remoteGetUsers(String nrpUser) {
        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);


        if (toDo.equals("Find")) { // Corrected string comparison
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayUserDataSingle.this);
            alertDialogBuilder.setMessage("Loading...");
            alertDialogBuilder.setCancelable(true);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.PROGRESS_TYPE);
            Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);
            String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
            ResponseApi responseApi = new ResponseApi(nrpUser, "", "data:image/jpeg;base64," + myBase64Image);
            Call<HeaderApiIMG> upload = api.find(responseApi);

            upload.enqueue(new Callback<HeaderApiIMG>() {
                @Override
                public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                    alertDialog.dismiss();
                    pDialog.dismiss();

                    if (response.isSuccessful() && response.body() != null) {
                        APIRespond = response.body();
                        imageList = APIRespond.getDataWithImg();
                        if (imageList != null) {
                            userNametxt.setText(imageList.getName());
                            userNrptxt.setText(imageList.getNrp());
                            userImage.setImageBitmap(decodeFromBase64(imageList.getImage()));
                        } else {
                            showErrorDialog("No data found");
                        }
                    } else {
                        showErrorDialog("Error: " + (response.body() != null ? response.body().toString() : "Unknown error"));
                    }
                }

                @Override
                public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                    pDialog.dismiss();
                    Log.e("API Failure", t.getMessage(), t);
                    new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Error")
                            .setContentText("Network failure: " + t.getMessage())
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                    Intent intent = new Intent(DisplayUserDataSingle.this, MainActivity.class);
                                    startActivity(intent);
                                    //finish();
                                }
                            }).show();
                }
            });

        } else { // Added else block
            final SweetAlertDialog pDialog = new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.PROGRESS_TYPE);
            final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(DisplayUserDataSingle.this);
            alertDialogBuilder.setMessage("Loading...");
            alertDialogBuilder.setCancelable(true);
            final AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();
            Call<HeaderApiIMG> call = api.getUser(nrpUser);
            call.enqueue(new Callback<HeaderApiIMG>() {
                @Override
                public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                    alertDialog.dismiss();
                    pDialog.dismiss();
                    if (response.isSuccessful() && response.body() != null) {
                        APIRespond = response.body();
                        imageList = APIRespond.getDataWithImg();
                        if (imageList != null) {
                            userNametxt.setText(imageList.getName());
                            userNrptxt.setText(imageList.getNrp());
                            userImage.setImageBitmap(decodeFromBase64(imageList.getImage()));
                        } else {
                            showErrorDialog("No data found");
                        }
                    } else {
                        showErrorDialog("Error: " + (response.body() != null ? response.body().toString() : "Unknown error"));
                    }
                }

                @Override
                public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                    alertDialog.dismiss();
                    pDialog.dismiss();
                    Log.e("API Error", t.toString());
                    showErrorDialog("Failed to make API call: " + t.getMessage());
                }
            });
        }
    }
    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }

    private void showErrorDialog(String message) {
        new SweetAlertDialog(DisplayUserDataSingle.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .setConfirmText("OK")
                .show();
    }
    private void performDeleteAction() {
        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
        Call<HeaderApiIMG> call = api.deleteItem(nrpUser);
        call.enqueue(new Callback<HeaderApiIMG>() {
            @Override
            public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                // Handle response
                if (response.isSuccessful()) {
                    // Item deleted successfully
                    Log.d("Delete", "Item deleted successfully");
                    Toast.makeText(getBaseContext(), "Item deleted successfully", Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(DisplayUserDataSingle.this, MainActivity.class);
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

}