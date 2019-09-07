package com.prabath.mywallet;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.daimajia.androidanimations.library.Techniques;
import com.daimajia.androidanimations.library.YoYo;
import com.prabath.mywallet.Listeners.RecordSelectListener;
import com.prabath.mywallet.adapters.RecordAdapter;

import java.util.ArrayList;
import java.util.List;

import database.firebase.firestore.FirestoreController;
import database.firebase.models.Account;
import database.firebase.models.CategoryType;
import database.firebase.models.Record;

//import database.local.LocalDatabaseController;
//import database.local.LocalDatabaseHelper;
//import database.local.models.Account;
//import database.local.models.Record;

public class AccountActivity extends AppCompatActivity implements RecordSelectListener {


    private Account account;
    private FirestoreController.CollectionRecords collectionRecords;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_account);
        account = (Account) getIntent().getSerializableExtra(AccountsActivity.EXTRA_ACCOUNT);
        init();
    }

    private void init() {
        collectionRecords = FirestoreController.newInstance().new CollectionRecords();
        setupRecycleView();
        showValues();
    }

    float expence = 0;
    float income = 0;
    float balunce = 0;

    private void showValues() {
        TextView txtExpense = findViewById(R.id.txtExpence);
        TextView txtIncome = findViewById(R.id.txtIncome);
        TextView txtBalance = findViewById(R.id.txtBalance);

        for (Record r : records) {
            r.getCategoryX(c -> {
                if (c.get(0).getType() == CategoryType.INCOME) {
                    income += r.getValue();
                } else {
                    expence += r.getValue();
                }
                balunce = income - expence;
                txtBalance.setText("Rs " + balunce);
                txtExpense.setText("Rs " + expence);
                txtIncome.setText("Rs " + income);
            });
        }


    }


    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private List<Record> records;
    private RecordAdapter recordAdapter;

    private void setupRecycleView() {
        records = new ArrayList<>();
        recyclerView = findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        //recyclerView.setItemAnimator(new DefaultItemAnimator());
        recordAdapter = new RecordAdapter(records, this);
        recyclerView.setAdapter(recordAdapter);
        showRecords();
    }


    @Override
    public void onSelect(int position, Record record) {

    }


    private void showRecords() {
        // LocalDatabaseController.TableRecord tableRecord = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(this)).new TableRecord();
        //List<Record> records = tableRecord.get(Record.FIELD_ACCOUNT + "='" + account.getId() + "'");
        collectionRecords.getByAccount(account.getId(), rs -> {
            this.records.addAll(rs);
            recordAdapter.notifyDataSetChanged();
            showValues();
        }).addOnFailureListener(e -> Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show());

    }

    public static String EXTRA_RECORD = "EXTRA_RECORD";

    public void addNewRecord(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(a -> {
            Record record = new Record();
            record.setAccount(account.getId());
            Intent intent = new Intent(this, CategorySelectorActivity.class);
            intent.putExtra(EXTRA_RECORD, record);
            startActivity(intent);
        }).playOn(view);
    }

    public void gotoAccounts(View view) {
        YoYo.with(Techniques.RubberBand).duration(500).onEnd(a -> {
            Intent intent = new Intent(this, AccountsActivity.class);
            startActivity(intent);
        }).playOn(view);

    }
}
