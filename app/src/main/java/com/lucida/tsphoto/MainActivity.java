package com.lucida.tsphoto;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.lucida.tsphoto.utils.PhotoUtils;

import java.io.File;

public class MainActivity extends AppCompatActivity {
    private ImageView mImageOld;
    private ImageView mImageNew;

    private static final int PICK_IMAGE_REQUEST = 1;
    private TextView mTextOld;
    private TextView mTextNew;

    private File oldFile;
    private File newFile;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initInstances();
    }

    private void initInstances() {
        mImageOld = (ImageView) findViewById(R.id.main_image_old);
        mImageNew = (ImageView) findViewById(R.id.main_image_new);
        mTextOld = (TextView) findViewById(R.id.main_text_old);
        mTextNew = (TextView) findViewById(R.id.main_text_new);
    }

    public void selectPhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    public void takePhoto(View view) {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
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

//                oldFile = FileUtil.getTempFile(this, Uri.parse(picturePath));
                oldFile = new File(picturePath);
//                mImageOld.setImageBitmap(BitmapFactory.decodeFile(oldFile.getAbsolutePath()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}
