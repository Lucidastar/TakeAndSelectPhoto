package com.lucida.tsphoto.utils;

import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.RequiresApi;

/**
 * Created by Administrator on 2017/4/12.
 */

public class PhotoUtils {

    private PhotoUtils(){
        throw new UnsupportedOperationException("u can't instantiate me...");
    }

    private static String handleImageBeforeKitKat(Context context,Intent intent){
        Uri uri = intent.getData();
        String imagePath = getImagePath(context,uri,null);
        return imagePath;
    }

    /**
     * 获取相册图片的路径
     * @param context
     * @param intent
     * @return
     */
    public static String getImagePath(Context context,Intent intent){
        String imagePath = null;
        if (Build.VERSION.SDK_INT >= 19){
            imagePath = handleImageOnKitKat(context,intent);
        }else {
            imagePath = handleImageBeforeKitKat(context,intent);
        }
        return imagePath;
    }

    private static String getImagePath(Context context,Uri uri, String selection) {
        String imagePath = null;
        Cursor cursor = context.getContentResolver().query(uri,null,selection,null,null);
        if (cursor != null){
            if (cursor.moveToFirst()){
                imagePath = cursor.getString(cursor.getColumnIndex(MediaStore.Images.Media.DATA));
            }

            cursor.close();
        }
        return imagePath;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    private static String handleImageOnKitKat(Context context,Intent intent){
        String imagePath = null;
        Uri uri = intent.getData();
        if (DocumentsContract.isDocumentUri(context,uri)){
            //如果是document类型的Uri，则通过document id 处理
            String docId = DocumentsContract.getDocumentId(uri);
            if ("com.android.providers.media.documents".equals(uri.getAuthority())){
                String id = docId.split(":")[1];
                String selection = MediaStore.Images.Media._ID + "=" + id;
                imagePath = getImagePath(context,MediaStore.Images.Media.EXTERNAL_CONTENT_URI,selection);
            }else if ("com.android.providers.downloads.documents".equals(uri.getAuthority())){
                Uri contentUri = ContentUris.withAppendedId(Uri.parse("content://downloads/public_downloads"),Long.valueOf(docId));
                imagePath = getImagePath(context,contentUri,null);
            }
        }else if ("content".equalsIgnoreCase(uri.getScheme())){
            imagePath = getImagePath(context,uri,null);
        }else if ("file".equalsIgnoreCase(uri.getScheme())){
            imagePath = uri.getPath();
        }

        return imagePath;
    }

}
