package ir.rasen.myapplication.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class HistoryDatabase {

    public static final String DB_HISTORY = "HISTORY_DB";
    public final static String TABLE_HISTORY_BUSINESS = "BUSINESS_TB";
    public final static String TABLE_HISTORY_PRODUCTS = "PRODUCTS_TB";
    public final static String TABLE_HISTORY_USERS = "USERS_TB";
    public final static String FIELD_ID = "_id";
    public final static String FIELD_HISTORY = "HISTORY";

    private SQLiteDatabase db;
    private Helper helper;
    private String table;

    public HistoryDatabase(Context context, String table) {

        this.table = table;
        helper = new Helper(context, DB_HISTORY, null, 1);
        db = helper.getWritableDatabase();

    }

    public long insert(String text)
    {
        if(db.query(table, new String[] {FIELD_ID, FIELD_HISTORY},
                FIELD_HISTORY+" LIKE '"+ text +"'", null, null, null, null).getCount()>0) return -1;
        ContentValues values = new ContentValues();
        values.put(FIELD_HISTORY, text);
        return db.insert(table, null, values);
    }

    public Cursor getHistory(String text)
    {
        return db.query(table, new String[] {FIELD_ID, FIELD_HISTORY},
                FIELD_HISTORY+" LIKE '"+ text +"%' ORDER BY " + FIELD_ID + " DESC", null, null, null, null);
    }

    private class Helper extends SQLiteOpenHelper
    {

        public Helper(Context context, String name, SQLiteDatabase.CursorFactory factory,
                      int version) {
            super(context, name, factory, version);
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("CREATE TABLE "+TABLE_HISTORY_BUSINESS+" ("+
                    FIELD_ID+" integer primary key autoincrement, "+FIELD_HISTORY+" text);");
            db.execSQL("CREATE TABLE "+TABLE_HISTORY_PRODUCTS+" ("+
                    FIELD_ID+" integer primary key autoincrement, "+FIELD_HISTORY+" text);");
            db.execSQL("CREATE TABLE "+TABLE_HISTORY_USERS+" ("+
                    FIELD_ID+" integer primary key autoincrement, "+FIELD_HISTORY+" text);");
            Log.d("HISTORY", "DBs CREATED");
        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        }

    }

}