package database.local.models;


import java.io.Serializable;
import java.sql.Date;
import java.sql.Time;

public class Record implements Serializable {

    private String id;
    private float value;
    private Category category;
    private Account account;
    private GLocation location;
    private String image;
    private String description;
    private Date date;
    private Time time;

    public static final String TABLE_NAME = "RECORD";

    public static final String FIELD_ID = "ID";
    public static final String FIELD_VALUE = "VALUE";
    public static final String FIELD_CATEGORY = "CAT";
    public static final String FIELD_ACCOUNT = "ACC";
    public static final String FIELD_LOCATION = "LOCATION";
    public static final String FIELD_IMAGE = "IMAGE";
    public static final String FIELD_DESCRIPTION = "DESCRIPTION";
    public static final String FIELD_DATE = "DATE";
    public static final String FIELD_TIME = "TIME";


    public static String CREATE_SQL = "CREATE TABLE " + TABLE_NAME + " (" +
            "    " + FIELD_ID + "       STRING PRIMARY KEY," +
            "    " + FIELD_VALUE + " STRING," +
            "    " + FIELD_CATEGORY + " STRING," +
            "    " + FIELD_ACCOUNT + " STRING," +
            "    " + FIELD_LOCATION + " STRING," +
            "    " + FIELD_IMAGE + " STRING," +
            "    " + FIELD_DESCRIPTION + " STRING," +
            "    " + FIELD_DATE + " DATE," +
            "    " + FIELD_TIME + " TIME" +
            ");";

    public static String INSERT_SQL(Record record) {
        return "INSERT INTO " + TABLE_NAME + "(" +
                FIELD_ID + "," +
                FIELD_VALUE + "," +
                FIELD_CATEGORY + "," +
                FIELD_ACCOUNT + "," +
                FIELD_LOCATION + "," +
                FIELD_IMAGE + "," +
                FIELD_DESCRIPTION + "," +
                FIELD_DATE + "," +
                FIELD_TIME + ") " +
                "VALUES(" +
                "'" + record.getId() + "'," +
                "'" + record.getValue() + "'," +
                "'" + record.getCategory().getId() + "'," +
                "'" + record.getAccount().getId() + "'," +
                "'" + record.getLocation().toString() + "'," +
                "'" + record.getImage() + "'," +
                "'" + record.getDescription() + "'," +
                "'" + record.getDate() + "'," +
                "'" + record.getTime() + "'" +
                ");";
    }

    public static String UPDATE_SQL(Record record) {
        return "UPDATE " + TABLE_NAME + " SET " +
                "" + FIELD_VALUE + "='" + record.getValue() + "'," +
                "" + FIELD_CATEGORY + "='" + record.getCategory().getId() + "'," +
                "" + FIELD_LOCATION + "='" + record.getLocation().toString() + "'," +
                "" + FIELD_IMAGE + "='" + record.getImage() + "'," +
                "" + FIELD_DESCRIPTION + "='" + record.getDescription() + "'," +
                "" + FIELD_DATE + "='" + record.getDate() + "'," +
                "" + FIELD_TIME + "='" + record.getTime() + "'" +
                " WHERE " + FIELD_ID + "='" + record.getId() + "';";
    }

    public static String REMOVE_SQL(String id) {
        String sql = "DELETE FROM " + TABLE_NAME + " WHERE " + FIELD_ID + "='" + id + "'";
        return sql;
    }

    public static String GET_SQL(String where) {
        if (where == null) {
            return "SELECT * FROM " + TABLE_NAME;
        } else {
            return "SELECT * FROM " + TABLE_NAME + " WHERE " + where;
        }
    }


    public Record() {
    }

    public String getId() {
        return id;
    }

    public Record setId(String id) {
        this.id = id;
        return this;
    }

    public float getValue() {
        return value;
    }

    public Record setValue(float value) {
        this.value = value;
        return this;
    }

    public Category getCategory() {
        return category;
    }

    public Record setCategory(Category category) {
        this.category = category;
        return this;
    }

    public Account getAccount() {
        return account;
    }

    public Record setAccount(Account account) {
        this.account = account;
        return this;
    }

    public GLocation getLocation() {
        return location;
    }

    public Record setLocation(GLocation location) {
        this.location = location;
        return this;
    }

    public String getImage() {
        return image;
    }

    public Record setImage(String image) {
        this.image = image;
        return this;
    }

    public String getDescription() {
        return description;
    }

    public Record setDescription(String description) {
        this.description = description;
        return this;
    }

    public Date getDate() {
        return date;
    }

    public Record setDate(Date date) {
        this.date = date;
        return this;
    }

    public Time getTime() {
        return time;
    }

    public Record setTime(Time time) {
        this.time = time;
        return this;
    }
}
