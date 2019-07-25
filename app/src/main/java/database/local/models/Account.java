package database.local.models;

import androidx.annotation.NonNull;

import java.io.Serializable;

public class Account implements Serializable {

    public static final String TABLE_NAME="ACCOUNT";

    public static final String FIELD_ID="ID";
    public static final String FIELD_NAME="NAME";
    public static final String FIELD_ICON="ICON";
    public static final String FIELD_DES="DES";
    public static final String FIELD_DATE="DATE";
    public static final String FIELD_TIME="TIME";
    public static final String FIELD_DEFAULT="DEFAULTX";


    public static String CREATE_SQL="CREATE TABLE "+TABLE_NAME+" (" +
            "    "+FIELD_ID+"       STRING PRIMARY KEY," +
            "    "+FIELD_NAME+" STRING," +
            "    "+FIELD_ICON+" STRING," +
            "    "+FIELD_DES+"    STRING," +
            "    "+FIELD_DATE+"    STRING," +
            "    "+FIELD_TIME+"    STRING," +
            "    "+FIELD_DEFAULT+"    STRING" +
            ");";
    public static String INSERT_SQL(Account account){
        return "INSERT INTO "+TABLE_NAME+"(" +
                FIELD_ID+","+
                FIELD_NAME+","+
                FIELD_ICON+","+
                FIELD_DES+","+
                FIELD_DATE+","+
                FIELD_TIME+","+
                FIELD_DEFAULT+") "+
                "VALUES(" +
                "'" + account.getId() + "'," +
                "'" + account.getName() + "'," +
                "'" + account.getIcon() + "'," +
                "'" + account.getDes() + "'," +
                "'" + account.getDate() + "'," +
                "'" + account.getTime() + "'," +
                "'" + account.isDefault() + "'" +
                ");";
    }

    public static String UPDATE_SQL(Account account){
        return "UPDATE "+TABLE_NAME+" SET " +
                ""+FIELD_NAME+"='"  + account.getName() + "'," +
                ""+FIELD_ICON+"='"  + account.getIcon() + "'," +
                ""+FIELD_DES+"='"  + account.getDes() + "'" +
                " WHERE "+FIELD_ID+"='"+account.getId()+"';";
    }

    public static String REMOVE_SQL(String id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + FIELD_ID + "='" + id + "' AND " + FIELD_DEFAULT + "!= '" + true + "'";
        return sql;
    }

    public static String GET_SQL(String where) {
        if (where == null) {
            return "SELECT * FROM " + Account.TABLE_NAME;
        } else {
            return "SELECT * FROM " + Account.TABLE_NAME + " WHERE " + where;
        }
    }

    private String id;
    private String name;
    private String des;
    private String icon;
    private String date;
    private String time;
    private boolean defaultx;


    public Account() {
    }

    public Account(
            String id,
            String name,
            String des,
            String icon,
            String date,
            String time
    ) {
        this.id = id;
        this.name = name;
        this.des = des;
        this.icon = icon;
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

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
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
