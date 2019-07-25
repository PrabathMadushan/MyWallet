package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.prabath.mywallet.fregments.TitleFragment;

import java.util.List;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Account;
import database.local.models.Record;

public class AccountActivity extends AppCompatActivity {

    private Account account;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        account = (Account) getIntent().getSerializableExtra(AccountsActivity.EXTRA_ACCOUNT);
        init();
    }


    private void init(){
        initTittleFragment();
        showRecords();
    }
    private void initTittleFragment() {
        TitleFragment titleFragment = TitleFragment.getInstance(R.drawable.db_inc_investments, "Account", this.getClass());
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.replace(R.id.fragmentTitle, titleFragment);
        transaction.commit();
    }

    private void showRecords(){
        LocalDatabaseController.TableRecord tableRecord = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(this)).new TableRecord();
        List<Record> records = tableRecord.get(Record.FIELD_ACCOUNT + "='" + account.getId() + "'");
        for (Record record : records) {
            System.out.println(record.getValue());
        }

    }

    public static String EXTRA_RECORD = "exRecord";

    public void addNewRecord(View view) {
        Record record = new Record();
        record.setAccount(account);
        Intent intent = new Intent(this, CategorySelectorActivity.class);
        intent.putExtra(EXTRA_RECORD, record);
        startActivity(intent);
    }
}
