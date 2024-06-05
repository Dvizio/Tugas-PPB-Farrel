// ShowLogger.java
package com.example.facereguser;

import android.content.Intent;
import android.os.Bundle;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.facereguser.server.ApiClientFaceReg;
import com.example.facereguser.server.LogEntry;
import com.example.facereguser.server.LogResponse;
import com.example.facereguser.server.serverAPI;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowLogger extends AppCompatActivity {

    private ListView listView;
    private LogAdapter logAdapter;
    private String nrp, nama;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log);

        Intent intent = getIntent();
        nrp = intent.getStringExtra("Nrp");
        nama = intent.getStringExtra("Nama");

        listView = findViewById(R.id.listView);
        Button backButton = findViewById(R.id.backButton);
        Window window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(getResources().getColor(R.color.your_color));

        // Initialize Retrofit
        ApiClientFaceReg api = serverAPI.builder().create(ApiClientFaceReg.class);

        // Make the network request
        Call<LogResponse> call = api.getLogs(nrp);
        call.enqueue(new Callback<LogResponse>() {
            @Override
            public void onResponse(Call<LogResponse> call, Response<LogResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    List<LogEntry> logEntries = response.body().getData();
                    logAdapter = new LogAdapter(ShowLogger.this, logEntries);
                    listView.setAdapter(logAdapter);
                } else {
                    Toast.makeText(ShowLogger.this, "Failed to retrieve data", Toast.LENGTH_SHORT).show();
                }
            }

            @Override
            public void onFailure(Call<LogResponse> call, Throwable t) {
                Toast.makeText(ShowLogger.this, "Error: " + t.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

        // Set up the back button
        backButton.setOnClickListener(v -> {
            Intent backIntent = new Intent(ShowLogger.this, UserLoggedIn.class);
            backIntent.putExtra("Nrp", nrp);
            backIntent.putExtra("Nama", nama);
            startActivity(backIntent);
        });
    }
}
