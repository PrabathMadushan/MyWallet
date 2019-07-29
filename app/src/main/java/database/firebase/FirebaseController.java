package database.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

import database.firebase.listeners.ReadCompleteListener;
import database.firebase.models.Category;

public class FirebaseController {

    private static FirebaseController controler = null;
    private FirebaseFirestore db;

    private FirebaseController() {
        db = FirebaseFirestore.getInstance();
    }

    public static FirebaseController newInstance() {
        if (controler == null) controler = new FirebaseController();
        return controler;
    }


    public class CollectionCategories {

        public void add(Category category, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Category.COLLECTION).document(category.getId()).set(category).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void update(Category category, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Category.COLLECTION).document(category.getId()).set(category).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void remove(Category category, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Category.COLLECTION).document(category.getId()).delete().addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void getAll(final ReadCompleteListener<Category> completeListener, OnFailureListener failureListener) {
            db.collection(Category.COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Category> categories = new ArrayList<>();
                    if (task.isComplete()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Category category = documentSnapshot.toObject(Category.class);
                            categories.add(category);
                        }
                        completeListener.onReadComplete(categories);
                    } else {
                        completeListener.onReadComplete(categories);
                    }
                }
            }).addOnFailureListener(failureListener);
        }

        public void getByField(String fieldName, Object value, final ReadCompleteListener<Category> completeListener, OnFailureListener failureListener) {
            db.collection(Category.COLLECTION).whereEqualTo(fieldName, value).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Category> categories = new ArrayList<>();
                    if (task.isComplete()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Category category = documentSnapshot.toObject(Category.class);
                            categories.add(category);
                        }
                        completeListener.onReadComplete(categories);
                    } else {
                        completeListener.onReadComplete(categories);
                    }
                }
            }).addOnFailureListener(failureListener);
        }


    }


    public static String genareteRandomKey() {
        return UUID.randomUUID().toString();
    }
}
