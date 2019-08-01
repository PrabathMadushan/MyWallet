package com.prabath.mywallet;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.AccountISelectListener;
import com.prabath.mywallet.adapters.AccountAdapter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Account;

public class AccountsActivity extends AppCompatActivity implements AccountISelectListener {

    private LocalDatabaseController.TableAccount tableAccount;
    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Account> accounts;
    private AccountAdapter accountAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_accounts);
        init();
    }

    private void initComponents() {
        tableAccount = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(getApplicationContext())).new TableAccount();
    }

    private void init() {
        initComponents();
        setupRecycleView();
    }


    private void setupRecycleView() {
        accounts = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        accountAdapter = new AccountAdapter(accounts, getApplicationContext(), tableAccount, this);
        recyclerView.setAdapter(accountAdapter);
        showAccounts();
    }

    private void showAccounts() {
        List<Account> as = tableAccount.get(null);
        Collections.reverse(as);
        for (Account a : as) {
            accounts.add(a);
        }
        accountAdapter.notifyDataSetChanged();
    }


    public void gotoAddnewAccount(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                Intent intent = new Intent(AccountsActivity.this, AddNewAccountActivity.class);
                intent.putExtra(AddNewCategoryActivity.TYPE, AddNewCategoryActivity.TYPE_NEW);
                startActivity(intent);
            }
        }).playOn(view);
    }

    public void back(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(new YoYo.AnimatorCallback() {
            @Override
            public void call(Animator animator) {
                startActivity(new Intent(AccountsActivity.this, MainActivity.class));
            }
        }).playOn(view);

    }

    public static final String EXTRA_ACCOUNT="EXR_ACCOUNT";
    @Override
    public void onSelect(Account account) {
        Intent intent = new Intent(this, AccountActivity.class);
        intent.putExtra(EXTRA_ACCOUNT,account);
        startActivity(intent);
    }
}
