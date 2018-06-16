package com.freetempo.yamviewer;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

public class AlbumItemHolder extends RecyclerView.ViewHolder {

    private ImageView cover;

    public AlbumItemHolder(View itemView) {
        super(itemView);
        cover = itemView.findViewById(R.id.album_cover);
    }

    public void bind(AlbumInfo albumInfo) {
        // load cover
        Glide.with(itemView.getContext()).load(albumInfo.getCover()).into(cover);
    }
}
