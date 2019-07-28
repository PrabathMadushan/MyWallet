package com.prabath.mywallet;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.prabath.mywallet.Others.DataValidater;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

public class Register extends AppCompatActivity {

    private CircularImageView imageView;
    private EditText email;
    private EditText password;
    private EditText passwordAgain;
    private EditText contact;

    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        init();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void init() {
        auth = FirebaseAuth.getInstance();
        imageView = findViewById(R.id.user_image);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        passwordAgain = findViewById(R.id.txtPasswordAgain);
        contact = findViewById(R.id.txtContact);
    }

    public void registerButtonClick(View view) {
        if (auth.getCurrentUser() != null) {
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
        } else {
            register();
        }
    }

    private void register() {
        if (
                DataValidater.validateText(email)
                        & DataValidater.validatePassword(password, passwordAgain)
                        & DataValidater.validateText(contact)
        ) {
            auth.createUserWithEmailAndPassword(
                    email.getText().toString(),
                    password.getText().toString()
            ).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                    Toast.makeText(Register.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                }
            }).addOnFailureListener(this, new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }


    static final int REQUEST_IMAGE_CAPTURE = 1;
    static final int RESULT_LOAD_IMG = 2;


    public void dispatchTakePictureIntent(View view) {
        YoYo.with(Techniques.RubberBand).duration(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
                    startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
                }
            }
        }).playOn(view);

    }

    public void dispatchOpenPictureIntent(View view) {
        YoYo.with(Techniques.RubberBand).duration(200).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
                photoPickerIntent.setType("image/*");
                startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
            }
        }).playOn(view);

    }

    private void startCropImage(Uri uri) {
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "crop_image.jpg"))).start(this);

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
        } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            startCropImage(imageUri);
//                final InputStream imageStream = getContentResolver().openInputStream(imageUri);
//                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
//                imageView.setImageBitmap(selectedImage);

        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                Uri output = UCrop.getOutput(data);
                final InputStream imageStream = getContentResolver().openInputStream(output);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException ex) {

            }

        }
    }
}
