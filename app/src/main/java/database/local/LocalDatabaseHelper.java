package database.local;

import android.annotation.SuppressLint;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.prabath.mywallet.Others.AccountIcons;
import com.prabath.mywallet.Others.CategoryIcons;
import com.prabath.mywallet.R;

import java.text.SimpleDateFormat;
import java.util.Date;

import database.local.models.Account;
import database.local.models.Category;
import database.local.models.CategoryType;
import database.local.models.Record;

public class LocalDatabaseHelper extends SQLiteOpenHelper {

    private static LocalDatabaseHelper helper;

    private LocalDatabaseHelper(Context c) {
        super(c, "mywallet", null, 2);
    }

    public static LocalDatabaseHelper getInstance(Context context) {
        if (helper == null) {
            helper = new LocalDatabaseHelper(context);
        }
        return helper;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(friend());
        db.execSQL(account());
        db.execSQL(category());
        db.execSQL(message());
        db.execSQL(notification());
        db.execSQL(record());
        db.execSQL(transfer());
        db.execSQL(user());
        defaultCategories(db);
        defaultAccounts(db);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    private void defaultCategories(SQLiteDatabase db) {
        int[] icons_expense={
                R.drawable.db_exp_baby,
                R.drawable.db_exp_beauty,
                R.drawable.db_exp_bills,
                R.drawable.db_exp_books,
                R.drawable.db_exp_clothing,
                R.drawable.db_exp_entertainment,
                R.drawable.db_exp_foods,
                R.drawable.db_exp_fruit,
                R.drawable.db_exp_gift,
                R.drawable.db_exp_home,
                R.drawable.db_exp_insurance,
                R.drawable.db_exp_medicine,
                R.drawable.db_exp_pet,
                R.drawable.db_exp_shopping,
                R.drawable.db_exp_snaks,
                R.drawable.db_exp_social,
                R.drawable.db_exp_sports,
                R.drawable.db_exp_student,
                R.drawable.db_exp_tax,
                R.drawable.db_exp_telephone,
                R.drawable.db_exp_transportation,
                R.drawable.db_exp_travel,
                R.drawable.db_exp_vehicle,

        };
        String[] names_expense={
                "Baby",
                "Beauty",
                "Bills",
                "Books",
                "Clothing",
                "Entertainment",
                "Foods",
                "Fruits",
                "Gift",
                "Home",
                "Insurance",
                "Medicine",
                "Pets",
                "Shopping",
                "Snaks",
                "Social",
                "Sports",
                "Students",
                "Tax",
                "Telephone",
                "Transportation",
                "Travel",
                "Vehicles",
        };
        int[] icons_income={
                R.drawable.db_inc_awards,
                R.drawable.db_inc_coupon,
                R.drawable.db_inc_dividents,
                R.drawable.db_inc_grants,
                R.drawable.db_inc_investments,
                R.drawable.db_inc_lottery,
                R.drawable.db_inc_refund,
                R.drawable.db_inc_salary,
                R.drawable.db_inc_sale,
        };
        String[] names_income={
                "Awards",
                "Coupons",
                "Dividents",
                "Grants",
                "Investments",
                "Lotteries",
                "refunds",
                "Salary",
                "Sales",
        };

        CategoryIcons iconsGeter = CategoryIcons.getInstance();
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") String today = new SimpleDateFormat("yyyy/MM/dd").format(date);
        @SuppressLint("SimpleDateFormat") String now = new SimpleDateFormat("hh:mm:ss").format(date);

        for(int i=0;i<icons_income.length;i++){
            Category c1 = new Category(
                    LocalDatabaseController.genareteRandomKey(),
                    CategoryType.INCOME,
                    iconsGeter.getId(icons_income[i]) + "",
                    names_income[i],
                    today,
                    now
            );
            c1.setDefault(true);
            db.execSQL(Category.INSERT_SQL(c1));
        }
        for(int i=0;i<icons_expense.length;i++){
            Category c1 = new Category(
                    LocalDatabaseController.genareteRandomKey(),
                    CategoryType.EXPENSE,
                    iconsGeter.getId(icons_expense[i]) + "",
                    names_expense[i],
                    today,
                    now
            );
            c1.setDefault(true);
            db.execSQL(Category.INSERT_SQL(c1));
        }


    }

    private void defaultAccounts(SQLiteDatabase db){

        int[] icons= {
                R.drawable.db_acc_wallet,
                R.drawable.db_acc_bank_building
        };
        String[] names={
                "Wallet",
                "Bank",
        };
        String[] des={
                "Your Wallet",
                "You Bank AccountActivity"
        };

        AccountIcons iconsGeter = AccountIcons.getInstance();
        Date date = new Date();
        @SuppressLint("SimpleDateFormat") String today = new SimpleDateFormat("yyyy/MM/dd").format(date);
        @SuppressLint("SimpleDateFormat") String now = new SimpleDateFormat("hh:mm:ss").format(date);
        for(int i=0;i<icons.length;i++){
            Account a = new Account(
                    LocalDatabaseController.genareteRandomKey(),
                    names[i],
                    des[i],
                    iconsGeter.getId(icons[i]) + "",
                    today,
                    now
            );
            a.setDefault(true);
            db.execSQL(Account.INSERT_SQL(a));
        }
    }


    private String account() {
        return Account.CREATE_SQL;
    }

    private String category() {
        return Category.CREAT_SQL;
    }

    private String friend() {
        return "CREATE TABLE FRIEND (" +
                "    id       STRING PRIMARY KEY," +
                "    friend_id       STRING" +
                ");";
    }

    private String message() {
        return "CREATE TABLE MESSAGE (" +
                "    id       STRING PRIMARY KEY," +
                "    to_id STRING," +
                "    message    STRING," +
                "    is_send BOOLEAN," +
                "    is_read    BOOLEAN" +
                ");";
    }

    private String notification() {
        return "CREATE TABLE NOTIFICATION (" +
                "    id       STRING PRIMARY KEY," +
                "    to_id STRING," +
                "    message    STRING," +
                "    is_read BOOLEAN" +
                ");";
    }

    private String record() {
        return Record.CREATE_SQL;
    }

    private String transfer() {
        return "CREATE TABLE TRANSFER (" +
                "    id       STRING PRIMARY KEY," +
                "    value DOUBLE," +
                "    from_id    STRING," +
                "    to_id STRING" +
                ");";
    }

    private String user() {
        return "CREATE TABLE USER (" +
                "    id       STRING PRIMARY KEY," +
                "    username STRING," +
                "    email    STRING," +
                "    password STRING," +
                "    image    STRING," +
                "    contact    STRING" +
                ");";
    }
}
