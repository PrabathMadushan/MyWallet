package database.firebase.models;

import java.io.Serializable;
import java.util.Date;

import database.firebase.firestore.FirestoreController;

public class Category implements Serializable {


    public final static String COLLECTION = "CATEGORIES";

    public final static String FIELD_ID = "id";
    public final static String FIELD_NAME = "name";
    public final static String FIELD_ICON = "icon";
    public final static String FIELD_TYPE = "type";
    public final static String FIELD_DATE_TIME = "dateTime";
    public final static String FIELD_DEFAULT = "defaultx";
    public final static String FIELD_USER = "user";


    private final String id;
    private String name;
    private String user;
    private int icon;
    private CategoryType type;
    private Date dateTime;
    private boolean defaultx = false;

    public Category() {
        id = FirestoreController.generateRandomKey();
    }

    public Category(CategoryType type, int icon, String name, Date dateTime) {
        this.name = name;
        this.icon = icon;
        this.type = type;
        this.dateTime = dateTime;
        id = FirestoreController.generateRandomKey();
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

    public int getIcon() {
        return icon;
    }

    public void setIcon(int icon) {
        this.icon = icon;
    }

    public CategoryType getType() {
        return type;
    }

    public void setType(CategoryType type) {
        this.type = type;
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


