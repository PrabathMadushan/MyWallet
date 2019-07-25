package database.local;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.sql.Date;
import java.sql.Time;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import database.local.models.Account;
import database.local.models.Category;
import database.local.models.CategoryType;
import database.local.models.GLocation;
import database.local.models.Record;

public class LocalDatabaseController {

    public static LocalDatabaseController controller = null;
    private SQLiteDatabase database;


    private LocalDatabaseController(LocalDatabaseHelper helper) {
         database = helper.getWritableDatabase();
    }

    public static LocalDatabaseController getInstance(LocalDatabaseHelper helper) {
        if (controller == null) {
            controller = new LocalDatabaseController(helper);
        }
        return controller;
    }

    public class TableCategory {

        public void add(Category category) {
                database.execSQL(Category.INSERT_SQL(category));
        }

        public void update(Category category) {
                database.execSQL(Category.UPDATE_SQL(category));
        }

        public void remove(Category category){
            if(!category.isDefualt()){
                database.execSQL(Category.REMOVE_SQL(category.getId()));
            }
        }

        public List<Category> get(String where) {
            List<Category> categories=new ArrayList<>();
            Cursor cursor = database.rawQuery(Category.GET_SQL(where), null);
            while (cursor.moveToNext()){
                Category category = new Category();
                String id=cursor.getString(cursor.getColumnIndex(Category.FIELD_ID));
                String name=cursor.getString(cursor.getColumnIndex(Category.FIELD_NAME));
                String icon=cursor.getString(cursor.getColumnIndex(Category.FIELD_ICON));
                String type=cursor.getString(cursor.getColumnIndex(Category.FIELD_TYPE));
                String date=cursor.getString(cursor.getColumnIndex(Category.FIELD_DATE));
                String time=cursor.getString(cursor.getColumnIndex(Category.FIELD_TIME));
                String defaultx=cursor.getString(cursor.getColumnIndex(Category.FIELD_DEFAULT));
                category.setId(id);
                category.setName(name);
                category.setType((type.equals(CategoryType.EXPENSE.toString()))?CategoryType.EXPENSE:CategoryType.INCOME);
                category.setDate(date);
                category.setTime(time);
                category.setIcon(icon);
                category.setDefualt(Boolean.parseBoolean(defaultx));
                categories.add(category);
            }

            return categories;
        }
    }

    public class TableAccount {

        public void add(Account account) {
            database.execSQL(Account.INSERT_SQL(account));
        }

        public void update(Account account) {
            database.execSQL(Account.UPDATE_SQL(account));
        }

        public void remove(Account account){
            if(!account.isDefault()){
                database.execSQL(Account.REMOVE_SQL(account.getId()));
            }
        }

        public List<Account> get(String where) {
            List<Account> accounts=new ArrayList<>();
            Cursor cursor = database.rawQuery(Account.GET_SQL(where), null);
            while (cursor.moveToNext()){
                Account account = new Account();
                String id=cursor.getString(cursor.getColumnIndex(Account.FIELD_ID));
                String name=cursor.getString(cursor.getColumnIndex(Account.FIELD_NAME));
                String icon=cursor.getString(cursor.getColumnIndex(Account.FIELD_ICON));
                String des=cursor.getString(cursor.getColumnIndex(Account.FIELD_DES));
                String date=cursor.getString(cursor.getColumnIndex(Account.FIELD_DATE));
                String time=cursor.getString(cursor.getColumnIndex(Account.FIELD_TIME));
                String defaultx=cursor.getString(cursor.getColumnIndex(Account.FIELD_DEFAULT));
                account.setId(id);
                account.setName(name);
                account.setDate(date);
                account.setTime(time);
                account.setIcon(icon);
                account.setDes(des);
                account.setDefault(Boolean.parseBoolean(defaultx));
                accounts.add(account);
            }

            return accounts;
        }
    }

    public class TableRecord {

        public void add(Record record) {
            database.execSQL(Record.INSERT_SQL(record));
        }

        public void update(Record record) {
            database.execSQL(Record.UPDATE_SQL(record));
        }

        public void remove(Record record){
                database.execSQL(Record.REMOVE_SQL(record.getId()));
        }

        public List<Record> get(String where) {
            List<Record> records=new ArrayList<>();
            Cursor cursor = database.rawQuery(Record.GET_SQL(where), null);
            while (cursor.moveToNext()){
                String id=cursor.getString(cursor.getColumnIndex(Record.FIELD_ID));
                String value=cursor.getString(cursor.getColumnIndex(Record.FIELD_VALUE));
                Category category = new TableCategory().get(Category.FIELD_ID + "='" + cursor.getString(cursor.getColumnIndex(Record.FIELD_CATEGORY)) + "'").get(0);
                Account account = new TableAccount().get(Account.FIELD_ID + "='" + cursor.getString(cursor.getColumnIndex(Record.FIELD_ACCOUNT)) + "'").get(0);
                String location=cursor.getString(cursor.getColumnIndex(Record.FIELD_LOCATION));
                String image=cursor.getString(cursor.getColumnIndex(Record.FIELD_IMAGE));
                String description=cursor.getString(cursor.getColumnIndex(Record.FIELD_DESCRIPTION));
                Date date = Date.valueOf(cursor.getString(cursor.getColumnIndex(Record.FIELD_DATE)));
                Time time = Time.valueOf(cursor.getString(cursor.getColumnIndex(Record.FIELD_TIME)));
                Record record=new Record()
                        .setId(id)
                        .setValue(Float.parseFloat(value))
                        .setCategory(category)
                        .setAccount(account)
                        .setLocation(new GLocation(location))
                        .setImage(image)
                        .setDescription(description)
                        .setDate(date)
                        .setTime(time);
                records.add(record);
            }

            return records;
        }
    }

    public static String genareteRandomKey(){
        return UUID.randomUUID().toString();
    }



}
