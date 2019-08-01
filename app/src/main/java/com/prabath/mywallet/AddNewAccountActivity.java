package com.prabath.mywallet;

import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.prabath.mywallet.Others.AccountIcons;
import com.prabath.mywallet.Others.DataValidater;
import com.prabath.mywallet.fregments.IconSelecterFragment;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Account;

public class AddNewAccountActivity extends AppCompatActivity {

    public static final String TYPE = "type";
    public static final String TYPE_EDIT = "edit";
    public static final String TYPE_NEW = "new";
    public static final String ACCOUNT_ID = "ct_id";


    private String type = null;
    private Account editAccount = null;

    private LocalDatabaseController.TableAccount tableAccount;

    private IconSelecterFragment iconSelecterFragment;
    private EditText name;
    private TextView description;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_account);
        init();
    }

    private void init() {
        initComponents();
        Intent intent = getIntent();
        type = intent.getStringExtra(TYPE);
        if (type.equals(TYPE_EDIT)) {
            String id = intent.getStringExtra(ACCOUNT_ID);
            setupForEdit(id);
        } else {
            setupForAddNew();
        }
    }

    private void initComponents() {
        tableAccount = LocalDatabaseController.getInstance(LocalDatabaseHelper.getInstance(this)).new TableAccount();
        name = findViewById(R.id.txtName);
        description = findViewById(R.id.txtDiscription);

    }

    private void setupForAddNew() {
        ((TextView)findViewById(R.id.btnAction)).setText("ADD");
        addFragment();
    }

    private void setupForEdit(String accountid) {
        Button action=findViewById(R.id.btnAction);
        action.setText("UPDATE");
        Drawable img = this.getResources().getDrawable( R.drawable.ic_nui_edit);
        img.setBounds( 0, 0, 60, 60 );
        action.setCompoundDrawables( img, null, null, null );
        List<Account> account = tableAccount.get(Account.FIELD_ID + "='" + accountid + "'");
        editAccount = account.get(0);
        name.setText(editAccount.getName());
        description.setText(editAccount.getDes());
        addFragment();
    }



    public void addFragment() {
        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        iconSelecterFragment = IconSelecterFragment.getInstance(AccountIcons.getInstance(), AccountIcons.getInstance().getIcon((editAccount==null)?1:Integer.parseInt(editAccount.getIcon())));
        transaction.replace(R.id.fragment_wraper, iconSelecterFragment);
        transaction.commit();
    }




    public void doAction(View view) {
        if (type.equals(TYPE_NEW)) {
            saveAccount();
        } else {
            updateAccount();
        }

    }

    private void back(){
        Intent intent=new Intent(this,AccountsActivity.class);
        startActivity(intent);
    }

    public void cancel(View view) {
        back();
    }

    private void saveAccount() {

        if(DataValidater.validateText(name)){
            Account account = new Account();
            account.setId(LocalDatabaseController.genareteRandomKey());
            account.setName(name.getText().toString());
            account.setDes(description.getText().toString());
            account.setIcon(iconSelecterFragment.getSelectedIconId() + "");
            account.setDefault(false);
            Date date=new Date();
            String today=new SimpleDateFormat("yyyy:MM:dd").format(date);
            String now=new SimpleDateFormat("hh:mm:ss").format(date);
            account.setDate(today);
            account.setTime(now);
            tableAccount.add(account);
            back();
        }
    }

    private void updateAccount() {
        if(DataValidater.validateText(name)){
            Account account = editAccount;
            account.setName(name.getText().toString());
            account.setDes(description.getText().toString());
            // account.setIcon(iconSelecterFragment.getSelectedIconId());
            tableAccount.update(account);
            back();
        }
    }
}
