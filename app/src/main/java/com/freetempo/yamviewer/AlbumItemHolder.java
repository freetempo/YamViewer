package com.freetempo.yamviewer;

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

        cover.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String description = albumInfo.getDescription();
                String showText;
                if (TextUtils.isEmpty(description)) {
                    showText = itemView.getContext().getResources()
                            .getString(R.string.no_album_description);
                } else {
                    showText = description;
                }
                ToastUtil.showToast(itemView.getContext(), showText);
                return true;
            }
        });

        name.setText(albumInfo.getName());
        String desc = albumInfo.getDescription();
        description.setText(albumInfo.getDescription());
        if (TextUtils.isEmpty(desc)) {
            description.setVisibility(View.GONE);
        } else {
            description.setVisibility(View.VISIBLE);
        }
        // photoNumber.setText(Integer.toString(albumInfo.getPhotos()));
        photoNumber.setText(String.format(itemView.getContext()
                .getString(R.string.photos_quantity), albumInfo.getPhotos()));
    }
}
