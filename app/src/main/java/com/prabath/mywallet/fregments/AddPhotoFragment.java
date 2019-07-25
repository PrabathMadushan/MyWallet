package com.prabath.mywallet.fregments;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Others.Init;
import com.prabath.mywallet.R;
import com.yalantis.ucrop.UCrop;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import database.local.models.Record;

import static android.app.Activity.RESULT_OK;


public class AddPhotoFragment extends Fragment {

    private static final String KEY_RECORD = "KEYRECORD";

    private Record record;

    public AddPhotoFragment() {
    }

    public static AddPhotoFragment newInstance(Record record) {
        AddPhotoFragment fragment = new AddPhotoFragment();
        Bundle args = new Bundle();
        args.putSerializable(KEY_RECORD, record);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            record = (Record) getArguments().getSerializable(KEY_RECORD);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_add_photo, container, false);
    }


    private ImageView imageView;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        Activity a = getActivity();
        imageView = a.findViewById(R.id.photoDocument);
        ConstraintLayout wraper = a.findViewById(R.id.fwaraper);
        // YoYo.with(Techniques.ZoomIn).duration(200).playOn(wraper);
        ImageView openCamara = a.findViewById(R.id.btnOpenCamara);
        ImageView openPhoto = a.findViewById(R.id.btnOpenPhoto);

        if (record.getImage() != null) {
            File image = new File(Init.getInstance().getImageFolderProfile(), record.getImage());
            try {
                Bitmap bitmap = BitmapFactory.decodeStream(new FileInputStream(image));
                imageView.setImageBitmap(bitmap);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        }

        openCamara.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchTakePictureIntent();
            }
        });
        openPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dispatchOpenPictureIntent();
            }
        });

    }

    private final int REQUEST_IMAGE_CAPTURE = 1;
    private static final int RESULT_LOAD_IMG = 2;


    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    private void dispatchOpenPictureIntent() {
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        photoPickerIntent.setType("image/*");
        startActivityForResult(photoPickerIntent, RESULT_LOAD_IMG);
    }

    private void startCropImage(Uri uri) {
        UCrop.of(uri, Uri.fromFile(new File(getActivity().getCacheDir(), "dcument_crop.jpg"))).start(getContext(), this);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();

            Bitmap imageBitmap = (Bitmap) extras.get("data");
            File cacheDir = getActivity().getBaseContext().getCacheDir();
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
                final InputStream imageStream = getActivity().getContentResolver().openInputStream(output);
                final Bitmap selectedImage = BitmapFactory.decodeStream(imageStream);
                imageView.setImageBitmap(selectedImage);

            } catch (FileNotFoundException ex) {
                ex.printStackTrace();
            }

        }
    }

    private String imageName;

    public Bitmap getSelectedImage() {
        return ((BitmapDrawable) imageView.getDrawable()).getBitmap();
    }

    public void setBitmap(Bitmap bitmap){
        imageView.setImageBitmap(bitmap);
    }

    public String seveImage() {
        try {
            imageName = UUID.randomUUID().toString()+".jpeg";
            File f = new File(Init.getInstance().getImageFolderRecord(), imageName);
            Toast.makeText(getContext(), f.getPath(), Toast.LENGTH_SHORT).show();
            FileOutputStream out = new FileOutputStream(f);
            getSelectedImage().compress(
                    Bitmap.CompressFormat.JPEG,
                    100, out);
            out.flush();
            out.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return imageName;
    }


}
