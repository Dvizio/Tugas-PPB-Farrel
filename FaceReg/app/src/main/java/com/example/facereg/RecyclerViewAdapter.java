package com.example.facereg;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;

import com.example.facereg.server.ResponseApi;

import java.util.ArrayList;

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.MyViewHolder> {
    private Bitmap decodeFromBase64(String base64Image) {
        byte[] imageBytes = Base64.decode(base64Image, Base64.DEFAULT);
        return BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
    }
    private Context context;
    private String sid="1";
    private ArrayList<ResponseApi> imageList;
    private OnDeleteButtonClickListener onDeleteButtonClickListener;

    public RecyclerViewAdapter(Context context, ArrayList<ResponseApi> imageList, OnDeleteButtonClickListener listener) {
        this.context = context;
        this.imageList = imageList;
        this.onDeleteButtonClickListener = listener;
    }

    public interface OnDeleteButtonClickListener {
        void onDeleteButtonClick(String subjectId);
    }
    @NonNull
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.activity_userlist,parent,false);
        MyViewHolder vh = new MyViewHolder(view);
        return vh;
    }



    @Override
    public int getItemCount() {
        return imageList.size();
    }
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        ResponseApi imageModel = imageList.get(position);

        //get data
        //we need All data
        String sname = imageModel.getName();
        String snrp = imageModel.getNrp();
        String simage = imageModel.getImage();


        //set data in view
        holder.subjectname.setText(sname);
        holder.subjectnrp.setText(snrp);
        holder.subjectImage.setImageBitmap(decodeFromBase64(simage));
        holder.subjectDeleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onDeleteButtonClickListener != null) {
                    onDeleteButtonClickListener.onDeleteButtonClick(imageModel.getNrp());
                }
            }
        });
    }

    public void setData(ArrayList<ResponseApi> data){
        imageList.clear();
        imageList.addAll(data);
        notifyDataSetChanged();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{

        //view for row_contact_item
        TextView subjectname;
        TextView subjectnrp;
        ImageView subjectImage;
        LinearLayout subjectLinearLayout;
        Button subjectDeleteButton;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            subjectname = itemView.findViewById(R.id.sname);
            subjectnrp = itemView.findViewById(R.id.snrp);
            subjectImage = itemView.findViewById(R.id.imageLayout);
            subjectLinearLayout = itemView.findViewById(R.id.subjectLL);
            subjectDeleteButton = itemView.findViewById(R.id.deleteBTN);
        }
    }
}
