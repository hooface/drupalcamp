package be.drupalcamp.leuven;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHandler extends SQLiteOpenHelper {

    // Database Version.
    private static final int DATABASE_VERSION = 1;

    // Database name.
    private static final String DATABASE_NAME = "DrupalCamp";

    // Sessions table name.
    public static final String TABLE_SESSIONS = "sessions";

    // Sessions table column names.
    public static final String SESSIONS_KEY_ID = "id";
    public static final String SESSIONS_KEY_TITLE = "name";
    public static final String SESSIONS_KEY_DESCRIPTION = "description";
    public static final String SESSIONS_KEY_SPECIAL = "special";
    public static final String SESSIONS_KEY_START_DATE = "start_date";
    public static final String SESSIONS_KEY_END_DATE = "end_date";
    public static final String SESSIONS_KEY_LEVEL = "level";
    public static final String SESSIONS_KEY_DAY = "day";

    // Speaker table name.
    public static final String TABLE_SPEAKERS = "speakers";

    // Speakers table column names.
    public static final String SPEAKERS_KEY_ID = "id";
    public static final String SPEAKERS_KEY_SESSION_ID = "session_id";
    public static final String SPEAKERS_KEY_USERNAME = "username";
    public static final String SPEAKERS_KEY_FIRSTNAME = "firstname";
    public static final String SPEAKERS_KEY_LASTNAME = "lastname";
    public static final String SPEAKERS_KEY_ORG = "organisation";
    public static final String SPEAKERS_KEY_TWITTER = "twitter";
    public static final String SPEAKERS_KEY_AVATAR = "avatar";

    // Favorites table name.
    public static final String TABLE_FAVORITES = "favorites";

    // Favorites table column names.
    public static final String FAVORITES_KEY_ID = "fav_id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_SESSIONS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "(" +
                "" + SESSIONS_KEY_ID + " INTEGER PRIMARY KEY," +
                "" + SESSIONS_KEY_TITLE + " TEXT," +
                "" + SESSIONS_KEY_DESCRIPTION + " TEXT," +
                "" + SESSIONS_KEY_SPECIAL + " INTEGER," +
                "" + SESSIONS_KEY_START_DATE + " INTEGER," +
                "" + SESSIONS_KEY_END_DATE + " INTEGER," +
                "" + SESSIONS_KEY_LEVEL + " INTEGER" +
                "" + SESSIONS_KEY_DAY + " INTEGER" +
                ")";
        db.execSQL(CREATE_SESSIONS_TABLE);

        String CREATE_SPEAKERS_TABLE = "CREATE TABLE " + TABLE_SPEAKERS + "(" +
                "" + SPEAKERS_KEY_ID + " INTEGER PRIMARY KEY," +
                "" + SPEAKERS_KEY_SESSION_ID + " INTEGER," +
                "" + SPEAKERS_KEY_USERNAME + " TEXT," +
                "" + SPEAKERS_KEY_FIRSTNAME + " TEXT," +
                "" + SPEAKERS_KEY_LASTNAME + " TEXT," +
                "" + SPEAKERS_KEY_ORG + " TEXT," +
                "" + SPEAKERS_KEY_TWITTER + " TEXT" +
                "" + SPEAKERS_KEY_AVATAR + " TEXT" +
                ")";
        db.execSQL(CREATE_SPEAKERS_TABLE);

        String CREATE_FAVORITES_TABLE = "CREATE TABLE " + TABLE_FAVORITES + "(" +
                "" + FAVORITES_KEY_ID + " INTEGER" +
                ")";
        db.execSQL(CREATE_FAVORITES_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // No upgrades.
    }

    // Set favorite status for an event.
    public void saveFavorite(int favorite, int eventId) {
        SQLiteDatabase db = this.getWritableDatabase();

        // Always delete first.
        assert db != null;
        db.delete(TABLE_FAVORITES, FAVORITES_KEY_ID + " = ?",
                new String[] { "" + eventId });

        // Insert if favorite is 1.
        if (favorite == 1) {
            ContentValues values = new ContentValues();
            values.put(FAVORITES_KEY_ID, eventId);
            db.insert(TABLE_FAVORITES, null, values);
        }

        db.close();
    }

    // Truncate the table, this only happens for update.
    public void truncateTable() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_SESSIONS, null, null);
        db.close();
    }

    /**
     * Get events
     *
     * @param selectQuery
     *   The query.
     *
     * @return <List>Session
     *   A list of events.
     */
    public List<Session> getSessions(String selectQuery) {
        List<Session> sessionList = new ArrayList<Session>();

        SQLiteDatabase db = this.getWritableDatabase();
        assert db != null;
        Cursor cursor = db.rawQuery(selectQuery, null);

        // Loop through all rows and add to list.
        if (cursor.moveToFirst()) {
            do {
                Session session = new Session(
                    cursor.getInt(0),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getInt(3),
                    cursor.getInt(4),
                    cursor.getInt(5),
                    cursor.getInt(6),
                    cursor.getInt(7)
                );

                // Add session to list.
                sessionList.add(session);
            }
            while (cursor.moveToNext());
        }

        db.close();

        return sessionList;
    }

    // Insert session.
    public void insertSession(Session session) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SESSIONS_KEY_ID, session.getId());
        values.put(SESSIONS_KEY_TITLE, session.getTitle());
        values.put(SESSIONS_KEY_DESCRIPTION, session.getDescription());
        values.put(SESSIONS_KEY_SPECIAL, session.getSpecial());
        values.put(SESSIONS_KEY_START_DATE, session.getStartDate());
        values.put(SESSIONS_KEY_END_DATE, session.getEndDate());
        values.put(SESSIONS_KEY_LEVEL, session.getLevel());

        db.insert(TABLE_SESSIONS, null, values);
        db.close();
    }

    // Insert speaker.
    public void insertSpeaker(Speaker speaker) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(SPEAKERS_KEY_ID, speaker.getId());
        values.put(SPEAKERS_KEY_SESSION_ID, speaker.getSessionId());
        values.put(SPEAKERS_KEY_USERNAME, speaker.getUsername());
        values.put(SPEAKERS_KEY_FIRSTNAME, speaker.getFirstName());
        values.put(SPEAKERS_KEY_LASTNAME, speaker.getLastName());
        values.put(SPEAKERS_KEY_ORG, speaker.getOrganisation());
        values.put(SPEAKERS_KEY_TWITTER, speaker.getTwitter());
        values.put(SPEAKERS_KEY_AVATAR, speaker.getAvatar());

        db.insert(TABLE_SPEAKERS, null, values);
        db.close();
    }

    // Get number of sessions.
    public int getSessionCount() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor dataCount = db.rawQuery("select * from " + TABLE_SESSIONS, null);
        int count = dataCount.getCount();
        db.close();
        return count;
    }

    // Get single session.
    public Session getSession(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        assert db != null;
        String selectQuery = "SELECT * FROM " + TABLE_SESSIONS;
        selectQuery += " te LEFT JOIN " + DatabaseHandler.TABLE_FAVORITES + " tf ON te." + DatabaseHandler.SESSIONS_KEY_ID + " = tf." + DatabaseHandler.FAVORITES_KEY_ID + " ";
        selectQuery += " WHERE " + SESSIONS_KEY_ID + " = " + id;
        Cursor cursor = db.rawQuery(selectQuery, null);

        if (cursor != null) {
            cursor.moveToFirst();
        }

        assert cursor != null;
        return new Session(
                cursor.getInt(0),
                cursor.getString(1),
                cursor.getString(2),
                cursor.getInt(3),
                cursor.getInt(4),
                cursor.getInt(5),
                cursor.getInt(6),
                cursor.getInt(7)
        );
    }
}
