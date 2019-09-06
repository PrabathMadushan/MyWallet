package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.SelectListener;
import com.prabath.mywallet.adapters.AccountAdapter;
import com.prabath.mywallet.dialogs.MyDialog;

import java.util.ArrayList;
import java.util.List;

import database.firebase.firestore.FirestoreController;
import database.firebase.models.Account;


public class AccountsActivity extends AppCompatActivity implements SelectListener<Account> {

    private FirestoreController.CollectionAccounts collectionAccounts;
    private List<Account> accountList;
    private AccountAdapter accountAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        init();
    }

    private void initComponents() {
        collectionAccounts = FirestoreController.newInstance().new CollectionAccounts();
    }

    private void init() {
        initComponents();
        setupRecycleView();
    }


    private void setupRecycleView() {
        accountList = new ArrayList<>();
        RecyclerView recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        RecyclerView.LayoutManager layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        accountAdapter = new AccountAdapter(accountList, getEditListener(), getDeleteListener(), this);
        recyclerView.setAdapter(accountAdapter);
        showAccounts();
    }

    private SelectListener<Account> getEditListener() {
        return (i, c) -> {
            Intent intent = new Intent(this, AddNewAccountActivity.class);
            intent.putExtra(AddNewAccountActivity.TYPE, AddNewAccountActivity.TYPE_EDIT)
                    .putExtra(AddNewAccountActivity.ACCOUNT_ID, c.getId());
            startActivity(intent);
        };
    }

    private SelectListener<Account> getDeleteListener() {
        return (index, account) ->
                new MyDialog(
                        this,
                        this.getString(R.string.delete),
                        this.getString(R.string.delete_this_category),
                        MyDialog.TYPE_QUESTION,
                        (dialog, which) -> {
                            if (which == MyDialog.RESULT_YES) {
                                collectionAccounts.remove(account).addOnSuccessListener(v -> {
                                    accountList.remove(index);
                                    accountAdapter.notifyItemRemoved(index);
                                    accountAdapter.notifyItemRangeChanged(index, accountList.size());
                                }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());
                            }
                        }).show();
    }

    private void showAccounts() {
        collectionAccounts.getAll(accounts -> {
            accountList.addAll(accounts);
            accountAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });
        collectionAccounts.getAllDefaults(accounts -> {
            accountList.addAll(accounts);
            accountAdapter.notifyDataSetChanged();
        }).addOnFailureListener(e -> {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        });

    }


    public void gotoAddNewAccount(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(animator -> {
            Intent intent = new Intent(AccountsActivity.this, AddNewAccountActivity.class);
            intent.putExtra(AddNewCategoryActivity.TYPE, AddNewCategoryActivity.TYPE_NEW);
            startActivity(intent);
        }).playOn(view);
    }

    public void back(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(animator -> startActivity(new Intent(AccountsActivity.this, MainActivity.class))).playOn(view);

    }

    public static final String EXTRA_ACCOUNT = "EXR_ACCOUNT";

    @Override
    public void select(int index, Account account) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(EXTRA_ACCOUNT, account);
        startActivity(intent);
    }
}
