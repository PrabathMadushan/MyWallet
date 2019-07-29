package database.firebase.models;

import java.io.Serializable;
import java.util.Date;

import database.firebase.FirebaseController;

public class Category implements Serializable {


    public final static String COLLECTION = "CATEGORIES";

    public final static String FIELD_ID = "id";
    public final static String FIELD_NAME = "name";
    public final static String FIELD_ICON = "icon";
    public final static String FIELD_TYPE = "type";
    public final static String FIELD_DATE = "dateTime";
    public final static String FIELD_DEFAULT = "defaultx";


    private final String id;
    private String name;
    private String icon;
    private CategoryType type;
    private Date dateTime;
    private boolean defaultx = false;

    public Category() {
        id = FirebaseController.genareteRandomKey();
    }

    public Category(CategoryType type, String icon, String name, Date dateTime) {
        this.name = name;
        this.icon = icon;
        this.type = type;
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
}


