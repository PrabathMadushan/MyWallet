package com.prabath.mywallet;


import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.sql.Date;

import database.local.models.Account;
import database.local.models.User;


public class TestActivity extends AppCompatActivity {

    FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        db = FirebaseFirestore.getInstance();

    }

    public void readData(View view) {
        final EditText txtData = findViewById(R.id.txtData);
        CollectionReference users = db.collection("users");
        users.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isComplete()) {
                    String data = "";
                    for (QueryDocumentSnapshot documentSnapshot : task.getResult()) {
                        User user = documentSnapshot.toObject(User.class);
                        data += user.getEmail() + "/" + user.getDate().toString() + ",account:"
                                + user.getAccount().getName();

                    }
                    txtData.setText(data);
                } else {
                    Toast.makeText(TestActivity.this, "Error getting document", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void saveUser(View view) {
        User user = new User();
        user.setUsername("Kamal");
        user.setPassword("1234");
        user.setContact("0775866255");
        user.setEmail("kamal@gmail.com");
        user.setDate(new Date(new java.util.Date().getTime()));
        Account account = new Account();
        account.setId("10");
        account.setDes("this is description");
        account.setName("bank");
        user.setAccount(account);
        db.collection("users").document().set(user)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Toast.makeText(TestActivity.this, "Saveed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(TestActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onBackPressed() {

    }
}
