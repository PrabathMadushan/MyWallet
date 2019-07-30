package database.firebase.models;


import java.io.Serializable;
import java.util.Date;

import database.firebase.FirebaseController;

public class Account implements Serializable {

    public final static String COLLECTION = "ACCOUNTS";

    public final static String FIELD_ID = "id";
    public final static String FIELD_NAME = "name";
    public final static String FIELD_DES = "des";
    public final static String FIELD_ICON = "icon";
    public final static String FIELD_DATE_TIME = "dateTime";
    public final static String FIELD_IS_DEFAULT = "defaultx";
    public final static String FIELD_USER = "user";


    private final String id;
    private String name;
    private String des;
    private int icon;
    private Date dateTime;
    private boolean defaultx;
    private String user;


    public Account() {
        id = FirebaseController.genareteRandomKey();
    }

    public Account(String name, String des, int icon, Date dateTime) {
        this.name = name;
        this.des = des;
        this.icon = icon;
        this.dateTime = dateTime;
        id = FirebaseController.genareteRandomKey();
    }

    public String getId() {
        return id;
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public boolean isDefaultx() {
        return defaultx;
    }

    public void setDefaultx(boolean defaultx) {
        this.defaultx = defaultx;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
