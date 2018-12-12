package com.example.kapis.securevault.Notes;

public class Notes {

    public static final String TABLE_NAME = "notes";

    public static final String COLUMN_ID                     = "id";
    public static final String COLUMN_TITLE                  = "title";
    public static final String COLUMN_BODY                   = "body";
    public static final String COLUMN_TIMESTAMP              = "timestamp";

    private int id;
    private String title;
    private String body;
    private String timestamp;


    // Create table SQL query
    public static final String CREATE_TABLE =
            "CREATE TABLE " + TABLE_NAME + "("
                    + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"
                    + COLUMN_TITLE + " TEXT,"
                    + COLUMN_BODY + " TEXT,"
                    + COLUMN_TIMESTAMP + " DATETIME DEFAULT CURRENT_TIMESTAMP"
                    + ")";

    public Notes() {
    }

    public Notes(int id, String title, String body, String timestamp) {
        this.id = id;
        this.title = title;
        this.body = body;
        this.timestamp = timestamp;
    }

    public int getId() {
        return id;
    }
    public String getTitle() {
        return title;
    }
    public String getBody() {
        return body;
    }
    public String getTimestamp() {
        return timestamp;
    }

    public void setId(int id) {
        this.id = id;
    }
    public void setTitle(String title) {
        this.title = title;
    }
    public void setBody(String body) { this.body = body; }
    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }


}
