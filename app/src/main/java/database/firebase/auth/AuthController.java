package database.firebase.auth;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import database.firebase.models.User;

public class AuthController {

    private static AuthController authController = null;

    private final FirebaseAuth auth;
    private FirebaseUser fUser;
    private User user;

    private AuthController() {
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
        if (fUser != null) {
            loadUser();
        }
    }

    public static AuthController newInstance() {
        if (authController == null) authController = new AuthController();
        return authController;
    }

    public boolean isAuthenticated() {
        return auth.getCurrentUser() != null;
    }

    public User getUser() {
        return user;
    }

    private void loadUser() {
        FirebaseFirestore.getInstance().collection(User.COLLECTION).whereEqualTo(User.FIELD_EMAIL, fUser.getEmail()).get()
                .addOnSuccessListener(q -> user = q.toObjects(User.class).get(0));

    }

    public Task<AuthResult> registerWithEmailAndPassword(String email, String password) {
        Task<AuthResult> authResultTask = auth.createUserWithEmailAndPassword(email, password);
        authResultTask.addOnSuccessListener(ar -> {
            fUser = ar.getUser();
            loadUser();
        });
        return authResultTask;
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        Task<AuthResult> authResultTask = auth.signInWithEmailAndPassword(email, password);
        authResultTask.addOnSuccessListener(ar -> {
            fUser = ar.getUser();
            loadUser();
        });
        return authResultTask;
    }

    public Task<Void> updateProfile(String displayName, Uri photoUri) {
        UserProfileChangeRequest build = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(photoUri).build();
        if (fUser != null)
            return fUser.updateProfile(build);
        return null;
    }

    public void signOut() {
        user = null;
        fUser = null;
        auth.signOut();
        authController = null;
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}
