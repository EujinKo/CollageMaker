package com.example.collagemaker;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static private int id_cur_imageview;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE &&
        resultCode == RESULT_OK){
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");
            ImageView imageView = findViewById(id_cur_imageview);
            imageView.setImageBitmap(imageBitmap);
        }
    }

    // Set up intent for camera
    public void captureImage(View view){
      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
      id_cur_imageview = view.getId();
      startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
    }

    public void shareImage(View view){

        Intent shareIntent = new Intent(
                android.content.Intent.ACTION_SEND);


    }
}
