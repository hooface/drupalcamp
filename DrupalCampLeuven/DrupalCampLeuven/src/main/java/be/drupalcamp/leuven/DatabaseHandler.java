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

    // Events table name.
    public static final String TABLE_SESSIONS = "sessions";

    // Events table column names.
    public static final String KEY_ID = "id";
    public static final String KEY_TITLE = "name";
    public static final String KEY_DESCRIPTION = "description";
    public static final String KEY_SPECIAL = "special";
    public static final String KEY_START_DATE = "start_date";
    public static final String KEY_END_DATE = "end_date";
    public static final String KEY_LEVEL = "level";

    // Favorites table name.
    public static final String TABLE_FAVORITES = "favorites";

    // Favorites table column names.
    public static final String FAVORITES_KEY_ID = "fav_id";


    public DatabaseHandler(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        String CREATE_EVENTS_TABLE = "CREATE TABLE " + TABLE_SESSIONS + "(" +
                "" + KEY_ID + " INTEGER PRIMARY KEY," +
                "" + KEY_TITLE + " TEXT," +
                "" + KEY_DESCRIPTION + " TEXT," +
                "" + KEY_SPECIAL + " INTEGER," +
                "" + KEY_START_DATE + " INTEGER," +
                "" + KEY_END_DATE + " INTEGER," +
                "" + KEY_LEVEL + " INTEGER" +
                ")";
        db.execSQL(CREATE_EVENTS_TABLE);

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
                    cursor.getInt(6)
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
        values.put(KEY_ID, session.getId());
        values.put(KEY_TITLE, session.getTitle());
        values.put(KEY_DESCRIPTION, session.getDescription());
        values.put(KEY_SPECIAL, session.getSpecial());
        values.put(KEY_START_DATE, session.getStartDate());
        values.put(KEY_END_DATE, session.getEndDate());
        values.put(KEY_LEVEL, session.getLevel());

        // Inserting Row
        db.insert(TABLE_SESSIONS, null, values);
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
        selectQuery += " te LEFT JOIN " + DatabaseHandler.TABLE_FAVORITES + " tf ON te." + DatabaseHandler.KEY_ID + " = tf." + DatabaseHandler.FAVORITES_KEY_ID + " ";
        selectQuery += " WHERE " + KEY_ID + " = " + id;
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
                cursor.getInt(6)
        );
    }
}
