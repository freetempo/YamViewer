package com.freetempo.yamviewer.utils;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;


public class ToastUtil {

    private static Toast toast;

    public static void showToast(Context context, String string) {
        showToast(context, string, Toast.LENGTH_SHORT);
    }

    public static void showToast(Context context, String string, int length) {
        if (toast != null) {
            toast.cancel();
        }
        toast = Toast.makeText(context, string, length);
        toast.show();
    }

}
