package com.lucida.tsphoto;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucida.tsphoto.utils.PhotoUtils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageOld;
    private ImageView mImageNew;
    private Uri imageUri;
    public static final int TAKE_PHOTO = 2;

    private static final int PICK_IMAGE_REQUEST = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
    }

    private void initInstances() {
        mImageOld = (ImageView) findViewById(R.id.main_image_old);
        mImageNew = (ImageView) findViewById(R.id.main_image_new);

    }

    public void selectPhoto(View view) {
        //6.0以上要进行动态权限的判断
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void takePhoto(View view) {
        //6.0以上要进行动态权限的判断
        //创建file对象，用于存储拍照后的图片
        File outputImage = new File(getExternalCacheDir(),"out_put.jpg");
        try {

            if (outputImage.exists()){
                outputImage.delete();
            }
            outputImage.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }
        if (Build.VERSION.SDK_INT >= 24){
            imageUri = FileProvider.getUriForFile(this,"com.example.cameraalbumtest.fileprovider",outputImage);
        }else {
            imageUri = Uri.fromFile(outputImage);
        }
        Intent intent = new Intent("android.media.action.IMAGE_CAPTURE");
        intent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);
        startActivityForResult(intent, TAKE_PHOTO);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK) {
            if (data == null) {
                return;
            }
            try {
                String picturePath = null;

                    picturePath = PhotoUtils.getImagePath(this,data);

                Bitmap bitmap = BitmapFactory.decodeFile(picturePath);
                mImageOld.setImageBitmap(BitmapFactory.decodeFile(picturePath));
                //=======================

            } catch (Exception e) {
                e.printStackTrace();
            }
        }else if (requestCode == TAKE_PHOTO && resultCode == RESULT_OK){
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(getContentResolver().openInputStream(imageUri));
                mImageNew.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }
    }
}
