package com.prabath.mywallet.mywallet;

import android.content.Context;

import database.local.LocalDatabaseController;
import database.local.LocalDatabaseHelper;
import database.local.models.Account;

public class MyWallet {

    private static MyWallet myWallet = null;
    private LocalDatabaseController controller;
    private LocalDatabaseHelper helper;
    private Context context;



    private MyWallet(Context context) {
        helper=LocalDatabaseHelper.getInstance(context);
        controller=LocalDatabaseController.getInstance(helper);
    }

    public static MyWallet getInstance(Context context) {
        if (myWallet == null) {
            myWallet = new MyWallet(context);
        }
        return myWallet;
    }

    private float getExpenceOfAccount(Account account){
        return 20000.00f;
    }


}
