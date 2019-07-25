package com.prabath.mywallet;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Register extends AppCompatActivity {

    ImageView imageView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        imageView=findViewById(R.id.user_image);
    }

    public void animateUserImageView(View v){
//        ScaleAnimation anim = new ScaleAnimation(
//                1f, 2f, // Start and end values for the X axis scaling
//                1f, 2f, // Start and end values for the Y axis scaling
//                Animation.RELATIVE_TO_SELF, 0f, // Pivot point of X scaling
//                Animation.RELATIVE_TO_SELF, 0f);// Pivot point of Y scaling
//        anim.setFillAfter(true); // Needed to keep the result of the animation
//        anim.setDuration(5000);
//        v.startAnimation(anim);

    }

    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMG= 2;


    public void dispatchTakePictureIntent(View view) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    public void dispatchOpenPictureIntent(View view){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    private void startCropImage(Uri uri){
        UCrop.of(uri,Uri.fromFile(new File(getCacheDir(),"crop_image.jpg"))).start(this);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap imageBitmap = (Bitmap) extras.get("data");

            File cacheDir = getBaseContext().getCacheDir();
            File f = new File(cacheDir, "image_out");

            try {
                FileOutputStream out = new FileOutputStream(
                        f);
                imageBitmap.compress(
                        Bitmap.CompressFormat.JPEG,
                        100, out);
                out.flush();
                out.close();
                startCropImage(Uri.parse(f.toURI().toString()));
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
            //imageView.setImageBitmap(imageBitmap);
        }else if(requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK){
                final Uri imageUri = data.getData();
                startCropImage(imageUri);
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);

        }else if(requestCode==UCrop.REQUEST_CROP && resultCode==RESULT_OK){
            try{
                Uri output = UCrop.getOutput(data);
                final InputStream imageStream = getContentResolver().openInputStream(output);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);

            }catch (FileNotFoundException ex){

            }

        }
    }
}
