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

import androidx.appcompat.app.AppCompatActivity;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.mikhaellopez.circularimageview.CircularImageView;
import com.prabath.mywallet.Others.DataValidater;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Date;

import database.firebase.auth.AuthController;
import database.firebase.firestore.FirestoreController;
import database.firebase.models.User;
import database.firebase.storage.StorageController;

public class RegisterActivity extends AppCompatActivity {

    private CircularImageView imageView;
    private EditText email;
    private EditText password;
    private EditText passwordAgain;
    private EditText contact;
    private EditText username;

    private AuthController auth;

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
        auth = AuthController.newInstance();
        imageView = findViewById(R.id.user_image);
        email = findViewById(R.id.txtEmail);
        password = findViewById(R.id.txtPassword);
        passwordAgain = findViewById(R.id.txtPasswordAgain);
        contact = findViewById(R.id.txtContact);
        username = findViewById(R.id.txtUsername);
    }

    public void registerButtonClick(View view) {
        if (auth.getAuth().getCurrentUser() != null) {
            Toast.makeText(this, "Registered", Toast.LENGTH_SHORT).show();
        } else {
            register();
        }
    }

    private void register() {
        if (
                DataValidater.validateText(username)
                        & DataValidater.validateText(email)
                        & DataValidater.validatePassword(password, passwordAgain)
                        & DataValidater.validateText(contact)
        ) {
            User user = new User();
            user.setEmail(email.getText().toString());
            user.setPassword(password.getText().toString());
            user.setContact(contact.getText().toString());
            user.setDateTime(new Date());
            user.setUsername(username.getText().toString());
            auth.registerWithEmailAndPassword(
                    email.getText().toString(),
                    password.getText().toString())
                    .addOnSuccessListener(this, t -> {
                        Toast.makeText(RegisterActivity.this, "Registration Complete", Toast.LENGTH_SHORT).show();
                        if (selectedPhotoUri != null) {
                            StorageController.getInstance().uploadProfilePhoto(user, selectedPhotoUri)
                                    .addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show())
                                    .addOnSuccessListener(t1 -> {
                                        Uri uploadSessionUri = t1.getUploadSessionUri();
                                        user.setImage(uploadSessionUri.toString());
                                        FirestoreController.newInstance().new CollectionUsers().add(user)
                                                .addOnSuccessListener(v -> {
                                                    Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
                                                    gotoPlash();
                                                })
                                                .addOnFailureListener(f -> Toast.makeText(this, f.getMessage(), Toast.LENGTH_SHORT).show());
                                    });
                        } else {
                            user.setImage(null);
                            FirestoreController.newInstance().new CollectionUsers().add(user)
                                    .addOnSuccessListener(v -> {
                                        Toast.makeText(this, "User saved", Toast.LENGTH_SHORT).show();
                                        gotoPlash();
                                    })
                                    .addOnFailureListener(f -> Toast.makeText(this, f.getMessage(), Toast.LENGTH_SHORT).show());
                        }
                    }).addOnFailureListener(this, e -> Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show());
        }
    }

    private void gotoPlash() {
        Intent intent = new Intent(this, SplashActivity.class);
        startActivity(intent);
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
        YoYo.with(Techniques.RubberBand).duration(200).onEnd(animator -> {
            Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
            photoPickerIntent.setType("image/*");
            startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
        }).playOn(view);

    }

    private void startCropImage(Uri uri) {
        UCrop.of(uri, Uri.fromFile(new File(getCacheDir(), "crop_image.jpg"))).start(this);

    }

    private Uri selectedPhotoUri;

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
        } else if (requestCode == RESULT_LOAD_IMG && resultCode == RESULT_OK) {
            final Uri imageUri = data.getData();
            startCropImage(imageUri);
        } else if (requestCode == UCrop.REQUEST_CROP && resultCode == RESULT_OK) {
            try {
                Uri output = UCrop.getOutput(data);
                selectedPhotoUri = output;
                final InputStream imageStream = getContentResolver().openInputStream(output);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);
            } catch (FileNotFoundException ex) {

            }

        }
    }
}
