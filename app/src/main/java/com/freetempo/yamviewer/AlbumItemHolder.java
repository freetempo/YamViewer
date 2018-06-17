package com.freetempo.yamviewer;

import android.content.Intent;
import android.net.Uri;
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

    public void bind(final AlbumInfo albumInfo) {
        // load cover
        final String coverLink = albumInfo.getCover();
        Glide.with(itemView.getContext()).load(coverLink).into(cover);
        // set click cover event
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open photo by web
//                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(coverLink));
//                itemView.getContext().startActivity(browseIntent);

                // open browse page
                BrowseAlbumActivity.launch(itemView.getContext(),
                        albumInfo.getUserName(), albumInfo.getId());

            }
        });
    }
}
