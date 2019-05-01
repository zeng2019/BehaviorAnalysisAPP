package com.example.administrator.timeRecording.utils;

import android.content.Context;
import android.widget.Toast;


public class ToastUtils {

    public void showToast(final String msg,final Context context) {
        Toast.makeText(context, msg, Toast.LENGTH_SHORT).show();
    }
}
