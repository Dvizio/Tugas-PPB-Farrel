package com.example.facereguser;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.facereguser.server.LogEntry;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class LogAdapter extends BaseAdapter {
    private Context context;
    private List<LogEntry> logEntries;

    public LogAdapter(Context context, List<LogEntry> logEntries) {
        this.context = context;
        this.logEntries = logEntries;
    }

    @Override
    public int getCount() {
        return logEntries.size();
    }

    @Override
    public Object getItem(int position) {
        return logEntries.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.log_item, parent, false);
        }

        LogEntry logEntry = logEntries.get(position);

        TextView userNrpTextView = convertView.findViewById(R.id.userNrpTextView);
        TextView createdAtTextView = convertView.findViewById(R.id.createdAtTextView);

        userNrpTextView.setText(logEntry.getUserNrp());

        // Convert the date format
        String formattedDate = convertDateFormat(logEntry.getCreatedAt());
        createdAtTextView.setText(formattedDate);

        return convertView;
    }

    // Method to convert date format
    private String convertDateFormat(String inputDate) {
        SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault());
        inputFormat.setTimeZone(TimeZone.getTimeZone("UTC"));
        SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        outputFormat.setTimeZone(TimeZone.getTimeZone("Asia/Jakarta")); // Set to GMT+7

        try {
            Date date = inputFormat.parse(inputDate);
            return outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return inputDate; // Return original date if conversion fails
        }
    }
}
