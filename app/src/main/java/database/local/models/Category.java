package database.local.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Category implements Serializable {


    public static String TABLE_NAME = "CATEGORY";

    public static String FIELD_ID = "ID";
    public static String FIELD_NAME = "NAME";
    public static String FIELD_ICON = "ICON";
    public static String FIELD_TYPE = "TYPE";
    public static String FIELD_DATE = "DATE";
    public static String FIELD_TIME = "TIME";
    public static String FIELD_DEFAULT = "DEFAULTX";

    public static String CREAT_SQL="CREATE TABLE " + Category.TABLE_NAME + " (" +
            "    " + Category.FIELD_ID + "       STRING PRIMARY KEY," +
            "    " + Category.FIELD_NAME + " STRING," +
            "    " + Category.FIELD_ICON + "    STRING," +
            "    " + Category.FIELD_TYPE + "    STRING," +
            "    " + Category.FIELD_DATE + "    STRING," +
            "    " + Category.FIELD_TIME + " STRING," +
            "    " + Category.FIELD_DEFAULT + " STRING" +
            ");";

    public static String INSERT_SQL(Category category) {
        return "INSERT INTO " + TABLE_NAME + "(" +
                Category.FIELD_ID + "," +
                Category.FIELD_NAME + "," +
                Category.FIELD_ICON + "," +
                Category.FIELD_TYPE + "," +
                Category.FIELD_DATE + "," +
                Category.FIELD_TIME + "," +
                Category.FIELD_DEFAULT + ")" +
                " VALUES(" +
                "'" + category.getId() + "'," +
                "'" + category.getName() + "'," +
                "'" + category.getIcon() + "'," +
                "'" + category.getType().toString() + "'," +
                "'" + category.getDate() + "'," +
                "'" + category.getTime() + "'," +
                "'" + category.isDefault() + "'" +
                ");";
    }

    public static String UPDATE_SQL(Category category) {
        return "UPDATE " + TABLE_NAME + " SET " +
                "" + Category.FIELD_NAME + "='" + category.getName() + "'," +
                "" + Category.FIELD_ICON + "='" + category.getIcon() + "'," +
                "" + Category.FIELD_TYPE + "='" + category.getType().toString() + "'" +
                " WHERE " + Category.FIELD_ID + "='" + category.getId() + "';";
    }

    public static String REMOVE_SQL(String id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + FIELD_ID + "='" + id + "' AND " + FIELD_DEFAULT + "!= '" + true + "'";
        return sql;
    }

    public static String GET_SQL(String where) {
        if (where == null) {
            return "SELECT * FROM " + Category.TABLE_NAME;
        } else {
            return "SELECT * FROM " + Category.TABLE_NAME + " WHERE " + where;
        }
    }


    private String id;
    private String name;
    private String icon;
    private CategoryType type;
    private String date;
    private String time;
    private boolean defaultx = false;

    public Category() {
    }

    public Category(
            String id
            , CategoryType type
            , String icon
            , String name
            , String date
            , String time
    ) {
        this.id = id;
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.date = date;
        this.time = time;
    }

    public boolean isDefault() {
        return defaultx;
    }

    public void setDefault(boolean defaultx) {
        this.defaultx = defaultx;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return this.getId();
    }
}


