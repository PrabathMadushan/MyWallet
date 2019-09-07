package database.firebase.models;


import java.io.Serializable;
import java.util.Date;

import database.firebase.firestore.FirestoreController;
import database.firebase.listeners.ReadCompleteListener;

public class Record implements Serializable {

    public static final String COLLECTION = "RECORDS";

    public static final String FIELD_ID = "id";
    public static final String FIELD_VALUE = "value";
    public static final String FIELD_CATEGORY = "category";
    public static final String FIELD_ACCOUNT = "account";
    public static final String FIELD_LOCATION = "location";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_DESCRIPTION = "description";
    public static final String FIELD_DATE_TIME = "dateTime";
    public static final String FIELD_USER = "user";

    private final String id;
    private float value;
    private String category;
    private String account;
    private GLocation location;
    private String image;
    private String user;
    private String description;
    private Date dateTime;

    public Record() {
        id = FirestoreController.generateRandomKey();
    }

    public Record(float value, Category category, Account account, GLocation location, String image, String description, Date dateTime) {
        this.value = value;
        this.category = category.getId();
        this.account = account.getId();
        this.location = location;
        this.image = image;
        this.description = description;
        this.dateTime = dateTime;
        id = FirestoreController.generateRandomKey();
    }

    public String getId() {
        return id;
    }

    public float getValue() {
        return value;
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void getCategoryX(ReadCompleteListener<Category> completeListener) {
        FirestoreController.newInstance().new CollectionCategories().getById(category, completeListener);
    }

    public String getCategory() {
        return category;
    }

    public String getAccount() {
        return account;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public void getAccountX(ReadCompleteListener<Account> completeListener) {
        FirestoreController.newInstance().new CollectionAccounts().getById(account, completeListener);
    }

    public void setAccount(String account) {
        this.account = account;
    }

    public GLocation getLocation() {
        return location;
    }

    public void setLocation(GLocation location) {
        this.location = location;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }
}
