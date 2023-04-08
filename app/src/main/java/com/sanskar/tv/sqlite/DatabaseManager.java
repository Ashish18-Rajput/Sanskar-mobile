package com.sanskar.tv.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.sanskar.tv.model.PlayListModel;
import com.sanskar.tv.model.VideoPlaylistModel;

import java.util.ArrayList;
import java.util.List;

public class DatabaseManager extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "ClientDatabase";
    private static final int DATABASE_VERSION = 1;
    private static final String TABLE_NAME = "client";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_NAME = "name";
    private static final String COLUMN_URL = "url";
    private static final String IMAGE_URL = "imgurl";
    private static final String DESC = "desc";

    public DatabaseManager(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {

        String CREATE_CONTACTS_TABLE = "CREATE TABLE " + TABLE_NAME + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY," + COLUMN_NAME + " TEXT,"
                + COLUMN_URL + " TEXT,"
                + IMAGE_URL + " TEXT" + ")";
        sqLiteDatabase.execSQL(CREATE_CONTACTS_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);

        // Create tables again
        onCreate(db);
    }
   /* public void addclient(PlayListModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.get_name()); // Contact Name
        values.put(COLUMN_URL, contact.get_url());// Contact Phone
        values.put(IMAGE_URL, contact.getImg_url());

        // Inserting Row
        db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }
*/


    public boolean addclient(PlayListModel contact) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, contact.get_name()); // Contact Name
        values.put(COLUMN_URL, contact.get_url());// Contact Phone
        values.put(IMAGE_URL, contact.getImg_url());


        if(isRecordAlreadyInDb(contact.get_name())){
            //db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            db.insert(TABLE_NAME, null,values);
            //2nd argument is String containing nullColumnHack
            db.close(); // Closing database connection
            return true;

        }else {
            return false;
        }

    }




    public boolean addvideoplaylist(VideoPlaylistModel videocontact) {
        SQLiteDatabase dbb = this.getWritableDatabase();

        ContentValues valuess = new ContentValues();
        valuess.put(COLUMN_NAME, videocontact.get_name()); // Contact Name
        valuess.put(COLUMN_URL, videocontact.get_url());// Contact Phone
        valuess.put(IMAGE_URL, videocontact.getImg_url());


        if(isRecordAlreadyvideoInDb(videocontact.get_name())){
            //db.insertWithOnConflict(TABLE_NAME, null, values,SQLiteDatabase.CONFLICT_REPLACE);
            dbb.insert(TABLE_NAME, null,valuess);
            //2nd argument is String containing nullColumnHack
            dbb.close(); // Closing database connection
            return true;

        }else {
            return false;
        }

    }


    public boolean isRecordAlreadyvideoInDb(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues valuess=new ContentValues();
        valuess.put(COLUMN_NAME, name);
        String query="select * from "+TABLE_NAME+" where "+COLUMN_NAME+"='"+name+"'";
        Cursor crr=db.rawQuery(query,null);

        if(crr!=null && crr.getCount()==0){
            //  db.close();
            return true;

        }else {
            //  db.close();
            return false;
        }
    }

    public boolean isRecordAlreadyInDb(String name){
        SQLiteDatabase db=this.getWritableDatabase();
        ContentValues values=new ContentValues();
        values.put(COLUMN_NAME, name);
        String query="select * from "+TABLE_NAME+" where "+COLUMN_NAME+"='"+name+"'";
        Cursor cr=db.rawQuery(query,null);

        if(cr!=null && cr.getCount()==0){
            //  db.close();
            return true;

        }else {
            //  db.close();
            return false;
        }
    }


    PlayListModel getClient(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_NAME, new String[] { COLUMN_ID,
                        COLUMN_NAME, COLUMN_URL }, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        PlayListModel client = new PlayListModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return client;
    }

    VideoPlaylistModel getvideoClient(int id) {
        SQLiteDatabase dbb = this.getReadableDatabase();

        Cursor cursor = dbb.query(TABLE_NAME, new String[] { COLUMN_ID,
                        COLUMN_NAME, COLUMN_URL }, COLUMN_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        VideoPlaylistModel videoclient = new VideoPlaylistModel(Integer.parseInt(cursor.getString(0)),
                cursor.getString(1), cursor.getString(2));
        // return contact
        return videoclient;
    }

    public List<PlayListModel> getAllData() {
        List<PlayListModel> contactList = new ArrayList<PlayListModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                PlayListModel client = new PlayListModel();
                client.set_id(Integer.parseInt(cursor.getString(0)));
                client.set_name(cursor.getString(1));
                client.set_url(cursor.getString(2));
                client.setImg_url(cursor.getString(3));
                // Adding contact to list
                contactList.add(client);
            } while (cursor.moveToNext());
        }

        // return contact list
        return contactList;
    }




    public List<VideoPlaylistModel> getAllvideoData() {
        List<VideoPlaylistModel> videocontactList = new ArrayList<VideoPlaylistModel>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_NAME;

        SQLiteDatabase dbb = this.getWritableDatabase();
        Cursor cursor = dbb.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                VideoPlaylistModel client = new VideoPlaylistModel();
                client.set_id(Integer.parseInt(cursor.getString(0)));
                client.set_name(cursor.getString(1));
                client.set_url(cursor.getString(2));
                client.setImg_url(cursor.getString(3));
                // Adding contact to list
                videocontactList.add(client);
            } while (cursor.moveToNext());
        }

        // return contact list
        return videocontactList;
    }

    public int updateContact(PlayListModel client) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(COLUMN_NAME, client.get_name());
        values.put(COLUMN_URL, client.get_url());

        // updating row
        return db.update(TABLE_NAME, values, COLUMN_ID + " = ?",
                new String[] { String.valueOf(client.get_id()) });
    }


    public int updatevideoContact(VideoPlaylistModel videoclient) {
        SQLiteDatabase dbb = this.getWritableDatabase();

        ContentValues valuess = new ContentValues();
        valuess.put(COLUMN_NAME, videoclient.get_name());
        valuess.put(COLUMN_URL, videoclient.get_url());

        // updating row
        return dbb.update(TABLE_NAME, valuess, COLUMN_ID + " = ?",
                new String[] { String.valueOf(videoclient.get_id()) });
    }

    public void deleteContact(PlayListModel client) {
        SQLiteDatabase db = this.getReadableDatabase();
        db.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[] { String.valueOf(client.get_name()) });
        db.close();
    }

    public void deleteVideoContact(VideoPlaylistModel client) {
        SQLiteDatabase dbb = this.getReadableDatabase();
        dbb.delete(TABLE_NAME, COLUMN_NAME + " = ?", new String[] { String.valueOf(client.get_name()) });
        dbb.close();
    }

    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }
    public boolean deleteTitle(String name)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(TABLE_NAME, COLUMN_NAME + "=?", new String[]{name}) > 0;
    }
}
