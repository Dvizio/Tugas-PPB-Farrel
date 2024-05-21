package com.example.facereg;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Base64;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.Face;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;
import java.io.ByteArrayOutputStream;
import java.util.List;
public class NameData extends AppCompatActivity {
    private EditText nrp;
    private EditText nama;
    private Button btnSend;
    String nrpSTR;
    String namaSTR;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_namedata);


        nrp = findViewById(R.id.nrp);
        nama = findViewById(R.id.nama);
        btnSend = findViewById(R.id.btnsend);

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrpSTR = nrp.getText().toString();
                namaSTR = nama.getText().toString();

                if (nrpSTR.trim().length() > 5) {
                    if (namaSTR.length() > 6) {
                        sendImg(nrpSTR, namaSTR);
                    }
                    else {
                        Toast.makeText(NameData.this, "Periksa Nama", Toast.LENGTH_SHORT).show();
                    }
                }
                else{
                    Toast.makeText(getBaseContext(),"Lengkapi NRP",Toast.LENGTH_LONG).show();
//                    nrp.requestFocus();
                }
            }
        });
    }

    protected void sendImg(String txt, String ps) {
        final SweetAlertDialog pDialog = new SweetAlertDialog(NameData.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();

        Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);

        // Create an InputImage object from the Bitmap
        InputImage image = InputImage.fromBitmap(result, 0);

        // Set up FaceDetectorOptions for performance
        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();

        // Get an instance of FaceDetector
        FaceDetector detector = FaceDetection.getClient(options);

        // Process the image using the detector
        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        // Face(s) detected, proceed to send the image to the server
                        String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                        ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
                        ResponseApi responseApi = new ResponseApi(txt, ps, "data:image/jpeg;base64," + myBase64Image);
                        Call<ResponseApi> upload = api.kirim(responseApi);

                        upload.enqueue(new Callback<ResponseApi>() {
                            @Override
                            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                                pDialog.dismiss();
                                if (response.code() == 200) {
                                    new SweetAlertDialog(NameData.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Hasil")
                                            .setContentText(response.body().getNrp())
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                    Intent intent = new Intent(NameData.this, MainActivity.class);
                                                    startActivity(intent);
                                                    if (response.body().toString().contains("Accepted")) {
                                                        finish();
                                                    }
                                                }
                                            }).show();
                                } else {
                                    new SweetAlertDialog(NameData.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Error")
                                            .setContentText(response.toString())
                                            .setConfirmText("OK")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    sDialog.dismiss();
                                                }
                                            }).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseApi> call, Throwable t) {
                                pDialog.dismiss();

                                // Create a custom dialog with a ScrollView
                                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(NameData.this);
                                alertDialogBuilder.setTitle("Hasil");
                                alertDialogBuilder.setCancelable(false);

                                // Create a ScrollView and add it to the dialog
                                ScrollView scrollView = new ScrollView(NameData.this);
                                alertDialogBuilder.setView(scrollView);

                                // Create a TextView to display the error message within the ScrollView
                                TextView errorMessageTextView = new TextView(NameData.this);
                                errorMessageTextView.setText(t.getMessage());
                                scrollView.addView(errorMessageTextView);

                                alertDialogBuilder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        dialogInterface.dismiss();
                                    }
                                });

                                // Show the custom dialog
                                AlertDialog alertDialog = alertDialogBuilder.create();
                                alertDialog.show();
                            }
                        });
                    } else {
                        // No face detected, show an error message
                        pDialog.dismiss();
                        new SweetAlertDialog(NameData.this, SweetAlertDialog.ERROR_TYPE)
                                .setTitleText("No Face Detected")
                                .setContentText("Please take a picture with a clear view of your face.")
                                .setConfirmText("OK")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        sDialog.dismiss();
                                    }
                                }).show();
                    }
                })
                .addOnFailureListener(e -> {
                    // Handle any errors in face detection
                    pDialog.dismiss();
                    new SweetAlertDialog(NameData.this, SweetAlertDialog.ERROR_TYPE)
                            .setTitleText("Face Detection Error")
                            .setContentText(e.getMessage())
                            .setConfirmText("OK")
                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                @Override
                                public void onClick(SweetAlertDialog sDialog) {
                                    sDialog.dismiss();
                                }
                            }).show();
                });
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
