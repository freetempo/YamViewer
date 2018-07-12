package com.freetempo.yamviewer;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.freetempo.yamviewer.utils.ToastUtil;

public class AlbumItemHolder extends RecyclerView.ViewHolder {

    private ImageView cover;
    private TextView name, description, photoNumber;

    public AlbumItemHolder(View itemView) {
        super(itemView);
        cover = itemView.findViewById(R.id.album_cover);
        name = itemView.findViewById(R.id.album_name);
        description = itemView.findViewById(R.id.album_description);
        photoNumber = itemView.findViewById(R.id.album_photo_number);
    }

    public void bind(final AlbumInfo albumInfo) {
        // load cover
        final String coverLink = albumInfo.getCover();
        final Context context = itemView.getContext();
        Glide.with(context).load(coverLink).into(cover);
        // set click cover event
        cover.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // open browse page
                BrowseAlbumActivity.launch(context,
                        albumInfo.getUserName(), albumInfo.getId());

            }
        });

        cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                ToastUtil.showToast(context, albumInfo.getAlbumInfoString());
                return true;
            }
        });

        String nameString = albumInfo.getName();
        if (albumInfo.getAlbumStatus().equals("passwd")) {
            nameString = context.getString(R.string.locked_album_prefix) + nameString;
        }
        name.setText(nameString);
        String desc = albumInfo.getDescription();
        description.setText(albumInfo.getDescription());
        if (TextUtils.isEmpty(desc)) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
        }
        String imageNumber = String.format(context
                .getString(R.string.photos_quantity), albumInfo.getPhotos());
        String viewCount = String.format(context.getString(R.string.view_count), albumInfo.getViewCount());
        String showString = imageNumber + "  .  " + viewCount;
        photoNumber.setText(showString);
    }
}
