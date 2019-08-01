package database.firebase.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import database.firebase.models.User;

public class StorageController {

    private static StorageController controller;

    private StorageReference storage;

    private StorageController() {
        storage = FirebaseStorage.getInstance().getReference();

    }

    public static StorageController getInstance() {
        if (controller == null) controller = new StorageController();
        return controller;
    }

    public UploadTask uploadRecordPhoto(Uri photoUri) {
        StorageReference child = storage.child("record-documents/test.jpg");
        return child.putFile(photoUri);
    }

    public UploadTask uploadProfilePhoto(User user, Uri localPhotoUri) {
        StorageReference child = storage.child(user.getId() + "/profile.jpg");
        return child.putFile(localPhotoUri);
    }

    //profile image
    //record images


}
