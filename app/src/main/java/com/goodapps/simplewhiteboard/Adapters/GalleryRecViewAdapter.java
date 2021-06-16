package com.goodapps.simplewhiteboard.Adapters;

import android.content.Context;
import android.content.Intent;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.goodapps.simplewhiteboard.ExpandedImageView;
import com.goodapps.simplewhiteboard.Models.DrawingImage;
import com.goodapps.simplewhiteboard.R;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;

public class GalleryRecViewAdapter extends RecyclerView.Adapter<GalleryRecViewAdapter.ViewHolder> {
    private final Context context;
    private final int displayWidth;
    private ArrayList<DrawingImage> images = new ArrayList<>();

    public GalleryRecViewAdapter(Context context, DisplayMetrics displayMetrics) {
        this.context = context;
        this.displayWidth = displayMetrics.widthPixels;
    }

    public void setImages(ArrayList<DrawingImage> images) {
        this.images = images;
        notifyDataSetChanged();
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.gallery_view_image_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull @NotNull GalleryRecViewAdapter.ViewHolder holder, int position) {
        int size = (displayWidth / 4) - 8;
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(size, size);
        holder.parent.setLayoutParams(layoutParams);

        Glide.with(context)
                .load(images.get(position).getUrl())
                .centerCrop()
                .into(holder.galleryItemImageView);

        holder.galleryItemImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ExpandedImageView.class);
                intent.putExtra("imageUrl", images.get(position).getUrl());
                context.startActivity(intent);
            }
        });


    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    protected class ViewHolder extends RecyclerView.ViewHolder {

        private final ImageView galleryItemImageView;
        private final LinearLayout parent;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            galleryItemImageView = (ImageView) itemView.findViewById(R.id.galleryItemImageView);
            parent = (LinearLayout) itemView.findViewById(R.id.galleryItemParent);

        }
    }
}
