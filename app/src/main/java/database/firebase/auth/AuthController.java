package database.firebase.auth;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

public class AuthController {

    private static AuthController controller;
    private final FirebaseAuth auth;
    private FirebaseUser fUser;

    private AuthController() {
        auth = FirebaseAuth.getInstance();
        fUser = auth.getCurrentUser();
    }

    public static AuthController getInstance() {
        if (controller == null) controller = new AuthController();
        return controller;
    }

    public Task<AuthResult> registerWithEmailAndPassword(String email, String password) {
        return auth.createUserWithEmailAndPassword(email, password);
    }

    public Task<AuthResult> signInWithEmailAndPassword(String email, String password) {
        return auth.signInWithEmailAndPassword(email, password);
    }

    public Task<Void> updateProfile(String displayName, Uri photoUri) {
        UserProfileChangeRequest build = new UserProfileChangeRequest.Builder().setDisplayName(displayName).setPhotoUri(photoUri).build();
        if (fUser != null)
            return fUser.updateProfile(build);
        return null;
    }

    public void signOut() {
        auth.signOut();
    }

    public FirebaseAuth getAuth() {
        return auth;
    }
}
