package com.prabath.mywallet.Others;

import com.prabath.mywallet.Listeners.IconCollection;
import com.prabath.mywallet.R;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class AccountIcons implements IconCollection {

    private static AccountIcons categoryIcons = null;
    private final BidiMap<Integer, Integer> iconss;

    private AccountIcons() {
        iconss = new DualHashBidiMap<>();
        addIconsToMap();
    }

    public static AccountIcons getInstance(){
        if (categoryIcons==null){
            categoryIcons=new AccountIcons();
        }
        return categoryIcons;
    }



    @Override
    public int getIcon(int id) {
        return iconss.get(id);
    }

    @Override
    public int getId(int icon) {
        return iconss.getKey(icon);
    }

    @Override
    public int getIconCount(){
        return iconss.size();
    }


    private void addIconsToMap() {
        int i=0;
        iconss.put(i++, R.drawable.db_acc_bank_building);
        iconss.put(i++, R.drawable.db_acc_contract);
        iconss.put(i++, R.drawable.db_acc_credit);
        iconss.put(i++, R.drawable.db_acc_credit_cardxx);

        iconss.put(i++, R.drawable.db_acc_creditcard);
        iconss.put(i++, R.drawable.db_acc_creditcardx);
        iconss.put(i++, R.drawable.db_acc_debitcard);
        iconss.put(i++, R.drawable.db_acc_funds);

        iconss.put(i++, R.drawable.db_acc_loan);
        iconss.put(i++, R.drawable.db_acc_loanx);
        iconss.put(i++, R.drawable.db_acc_loanxx);
        iconss.put(i++, R.drawable.db_acc_online_payment);

        iconss.put(i++, R.drawable.db_acc_piggy_bank);
        iconss.put(i++, R.drawable.db_acc_scholarship);
        iconss.put(i++, R.drawable.db_acc_wallet);
        iconss.put(i++, R.drawable.db_acc_walletx);

    }
}
