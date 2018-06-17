package com.freetempo.yamviewer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class PhotoItemHolder extends RecyclerView.ViewHolder {

    private ImageView image;

    public PhotoItemHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.photo_image);
    }

    public void bind(PhotoItem photoItem) {
        final String imageUrl = photoItem.getUrl();

        Glide.with(itemView.getContext()).load(imageUrl).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
                itemView.getContext().startActivity(browseIntent);
            }
        });
    }
}
