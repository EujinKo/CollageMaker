package com.example.collagemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static private int id_cur_imageview;

    String currentPhotoPath = null;
    ImageView currentImageView = null;
    Uri currentPhotoUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_row);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,Intent data){
        if(requestCode == REQUEST_IMAGE_CAPTURE &&
        resultCode == RESULT_OK){
            BitmapFactory.Options bmOptions =
                    new BitmapFactory.Options();
            Bitmap bitmap = BitmapFactory.decodeFile(
                    currentPhotoPath, bmOptions);

            int size = Math.min(bitmap.getWidth(),bitmap.getHeight());
            Bitmap squareBitmap = Bitmap.createBitmap(
                    bitmap,0,0,size,size);

            currentImageView.setImageBitmap(bitmap);
        }
    }

    // Set up intent for camera, create empty File to save the picture taken
    //      after using camera app
    public void captureImage(View view){
      Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

      File photoFile = null;
      try{
          photoFile = createImageFile();
          currentImageView = (ImageView) view;
          currentPhotoUri = FileProvider.getUriForFile(
                  this,"com.example.CollageMaker.FileProvider",photoFile);
          cameraIntent.putExtra(
                  MediaStore.EXTRA_OUTPUT,currentPhotoUri);
          startActivityForResult(cameraIntent,REQUEST_IMAGE_CAPTURE);
      }catch(Exception e){
          e.printStackTrace();
      }
    }

    // This function creates empty file directory to save image
    private File createImageFile() throws Exception{
        String timeStamp = new SimpleDateFormat(
                "yyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_"+timeStamp+"_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(imageFileName,".jpg",storageDir);
        currentPhotoPath = image.getAbsolutePath();

        return image;
    }

    public void shareImage(View view){
        Bitmap bitmap = Bitmap.createBitmap(
                view.getWidth(), view.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        view.draw(canvas);


        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        startActivity(shareIntent);
    }
}
