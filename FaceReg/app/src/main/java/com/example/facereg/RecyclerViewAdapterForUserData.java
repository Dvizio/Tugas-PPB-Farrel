package com.example.facereg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facereg.server.ResponseApiUserData;

import java.util.ArrayList;
import java.util.List;


public class RecyclerViewAdapterForUserData extends RecyclerView.Adapter<RecyclerViewAdapterForUserData.MyViewHolder> {
    private Bitmap decodeFromBase64(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }

    private Context context;
    private ArrayList<ResponseApiUserData> imageList;


    public RecyclerViewAdapterForUserData(Context context, ArrayList<ResponseApiUserData> imageList) {
        this.context = context;
        this.imageList = imageList;
    }


    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_userdatalist,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }



    @Override
    public int getItemCount() {
        return imageList.size();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResponseApiUserData imageModel = imageList.get(position);
//        Toast.makeText(RecyclerViewAdapterForUserData.this.context, imageModel.toString(), Toast.LENGTH_LONG).show();
        //get data
        //we need All data
        String sname = imageModel.getName();
        String snrp = imageModel.getNrp();



        //set data in view
        holder.subjectname.setText(sname);
        holder.subjectnrp.setText(snrp);


    }

    public void setData(List<ResponseApiUserData> data){
        imageList.clear();
        imageList.addAll(data);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        //view for row_contact_item
        TextView subjectname;
        TextView subjectnrp;

        LinearLayout subjectLinearLayout;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            subjectname = itemView.findViewById(R.id.username);
            subjectnrp = itemView.findViewById(R.id.usernrp);
            subjectLinearLayout = itemView.findViewById(R.id.userLL);
        }
    }
}
