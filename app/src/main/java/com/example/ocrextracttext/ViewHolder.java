package com.example.ocrextracttext;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

public class ViewHolder extends RecyclerView.ViewHolder {

    public TextView txtName;
    public ImageView pdfImageView;
    public CardView cardView;


    public ViewHolder(@NonNull View itemView) {
        super(itemView);
        pdfImageView=itemView.findViewById(R.id.pdf_imageView);
        txtName=itemView.findViewById(R.id.pdf_txtName);
        cardView=itemView.findViewById(R.id.pdf_cardView);
    }
}
