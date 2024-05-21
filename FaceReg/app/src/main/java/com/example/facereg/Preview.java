package com.example.facereg;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.example.facereg.server.ApiClientFaceReg;
import com.example.facereg.server.FaceVerificationResponseAPI;
import com.example.facereg.server.HeaderApi;
import com.example.facereg.server.HeaderApiIMG;
import com.example.facereg.server.ResponseApi;
import com.example.facereg.server.serverApi;

import java.io.ByteArrayOutputStream;

import cn.pedant.SweetAlert.SweetAlertDialog;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Preview extends AppCompatActivity {
    private ImageView imageView;
    private Button btn;
    private Button btnExit;
    private Switch switchBtn;
    private String toDo;
    private FaceVerificationResponseAPI APIRespond;
    private EditText txtinput;
    private SweetAlertDialog pDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_preview);
        Intent intent = getIntent();
        toDo = intent.getStringExtra("BTN");

        imageView = findViewById(R.id.imageView5);
        imageView.setImageBitmap(ScanFoto.bitmap);
        btn = findViewById(R.id.btnnext);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (toDo.equals("Find")) {
                    Intent intent = new Intent(Preview.this, DisplayUserDataSingle.class);
                    intent.putExtra("BTN", toDo);
                    startActivity(intent);
                } else if (toDo.equals("Verify")) {
                    tampil_input();
                } else {
                    Intent intent = new Intent(Preview.this, NameData.class);
                    intent.putExtra("BTN", toDo);
                    startActivity(intent);
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

    private void tampil_input() {
        LayoutInflater li = LayoutInflater.from(this);
        View inputnya = li.inflate(R.layout.dialog_getnrpforverify, null);

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
                    pDialog = new SweetAlertDialog(Preview.this, SweetAlertDialog.PROGRESS_TYPE);
                    pDialog.show();
                    Bitmap result = Bitmap.createScaledBitmap(ScanFoto.bitmap, 200, 200, true);
                    String myBase64Image = encodeToBase64(result, Bitmap.CompressFormat.JPEG, 100);
                    ApiClientFaceReg api = serverApi.builder().create(ApiClientFaceReg.class);
                    ResponseApi responseApi = new ResponseApi(txtinput.getText().toString(), "", "data:image/jpeg;base64," + myBase64Image);
                    Call<FaceVerificationResponseAPI> upload = api.verify(txtinput.getText().toString(), responseApi);

                    upload.enqueue(new Callback<FaceVerificationResponseAPI>() {
                        @Override
                        public void onResponse(Call<FaceVerificationResponseAPI> call, Response<FaceVerificationResponseAPI> response) {
                            pDialog.dismiss();
                            if (response.isSuccessful() && response.body() != null) {
                                APIRespond = response.body();
                                if ("User verified.".equals(APIRespond.getMessage())) {
                                    showSuccessDialog("Hasil", "Verified");
                                } else if ("User unverified.".equals(APIRespond.getMessage())) {
                                    showWarningDialog("Error", "Not Match!");
                                }
                            } else {
                                handleErrorResponse(response);
                            }
                        }

                        @Override
                        public void onFailure(Call<FaceVerificationResponseAPI> call, Throwable t) {
                            pDialog.dismiss();
                            showErrorDialog("Can't Extract Face", "Error");
                        }
                    });
                    break;
                case DialogInterface.BUTTON_NEGATIVE:
                    // You can add actions for negative button click here if needed
                    break;
            }
        }
    };

    private void handleErrorResponse(Response<FaceVerificationResponseAPI> response) {
        String message = "Unknown error";
        if (response.code() == 500) {
            message = "Internal server error";
        } else if (response.body() == null) {
            message = "Response body is null";
        }
        showErrorDialog(message, "Error");
    }

    private void showSuccessDialog(String title, String message) {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.SUCCESS_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }).show();
    }

    private void showWarningDialog(String title, String message) {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    private void showErrorDialog(String message, String title) {
        new SweetAlertDialog(Preview.this, SweetAlertDialog.ERROR_TYPE)
                .setTitleText(title)
                .setContentText(message)
                .setConfirmText("OK")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismiss();
                        Intent intent = new Intent(Preview.this, MainActivity.class);
                        startActivity(intent);
                    }
                }).show();
    }

    private String encodeToBase64(Bitmap image, Bitmap.CompressFormat compressFormat, int quality) {
        ByteArrayOutputStream byteArrayOS = new ByteArrayOutputStream();
        image.compress(compressFormat, quality, byteArrayOS);
        return Base64.encodeToString(byteArrayOS.toByteArray(), Base64.DEFAULT);
    }
}
