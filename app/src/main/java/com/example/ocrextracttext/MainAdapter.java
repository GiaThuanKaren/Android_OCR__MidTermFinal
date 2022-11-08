package com.example.ocrextracttext;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.io.File;
import java.util.List;

public class MainAdapter extends RecyclerView.Adapter<ViewHolder> {

    private Context context;
    private List<File> pdfFiles;
    private OnPdfSelectListener listener;
    private Bitmap image;

    public MainAdapter(Context context, List<File> pdfFiles, OnPdfSelectListener listener, Bitmap image) {
        this.context = context;
        this.pdfFiles = pdfFiles;
        this.listener=listener;
        this.image = image;
    }


    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ViewHolder(LayoutInflater.from(context).inflate(R.layout.rv_item,parent,false));
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.txtName.setText(pdfFiles.get(position).getName());
        holder.txtName.setSelected(true);
        holder.pdfImageView.setImageBitmap(image);
        holder.cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                listener.onPdfSelected(pdfFiles.get(position));
            }
        });

    }



    @Override
    public int getItemCount() {
        return pdfFiles.size();
    }
}
