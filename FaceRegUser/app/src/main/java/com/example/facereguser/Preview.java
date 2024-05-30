package com.example.facereguser;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereguser.server.ApiClientFaceReg;
import com.example.facereguser.server.FaceVerificationResponseAPI;
import com.example.facereguser.server.HeaderApiIMG;
import com.example.facereguser.server.ResponseApi;
import com.example.facereguser.server.serverAPI;
import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.face.FaceDetection;
import com.google.mlkit.vision.face.FaceDetector;
import com.google.mlkit.vision.face.FaceDetectorOptions;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preview extends AppCompatActivity {
    private ImageView imageView;
    private Button btnNext;
    private Button btnExit;
    private Switch switchBtn;
    private String toDo, nrp, nama;
    private FaceVerificationResponseAPI APIRespond;
    private EditText txtinput;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);

        // Retrieve the intent extras
        Intent intent = getIntent();
        toDo = intent.getStringExtra("BTN");

        // Initialize UI elements
        imageView = findViewById(R.id.imageView5);
        imageView.setImageBitmap(ScanFoto.bitmap);

        btnNext = findViewById(R.id.btnnext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nrp = intent.getStringExtra("Nrp");
                nama = intent.getStringExtra("Nama");

                switch (toDo) {
                    case "Verify":
                        sendVerification(nrp, nama);
                        break;
                    case "Register":
                        sendImg(nrp, nama);
                        break;
                    case "Find":
                        Intent findIntent = new Intent(Preview.this, DisplayUserDataSingle.class);
                        startActivity(findIntent);
                        break;
                    case "Edit":
                        editUser(nrp, nama);
                        break;
                }
            }
        });

        btnExit = findViewById(R.id.btntake);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // Send verification request
    protected void sendVerification(String nrp, String nama) {
        showProgressDialog();

        Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);
        InputImage image = InputImage.fromBitmap(result, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
                        ResponseApi responseApi = new ResponseApi(nrp, nama, "data:image/jpeg;base64," + myBase64Image);
                        Call<FaceVerificationResponseAPI> upload = api.verify(nrp, responseApi);

                        upload.enqueue(new Callback<FaceVerificationResponseAPI>() {
                            @Override
                            public void onResponse(Call<FaceVerificationResponseAPI> call, Response<FaceVerificationResponseAPI> response) {
                                pDialog.dismiss();
                                handleVerificationResponse(response);
                            }

                            @Override
                            public void onFailure(Call<FaceVerificationResponseAPI> call, Throwable t) {
                                pDialog.dismiss();
                                showErrorDialog(t.getMessage());
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        showNoFaceDetectedDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    pDialog.dismiss();
                    showFaceDetectionErrorDialog(e.getMessage());
                });
    }

    // Send image for registration
    protected void sendImg(String nrp, String nama) {
        showProgressDialog();

        Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);
        InputImage image = InputImage.fromBitmap(result, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
                        ResponseApi responseApi = new ResponseApi(nrp, nama, "data:image/jpeg;base64," + myBase64Image);
                        Call<ResponseApi> upload = api.kirim(responseApi);

                        upload.enqueue(new Callback<ResponseApi>() {
                            @Override
                            public void onResponse(Call<ResponseApi> call, Response<ResponseApi> response) {
                                pDialog.dismiss();
                                handleRegistrationResponse(response);
                            }

                            @Override
                            public void onFailure(Call<ResponseApi> call, Throwable t) {
                                pDialog.dismiss();
                                showErrorDialog(t.getMessage());
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        showNoFaceDetectedDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    pDialog.dismiss();
                    showFaceDetectionErrorDialog(e.getMessage());
                });
    }

    // Edit user details
    protected void editUser(String nrp, String nama) {
        showProgressDialog();

        Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 300, 400, true);
        InputImage image = InputImage.fromBitmap(result, 0);

        FaceDetectorOptions options = new FaceDetectorOptions.Builder()
                .setPerformanceMode(FaceDetectorOptions.PERFORMANCE_MODE_FAST)
                .build();
        FaceDetector detector = FaceDetection.getClient(options);

        detector.process(image)
                .addOnSuccessListener(faces -> {
                    if (!faces.isEmpty()) {
                        String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);
                        ResponseApi responseApi = new ResponseApi(nrp, nama, "data:image/jpeg;base64," + myBase64Image);
                        Call<HeaderApiIMG> upload = api.editUser(nrp, responseApi);

                        upload.enqueue(new Callback<HeaderApiIMG>() {
                            @Override
                            public void onResponse(Call<HeaderApiIMG> call, Response<HeaderApiIMG> response) {
                                pDialog.dismiss();
                                handleEditResponse(response);
                            }

                            @Override
                            public void onFailure(Call<HeaderApiIMG> call, Throwable t) {
                                pDialog.dismiss();
                                showErrorDialog(t.getMessage());
                            }
                        });
                    } else {
                        pDialog.dismiss();
                        showNoFaceDetectedDialog();
                    }
                })
                .addOnFailureListener(e -> {
                    pDialog.dismiss();
                    showFaceDetectionErrorDialog(e.getMessage());
                });
    }

    // Show progress dialog
    private void showProgressDialog() {
        pDialog = new SweetAlertDialog(Preview.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.parseColor("#A5DC86"));
        pDialog.setTitleText("Loading");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    // Handle verification response
    private void handleVerificationResponse(Response<FaceVerificationResponseAPI> response) {
        FaceVerificationResponseAPI verificationData = response.body();
        if (verificationData.getData().isVerified()) {
                new SweetAlertDialog(Preview.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("Success")
                        .setContentText("Face Verified")
                        .setConfirmText("OK")
                        .setConfirmClickListener(sDialog -> {
                            sDialog.dismiss();
                            Intent intent = new Intent(Preview.this, UserLoggedIn.class);
                            toDo = "LoggedIn";
                            intent.putExtra("BTN", toDo);
                            intent.putExtra("Nrp", nrp);
                            intent.putExtra("Nama", nama);
                            startActivity(intent);
                            finish();
                        }).show();
            } else {
            new SweetAlertDialog(Preview.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Error")
                    .setContentText("Face Doesn't Match!")
                    .setConfirmText("OK")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                    }).show();
        }
    }

    // Handle registration response
    private void handleRegistrationResponse(Response<ResponseApi> response) {
        if (response.code() == 200) {
            new SweetAlertDialog(Preview.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Please Verify")
                    .setContentText(response.body().getNrp())
                    .setConfirmText("OK")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, ScanFoto.class);
                        toDo = "Verify";
                        intent.putExtra("BTN", toDo);
                        intent.putExtra("Nrp", nrp);
                        intent.putExtra("Name", nama);
                        startActivity(intent);
                        finish();
                    }).show();
        } else {
            new SweetAlertDialog(Preview.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Error")
                    .setContentText(response.toString())
                    .setConfirmText("OK")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                    }).show();
        }
    }

    // Handle edit response
    private void handleEditResponse(Response<HeaderApiIMG> response) {
        if (response.code() == 200) {
            new SweetAlertDialog(Preview.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("Face Data Added")
                    .setContentText("User Data Edited")
                    .setConfirmText("OK")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, UserLoggedIn.class);
                        toDo = "LoggedIn";
                        intent.putExtra("BTN", toDo);
                        intent.putExtra("Nrp", nrp);
                        intent.putExtra("Name", nama);
                        startActivity(intent);
                    }).show();
        } else {
            new SweetAlertDialog(Preview.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("Error")
                    .setContentText(response.toString())
                    .setConfirmText("OK")
                    .setConfirmClickListener(sDialog -> {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                    }).show();
        }
    }

    // Show error dialog
    private void showErrorDialog(String message) {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Error")
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(SweetAlertDialog::dismiss).show();
    }

    // Show no face detected dialog
    private void showNoFaceDetectedDialog() {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("No Face Detected")
                .setContentText("No face detected in the image. Please try again.")
                .setConfirmText("OK")
                .setConfirmClickListener(SweetAlertDialog::dismiss).show();
    }

    // Show face detection error dialog
    private void showFaceDetectionErrorDialog(String message) {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText("Face Detection Error")
                .setContentText("Error detecting face in the image. Please try again.")
                .setConfirmText("OK")
                .setConfirmClickListener(SweetAlertDialog::dismiss).show();
    }

    // Encode image to Base64 string
    public String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
