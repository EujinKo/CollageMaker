package com.example.collagemaker;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import android.view.ViewGroup;
import android.widget.ImageView;

import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity {

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static private int id_cur_imageview;

    String currentPhotoPath = null;
    ImageView currentImageView = null;
    Uri currentPhotoUri = null;
    Uri currentViewGroupUri = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
        System.out.println("ActionSend");

        saveImageGroupToUri();

        Intent shareIntent = new Intent(android.content.Intent.ACTION_SEND);
        shareIntent.putExtra(Intent.EXTRA_STREAM,currentViewGroupUri);
        shareIntent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        shareIntent.setType("image/png");
        startActivity(shareIntent);
    }

    public void saveImageGroupToUri(){
        ViewGroup constraintLayout = findViewById(R.id.image_view_group);
        //creates bitmap to draw on
        Bitmap bitmap = Bitmap.createBitmap(
                constraintLayout.getWidth(), constraintLayout.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bitmap);
        //This draws the view inside the bitmap
        constraintLayout.draw(canvas);

        try{
            File imageFile = createImageFile();
            FileOutputStream out = new FileOutputStream(imageFile);
            bitmap.compress(Bitmap.CompressFormat.PNG,100,out);

            //Saves the uri for the view group image file
            currentViewGroupUri = FileProvider.getUriForFile(
                    this,"com.example.CollageMaker.FileProvider",
                    imageFile);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
}
