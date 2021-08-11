package whatsapp.whtools.statusdownloader.DBHelper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by paxees on 3/10/2019.
 */

public class Database extends SQLiteOpenHelper {

    public static final String DB_NAME = "whatsapp_status_db";
    public static final String TABLE_NAME = "call_tbl";
    public static final String COLUMN_SMS_ID = "sms_id";
    public static final String COLUMN_SMSTITLE = "sms_title";
    public static final String COLUMN_SMS = "sms";
    public static final String COLUMN_DATE = "sms_date";

    public Database(Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String sql = "create table " + TABLE_NAME + " (id INTEGER PRIMARY KEY ," + COLUMN_SMSTITLE + " text" + "," + COLUMN_DATE + " text" + "," + COLUMN_SMS + " text)";
        db.execSQL(sql);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String sql = "DROP TABLE IF EXISTS " + TABLE_NAME + "";
        db.execSQL(sql);
        onCreate(db);
    }

/*    public boolean CheckNumber(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select " + COLUMN_SMSDESC + " From " + TABLE_NAME + " Where " + COLUMN_SMSDESC + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{number});
        if (cursor.getCount() > 0) {
            //data exist
            return true;
        } else
            //data not exist
            return false;
    }*/

  /*  public Cursor getCountOfCalls(String number) {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "Select * From " + TABLE_NAME + " Where " + COLUMN_SMSDESC + " =? ";
        Cursor cursor = db.rawQuery(query, new String[]{number});
        return cursor;
    }*/

    public boolean addContactNumber(String name, String date, String sms ) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COLUMN_SMSTITLE, name);
        contentValues.put(COLUMN_DATE, date);
        contentValues.put(COLUMN_SMS, sms);
        db.insert(TABLE_NAME, null, contentValues);
        db.close();
        return true;
    }

  /*  public boolean updateContactNumber(String name, String number, String count, String date) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        //These Fields should be your String values of actual column names
        cv.put(COLUMN_DATE, date);
        cv.put(COLUMN_COUNT, count);
        db.update(TABLE_NAME, cv, "call_desc = ?", new String[]{number});
        db.close();
        return true;
    }*/

//    public Cursor getArtistRecord() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT DISTINCT "+COLUMN_thisArtist+","+COLUMN_thisartistid+" FROM "+TABLE_NAME+"";
//        Cursor c = db.rawQuery(sql, null);
//        return c;
//    }
//    public Cursor getArtistRecordReverse() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT DISTINCT "+COLUMN_thisArtist+","+COLUMN_thisartistid+" FROM "+TABLE_NAME+"ORDER BY "+COLUMN_thisartistid+" DESC";
//        Cursor c = db.rawQuery(sql, null);
//        return c;
//    }

    //    public Cursor getAGeneresRecord() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT DISTINCT "+COLUMN_thisgenres+" FROM "+TABLE_NAME+"";
//        Cursor c = db.rawQuery(sql, null);
//        return c;
//    }
    public Cursor getSmsTitleSelectedId(int smsId) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SMS_ID + "= " + smsId;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    //    public Cursor getAlbumRecord() {
//        SQLiteDatabase db = this.getReadableDatabase();
//
//        String sql = "SELECT DISTINCT "+COLUMN_thisAlbumb+","+COLUMN_thisalbumbid+" FROM "+TABLE_NAME+"";
//        Cursor c = db.rawQuery(sql, null);
//        return c;
//    }
    public Cursor getSmsWhereSelectedTitle(String title) {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME + " WHERE " + COLUMN_SMSTITLE + "= " + title;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public Cursor getAllNumber() {
        SQLiteDatabase db = this.getReadableDatabase();

        String sql = "SELECT * FROM " + TABLE_NAME;
        Cursor c = db.rawQuery(sql, null);
        return c;
    }

    public boolean deleteSms() {
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME, null, null);
        return true;

    }
}

