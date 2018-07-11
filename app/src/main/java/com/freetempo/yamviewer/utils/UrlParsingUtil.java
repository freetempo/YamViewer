package com.freetempo.yamviewer.utils;

import android.net.Uri;

import java.util.List;

public class UrlParsingUtil {

    public static String getUserName(Uri uri) {
        // https://(username).tian.yam.com/albums
        String userName = "";
        if (uri != null) {
            String[] hostSplittedList = uri.getAuthority().split("\\.");
            if (hostSplittedList != null) {
                userName = hostSplittedList[0];
            }
        }
        return userName;
    }

    public static String getAlbumId(Uri uri) {
        // https://(username).tian.yam.com/album/(albumid)
        String albumId = "";
        if (uri != null) {
            List<String> segmentsList = uri.getPathSegments();
            if (segmentsList != null && segmentsList.size() >= 2) {
                albumId = segmentsList.get(1);
            }
        }
        return albumId;
    }

}
