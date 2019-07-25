package com.prabath.mywallet.Others;

import com.prabath.mywallet.Listeners.IconCollection;
import com.prabath.mywallet.R;

import org.apache.commons.collections4.BidiMap;
import org.apache.commons.collections4.bidimap.DualHashBidiMap;

public class CategoryIcons implements IconCollection {

    private static CategoryIcons categoryIcons;
    private final BidiMap<Integer, Integer> iconss;

    private CategoryIcons() {
        iconss = new DualHashBidiMap<>();
        addIconsToMap();
    }

    public static CategoryIcons getInstance(){
        if (categoryIcons==null){
            categoryIcons=new CategoryIcons();
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
        iconss.put(i++, R.drawable.db_exp_baby);
        iconss.put(i++, R.drawable.db_exp_beauty);
        iconss.put(i++, R.drawable.db_exp_bills);
        iconss.put(i++, R.drawable.db_exp_books);

        iconss.put(i++, R.drawable.db_exp_clothing);
        iconss.put(i++, R.drawable.db_exp_entertainment);
        iconss.put(i++, R.drawable.db_exp_foods);
        iconss.put(i++, R.drawable.db_exp_fruit);

        iconss.put(i++, R.drawable.db_exp_gift);
        iconss.put(i++, R.drawable.db_exp_home);
        iconss.put(i++, R.drawable.db_exp_insurance);
        iconss.put(i++, R.drawable.db_exp_medicine);

        iconss.put(i++, R.drawable.db_exp_pet);
        iconss.put(i++, R.drawable.db_exp_shopping);
        iconss.put(i++, R.drawable.db_exp_snaks);
        iconss.put(i++, R.drawable.db_exp_social);

        iconss.put(i++, R.drawable.db_exp_sports);
        iconss.put(i++, R.drawable.db_exp_student);
        iconss.put(i++, R.drawable.db_exp_tax);
        iconss.put(i++, R.drawable.db_exp_telephone);

        iconss.put(i++, R.drawable.db_exp_transportation);
        iconss.put(i++, R.drawable.db_exp_travel);
        iconss.put(i++, R.drawable.db_exp_vehicle);
        iconss.put(i++, R.drawable.db_inc_awards);

        iconss.put(i++, R.drawable.db_inc_coupon);
        iconss.put(i++, R.drawable.db_inc_dividents);
        iconss.put(i++, R.drawable.db_inc_grants);
        iconss.put(i++, R.drawable.db_inc_investments);

        iconss.put(i++, R.drawable.db_inc_lottery);
        iconss.put(i++, R.drawable.db_inc_sale);
        iconss.put(i++, R.drawable.db_inc_refund);
        iconss.put(i++, R.drawable.db_inc_salary);



    }
}
