package database.firebase.firestore;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.UUID;

import database.firebase.auth.AuthController;
import database.firebase.listeners.ReadCompleteListener;
import database.firebase.models.Account;
import database.firebase.models.Category;
import database.firebase.models.CategoryType;
import database.firebase.models.Record;
import database.firebase.models.User;

public class FirestoreController {

    private static FirestoreController controler = null;
    private FirebaseFirestore db;

    private FirestoreController() {
        db = FirebaseFirestore.getInstance();
    }


    public static FirestoreController newInstance() {
        if (controler == null) {
            controler = new FirestoreController();
        }
        return controler;
    }

    public class CollectionUsers {
        public Task<Void> add(User user) {
            return db.collection(User.COLLECTION).document(user.getId()).set(user);
        }

        public Task<Void> update(User user) {
            return db.collection(User.COLLECTION).document(user.getId()).set(user);
        }

        public Task<Void> remove(User user) {
            return db.collection(User.COLLECTION).document(user.getId()).delete();
        }

        public Task<QuerySnapshot> getByEmail(String email, final ReadCompleteListener<User> listener) {
            return db.collection(User.COLLECTION).whereEqualTo(User.FIELD_EMAIL, email).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                @Override
                public void onComplete(@NonNull Task<QuerySnapshot> task) {
                    ArrayList<User> users = new ArrayList<>();
                    if (task.isComplete()) {
                        if (task.getResult() != null)
                            for (QueryDocumentSnapshot snapshot : task.getResult()) {
                                User user = snapshot.toObject(User.class);
                                users.add(user);
                            }
                        if (listener != null) listener.onReadComplete(users);
                    } else {
                        if (listener != null) listener.onReadComplete(users);
                    }
                }
            });
        }
    }

    public class CollectionRecords {
        public Task<Void> add(Record record) {
            return db.collection(Record.COLLECTION).document(record.getId()).set(record);
        }

        public Task<Void> update(Record record) {
            return db.collection(Record.COLLECTION).document(record.getId()).set(record);
        }

        public Task<Void> remove(Record record) {
            return db.collection(Account.COLLECTION).document(record.getId()).delete();
        }

        public Task<QuerySnapshot> getAll(final ReadCompleteListener<Record> completeListener) {
            return db.collection(Record.COLLECTION)
                    .whereEqualTo(Record.FIELD_USER, AuthController.newInstance().getUser().getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Record> records = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Record account = documentSnapshot.toObject(Record.class);
                                    records.add(account);
                                }
                            if (completeListener != null) completeListener.onReadComplete(records);
                        } else {
                            if (completeListener != null) completeListener.onReadComplete(records);
                        }
                    });
        }

        public Task<QuerySnapshot> getByAccount(String accountid, final ReadCompleteListener<Record> completeListener) {
            return getByField(Record.FIELD_ACCOUNT, accountid, list -> {
                if (completeListener != null) completeListener.onReadComplete(list);
            });
        }

        private Task<QuerySnapshot> getByField(String fieldName, Object value, final ReadCompleteListener<Record> completeListener) {
            return db.collection(Record.COLLECTION)
                    .whereEqualTo(fieldName, value)
                    .whereEqualTo(Record.FIELD_USER, AuthController.newInstance().getUser().getId())
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Record> records = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Record category = documentSnapshot.toObject(Record.class);
                                    records.add(category);
                                }
                            if (completeListener != null) completeListener.onReadComplete(records);
                        } else {
                            if (completeListener != null) completeListener.onReadComplete(records);
                        }
                    });
        }
    }

    public class CollectionAccounts {

        public Task<Void> add(Account account) {
            return db.collection(Account.COLLECTION).document(account.getId()).set(account);
        }

        public Task<Void> update(Account account) {
            return db.collection(Account.COLLECTION).document(account.getId()).set(account);
        }

        public Task<Void> remove(Account account) {
            return db.collection(Account.COLLECTION).document(account.getId()).delete();
        }

        public Task<QuerySnapshot> getAll(final ReadCompleteListener<Account> completeListener) {
            return db.collection(Account.COLLECTION)
                    .whereEqualTo(Account.FIELD_USER, AuthController.newInstance().getUser().getId())
                    .orderBy(Account.FIELD_DATE_TIME, Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Account> accounts = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Account account = documentSnapshot.toObject(Account.class);
                                    accounts.add(account);
                                }
                            if (completeListener != null) completeListener.onReadComplete(accounts);
                        } else {
                            if (completeListener != null) completeListener.onReadComplete(accounts);
                        }
                    });
        }

        public Task<QuerySnapshot> getAllDefaults(final ReadCompleteListener<Account> completeListener) {
            return db.collection(Account.COLLECTION)
                    .whereEqualTo(Account.FIELD_IS_DEFAULT, true)
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Account> accounts = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Account account = documentSnapshot.toObject(Account.class);
                                    accounts.add(account);
                                }
                            if (completeListener != null) completeListener.onReadComplete(accounts);
                        } else {
                            if (completeListener != null) completeListener.onReadComplete(accounts);
                        }
                    });
        }

        public Task<QuerySnapshot> getById(String id, final ReadCompleteListener<Account> completeListener) {
            return getByField(Account.FIELD_ID, id, list -> {
                if (completeListener != null) completeListener.onReadComplete(list);
            });
        }

        private Task<QuerySnapshot> getByField(String fieldName, Object value, final ReadCompleteListener<Account> completeListener) {
            return db.collection(Account.COLLECTION).whereEqualTo(fieldName, value).get().addOnCompleteListener(task -> {
                ArrayList<Account> accounts = new ArrayList<>();
                if (task.isComplete()) {
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Account category = documentSnapshot.toObject(Account.class);
                            accounts.add(category);
                        }
                    if (completeListener != null) completeListener.onReadComplete(accounts);
                } else {
                    if (completeListener != null) completeListener.onReadComplete(accounts);
                }
            });
        }

    }

    public class CollectionCategories {

        public Task<Void> add(Category category) {
            return db.collection(Category.COLLECTION).document(category.getId()).set(category);
        }

        public Task<Void> update(Category category) {
            return db.collection(Category.COLLECTION).document(category.getId()).set(category);
        }

        public Task<Void> remove(Category category) {
            return db.collection(Category.COLLECTION).document(category.getId()).delete();
        }

        public Task<QuerySnapshot> getAll(final ReadCompleteListener<Category> completeListener) {
            return db.collection(Category.COLLECTION)
                    .whereEqualTo(Category.FIELD_USER, AuthController.newInstance().getUser().getId())
                    .orderBy(Category.FIELD_DATE_TIME, Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Category> categories = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Category category = documentSnapshot.toObject(Category.class);
                                    categories.add(category);
                                }
                            completeListener.onReadComplete(categories);
                        } else {
                            completeListener.onReadComplete(categories);
                        }
                    });
        }

        public Task<QuerySnapshot> getAllDefaults(final ReadCompleteListener<Category> completeListener) {
            return db.collection(Category.COLLECTION)
                    .whereEqualTo(Category.FIELD_DEFAULT, true)
                    .orderBy(Category.FIELD_DATE_TIME, Query.Direction.DESCENDING)
                    .get()
                    .addOnCompleteListener(task -> {
                        ArrayList<Category> categories = new ArrayList<>();
                        if (task.isComplete()) {
                            if (task.getResult() != null)
                                for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                                    Category category = documentSnapshot.toObject(Category.class);
                                    categories.add(category);
                                }
                            completeListener.onReadComplete(categories);
                        } else {
                            completeListener.onReadComplete(categories);
                        }
                    });
        }


        public Task<QuerySnapshot> getById(String id, final ReadCompleteListener<Category> completeListener) {
            return getByField(Category.FIELD_ID, id, list -> {
                if (completeListener != null) completeListener.onReadComplete(list);
            });
        }

        public Task<QuerySnapshot> getByType(CategoryType type, final ReadCompleteListener<Category> completeListener) {
            return getByField(Category.FIELD_TYPE, type, list -> {
                if (completeListener != null) completeListener.onReadComplete(list);
            });
        }


        private Task<QuerySnapshot> getByField(String fieldName, Object value, final ReadCompleteListener<Category> completeListener) {
            return db.collection(Category.COLLECTION).whereEqualTo(fieldName, value).get().addOnCompleteListener(task -> {
                ArrayList<Category> categories = new ArrayList<>();
                if (task.isComplete()) {
                    if (task.getResult() != null)
                        for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                            Category category = documentSnapshot.toObject(Category.class);
                            categories.add(category);
                        }
                    if (completeListener != null) completeListener.onReadComplete(categories);
                } else {
                    if (completeListener != null) completeListener.onReadComplete(categories);
                }
            });
        }
    }


    public static String generateRandomKey() {
        return UUID.randomUUID().toString();
    }
}
