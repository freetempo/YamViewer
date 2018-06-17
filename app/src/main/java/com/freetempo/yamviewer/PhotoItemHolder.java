package com.freetempo.yamviewer;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.freetempo.yamviewer.utils.ToastUtil;

public class PhotoItemHolder extends RecyclerView.ViewHolder {

    private ImageView image;
    private TextView name;

    public PhotoItemHolder(View itemView) {
        super(itemView);
        image = itemView.findViewById(R.id.photo_image);
        name = itemView.findViewById(R.id.photo_name);
    }

    public void bind(final PhotoItem photoItem) {
        final String imageUrl = photoItem.getUrl();
        final String photoName = photoItem.getName();

        Glide.with(itemView.getContext()).load(imageUrl).into(image);

        image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browseIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(imageUrl));
                itemView.getContext().startActivity(browseIntent);
            }
        });

        image.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                String showText;
                if (TextUtils.isEmpty(photoName)) {
                    showText = itemView.getContext().getResources()
                            .getString(R.string.no_photo_description);
                } else {
                    showText = photoName;
                }
                ToastUtil.showToast(itemView.getContext(), showText);
                return true;
            }
        });

        if (photoName != null) {
            name.setText(photoName);
            name.setVisibility(View.VISIBLE);
        } else {
            name.setText("");
            name.setVisibility(View.GONE);
        }
    }
}
