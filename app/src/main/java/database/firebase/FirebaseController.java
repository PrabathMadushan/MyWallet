package database.firebase;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

import database.firebase.listeners.ReadCompleteListener;
import database.firebase.models.Account;
import database.firebase.models.Category;
import database.firebase.models.CategoryType;
import database.firebase.models.Record;
import database.firebase.models.User;

public class FirebaseController {

    private static FirebaseController controler = null;
    private FirebaseFirestore db;
    private FirebaseUser firebaseUser;
    private User user;

    private FirebaseController(final FirebaseUser user) {
        db = FirebaseFirestore.getInstance();
        this.firebaseUser = user;
//        db.collection("USER").whereEqualTo("EMAIL", firebaseUser.getEmail()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//            @Override
//            public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
//                    FirebaseController.this.user = documentSnapshot.toObject(User.class);
//                }
//            }
//        });
    }


    public static FirebaseController newInstance(FirebaseUser user) {
        if (controler == null) controler = new FirebaseController(user);
        return controler;
    }


    public class CollectionRecords {
        public void add(Record record, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Record.COLLECTION).document(record.getId()).set(record).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void update(Record record, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Record.COLLECTION).document(record.getId()).set(record).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void remove(Record account, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Record.COLLECTION).document(account.getId()).delete().addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }


    }

    public class CollectionAccounts {

        public void add(Account account, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Account.COLLECTION).document(account.getId()).set(account).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void update(Account account, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Account.COLLECTION).document(account.getId()).set(account).addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void remove(Account account, OnSuccessListener<Void> successListener, OnFailureListener failureListener) {
            db.collection(Account.COLLECTION).document(account.getId()).delete().addOnSuccessListener(successListener).addOnFailureListener(failureListener);
        }

        public void getAll(final ReadCompleteListener<Account> completeListener, OnFailureListener failureListener) {
            db.collection(Account.COLLECTION).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<Account> accounts = new ArrayList<>();
                    if (task.isComplete()) {
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Account account = documentSnapshot.toObject(Account.class);
                            accounts.add(account);
                        }
                        completeListener.onReadComplete(accounts);
                    } else {
                        completeListener.onReadComplete(accounts);
                    }
                }
            }).addOnFailureListener(failureListener);
        }

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

        public void getById(String id, final ReadCompleteListener<Category> completeListener, OnFailureListener failureListener) {
            getByField(Category.FIELD_ID, id, new ReadCompleteListener<Category>() {
                @Override
                public void onReadComplete(ArrayList<Category> list) {
                    completeListener.onReadComplete(list);
                }
            }, failureListener);
        }

        public void getByType(CategoryType type, final ReadCompleteListener<Category> completeListener, OnFailureListener failureListener) {
            getByField(Category.FIELD_TYPE, type, new ReadCompleteListener<Category>() {
                @Override
                public void onReadComplete(ArrayList<Category> list) {
                    completeListener.onReadComplete(list);
                }
            }, failureListener);
        }

        private void getByField(String fieldName, Object value, final ReadCompleteListener<Category> completeListener, OnFailureListener failureListener) {
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
