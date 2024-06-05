package com.example.facereguser;

import static android.Manifest.permission.CAMERA;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.vision.CameraSource;
import com.google.android.gms.vision.face.FaceDetector;
import com.google.android.gms.vision.face.LargestFaceFocusingProcessor;

import java.io.IOException;
import java.util.ArrayList;

public class ScanFoto extends AppCompatActivity implements SurfaceHolder.Callback, CameraSource.PictureCallback{
    public static final int CAMERA_REQUEST = 101;
    public static Bitmap bitmap;
    private SurfaceHolder surfaceHolder;
    private SurfaceView surfaceView;
    private String[] neededPermissions = new String[]{CAMERA};
    private FaceDetector detector;
    private CameraSource cameraSource;
    private String toDo,namastr,nrpstr;
    private Button btnExit;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scan_foto);

        Intent intent = getIntent();
        toDo = intent.getStringExtra("BTN");
        namastr= intent.getStringExtra("Nama");
        nrpstr= intent.getStringExtra("Nrp");
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.your_color));



        surfaceView = findViewById(R.id.surfaceView);
        btnExit = findViewById(R.id.btnExit);
        btnExit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
                //System.exit(1);
                //MainActivity.this.finish();
                android.os.Process.killProcess(android.os.Process.myPid());
                System.exit(1);
            }
        });

        detector = new FaceDetector.Builder(this)
                .setProminentFaceOnly(true) // optimize for single, relatively large face
                .setTrackingEnabled(true) // enable face tracking
                .setClassificationType(/* eyes open and smile */ FaceDetector.ALL_CLASSIFICATIONS)
                .setMode(FaceDetector.FAST_MODE) // for one face this is OK
                .build();

        if (!detector.isOperational()) {
            Log.w("ScanFoto", "Detector Dependencies are not yet available");
        } else {
            Log.w("ScanFoto", "Detector Dependencies are available");
            if (surfaceView != null) {
                boolean result = checkPermission();
                if (result) {
                    setViewVisibility(R.id.tv_capture);
                    setViewVisibility(R.id.surfaceView);
                    setupSurfaceHolder();
                }
            }
        }
    }
    private boolean checkPermission() {
        ArrayList<String> permissionsNotGranted = new ArrayList<>();
        for (String permission : neededPermissions) {
            if (ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED) {
                permissionsNotGranted.add(permission);
            }
        }
        if (!permissionsNotGranted.isEmpty()) {
            boolean shouldShowAlert = false;
            for (String permission : permissionsNotGranted) {
                shouldShowAlert = ActivityCompat.shouldShowRequestPermissionRationale(this, permission);
            }
            if (shouldShowAlert) {
                showPermissionAlert(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
            } else {
                requestPermissions(permissionsNotGranted.toArray(new String[permissionsNotGranted.size()]));
            }
            return false;
        }
        return true;
    }
    private void showPermissionAlert(final String[] permissions) {
        AlertDialog.Builder alertBuilder = new AlertDialog.Builder(this);
        alertBuilder.setCancelable(true);
        alertBuilder.setTitle("Permission Required");
        alertBuilder.setMessage("You must grant permission to access camera to run this application.");
        alertBuilder.setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                requestPermissions(permissions);
            }
        });
        AlertDialog alert = alertBuilder.create();
        alert.show();
    }

    private void requestPermissions(String[] permissions) {
        ActivityCompat.requestPermissions(ScanFoto.this, permissions, CAMERA_REQUEST);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == CAMERA_REQUEST) {
            for (int result : grantResults) {
                if (result == PackageManager.PERMISSION_DENIED) {
                    Toast.makeText(ScanFoto.this, "All permissions are required.", Toast.LENGTH_LONG).show();
                    //setViewVisibility(R.id.showPermissionMsg);
                    checkPermission();
                    return;
                }
            }
            setViewVisibility(R.id.tv_capture);
            setViewVisibility(R.id.surfaceView);
            setupSurfaceHolder();
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void setViewVisibility(int id) {
        View view = findViewById(id);
        if (view != null) {
            view.setVisibility(View.VISIBLE);
        }
    }
    private void setupSurfaceHolder() {
        cameraSource = new CameraSource.Builder(this, detector)
                .setFacing(CameraSource.CAMERA_FACING_FRONT)
                .setRequestedFps(2.0f)
                .setAutoFocusEnabled(true)
                .build();

        surfaceHolder = surfaceView.getHolder();
        surfaceHolder.addCallback(this);
    }

    public void captureImage() {
        // We add a delay of 200ms so that image captured is stable.
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        clickImage();
                    }
                });
            }
        }, 5); //200ms
    }

    private void clickImage() {
        if (cameraSource != null) {
            Toast.makeText(getBaseContext(),"Blink detected, take a picture",Toast.LENGTH_SHORT).show();
            cameraSource.takePicture(null, this);
        }
    }

    @Override
    public void surfaceCreated(SurfaceHolder surfaceHolder) {
        startCamera();
    }

    private void startCamera() {
        try {
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                return;
            }
            cameraSource.start(surfaceHolder);
            detector.setProcessor(new LargestFaceFocusingProcessor(detector,
                    new GraphicFaceTracker(this)));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void surfaceChanged(SurfaceHolder surfaceHolder, int i, int i1, int i2) {
    }

    @Override
    public void surfaceDestroyed(SurfaceHolder surfaceHolder) {
        cameraSource.stop();
    }

    @Override
    public void onPictureTaken(byte[] bytes) {
        bitmap = BitmapFactory.decodeByteArray(bytes, 0, bytes.length);
        int orientation = ScanFoto.Exif.getOrientation(bytes);
        switch(orientation) {
            case 90:
                bitmap= rotateImage(bitmap, 90);
                break;
            case 180:
                bitmap= rotateImage(bitmap, 180);
                break;
            case 270:
                bitmap= rotateImage(bitmap, 270);
                break;
            case 0:
                // if orientation is zero we don't need to rotate this
            default:
                break;
        }
        //write your code here to save bitmap
        // Save or Display image as per your requirements. Here we display the image.

        Intent intent = new Intent(this, Preview.class);
        intent.putExtra("BTN", toDo);
        intent.putExtra("Nrp", nrpstr);
        intent.putExtra("Nama", namastr);
        startActivity(intent);
    }
    public static Bitmap rotateImage(Bitmap source, float angle) {
        Matrix matrix = new Matrix();
        matrix.postRotate(angle);
        return Bitmap.createBitmap(source, 0, 0, source.getWidth(), source.getHeight(), matrix, true);
    }

    public static class Exif {
        private static final String TAG = "CameraExif";

        // Returns the degrees in clockwise. Values are 0, 90, 180, or 270.
        public static int getOrientation(byte[] jpeg) {
            if (jpeg == null) {
                return 0;
            }

            int offset = 0;
            int length = 0;

            // ISO/IEC 10918-1:1993(E)
            while (offset + 3 < jpeg.length && (jpeg[offset++] & 0xFF) == 0xFF) {
                int marker = jpeg[offset] & 0xFF;

                // Check if the marker is a padding.
                if (marker == 0xFF) {
                    continue;
                }
                offset++;

                // Check if the marker is SOI or TEM.
                if (marker == 0xD8 || marker == 0x01) {
                    continue;
                }
                // Check if the marker is EOI or SOS.
                if (marker == 0xD9 || marker == 0xDA) {
                    break;
                }

                // Get the length and check if it is reasonable.
                length = pack(jpeg, offset, 2, false);
                if (length < 2 || offset + length > jpeg.length) {
                    Log.e(TAG, "Invalid length");
                    return 0;
                }

                // Break if the marker is EXIF in APP1.
                if (marker == 0xE1 && length >= 8 &&
                        pack(jpeg, offset + 2, 4, false) == 0x45786966 &&
                        pack(jpeg, offset + 6, 2, false) == 0) {
                    offset += 8;
                    length -= 8;
                    break;
                }

                // Skip other markers.
                offset += length;
                length = 0;
            }

            // JEITA CP-3451 Exif Version 2.2
            if (length > 8) {
                // Identify the byte order.
                int tag = pack(jpeg, offset, 4, false);
                if (tag != 0x49492A00 && tag != 0x4D4D002A) {
                    Log.e(TAG, "Invalid byte order");
                    return 0;
                }
                boolean littleEndian = (tag == 0x49492A00);

                // Get the offset and check if it is reasonable.
                int count = pack(jpeg, offset + 4, 4, littleEndian) + 2;
                if (count < 10 || count > length) {
                    Log.e(TAG, "Invalid offset");
                    return 0;
                }
                offset += count;
                length -= count;

                // Get the count and go through all the elements.
                count = pack(jpeg, offset - 2, 2, littleEndian);
                while (count-- > 0 && length >= 12) {
                    // Get the tag and check if it is orientation.
                    tag = pack(jpeg, offset, 2, littleEndian);
                    if (tag == 0x0112) {
                        // We do not really care about type and count, do we?
                        int orientation = pack(jpeg, offset + 8, 2, littleEndian);
                        switch (orientation) {
                            case 1:
                                return 0;
                            case 3:
                                return 180;
                            case 6:
                                return 90;
                            case 8:
                                return 270;
                        }
                        Log.i(TAG, "Unsupported orientation");
                        return 0;
                    }
                    offset += 12;
                    length -= 12;
                }
            }

            Log.i(TAG, "Orientation not found");
            return 0;
        }

        private static int pack(byte[] bytes, int offset, int length,
                                boolean littleEndian) {
            int step = 1;
            if (littleEndian) {
                offset += length - 1;
                step = -1;
            }

            int value = 0;
            while (length-- > 0) {
                value = (value << 8) | (bytes[offset] & 0xFF);
                offset += step;
            }
            return value;
        }
    }
}