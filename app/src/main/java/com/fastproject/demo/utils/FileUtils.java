package com.fastproject.demo.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class FileUtils {

    private void getExternalPath() {

    }

    public static File getImgFile(boolean video) {
        File folder = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM), "Camera");
        if (!folder.exists() || !folder.isDirectory()) {
            if (!folder.mkdirs()) {
                folder = Environment.getExternalStorageDirectory();
            }
        }
        return new File(folder, (video ? "VID" : "IMG") + "_" +
                new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date()) +
                (video ? ".mp4" : ".jpg"));
    }

    /**
     * get path by uri
     */
    public static String getPathByUri(Context context, Uri uri) {
        String path = null;
        if (uri == null)
            return null;
        String scheme = uri.getScheme();
        if (scheme == null || ContentResolver.SCHEME_FILE.equals(scheme))
            path = uri.getPath();
        else if (ContentResolver.SCHEME_CONTENT.equals(scheme)) {
            Cursor cursor = context.getContentResolver().query(uri,
                    new String[]{MediaStore.Images.ImageColumns.DATA}, null, null, null);
            if (null != cursor) {
                if (cursor.moveToFirst()) {
                    int index = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
                    if (index > -1) {
                        path = cursor.getString(index);
                    }
                }
                cursor.close();
            }
        }
        return path;
    }

    public void delTempPic(Context context, String tempPicPath) {
        if (tempPicPath.contains("temp")) {
            File file = new File(tempPicPath);
            if (!file.exists())
                return;
            file.delete();
            // 更新图库
            Intent intent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
            Uri uri = Uri.fromFile(new File(tempPicPath));
            intent.setData(uri);
            context.sendBroadcast(intent);
        }
    }

}
