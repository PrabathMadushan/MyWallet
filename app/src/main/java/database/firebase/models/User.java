package database.firebase.models;


import java.io.Serializable;
import java.util.Date;

import database.firebase.firestore.FirestoreController;


public class User implements Serializable {

    public static final String COLLECTION = "USERS";

    public static final String FIELD_ID = "id";
    public static final String FIELD_USERNAME = "username";
    public static final String FIELD_EMAIL = "email";
    public static final String FIELD_PASSWORD = "password";
    public static final String FIELD_IMAGE = "image";
    public static final String FIELD_CONTACT = "contact";
    public static final String FIELD_DATETIME = "dateTime";
    private String id;
    private String username;
    private String email;
    private String password;
    private String image;
    private String contact;
    private Date dateTime;

    public User() {
        id = FirestoreController.generateRandomKey();
    }

    public Date getDateTime() {
        return dateTime;
    }

    public void setDateTime(Date dateTime) {
        this.dateTime = dateTime;
    }


    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }


    @Override
    public String toString() {
        return username;
    }
}
