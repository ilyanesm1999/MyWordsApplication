package soft.example.com.mywordsapplication;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;

import soft.example.com.mywordsapplication.sqliteasset.SQLiteAssetHelper;

/**
 * Created by admin on 27.12.2015.
 */
public class MyDBHelper extends SQLiteAssetHelper {

    private static final String DATABASE_NAME = "words.sqlite";
    private static final int DATABASE_VERSION = 1;


    public MyDBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public HashMap<String, Integer>  getAllWords(){
        HashMap<String, Integer> words = new HashMap<String, Integer>();

        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        Cursor c = db.rawQuery("SELECT UNAME, LENG FROM sg_entry", null);

        if (c.moveToFirst()) {
            while(c.moveToNext()) {
                words.put(c.getString(0),c.getInt(1));
            }
        }
        c.close();
        return words;
    }

    public HashMap<String, Object> getWordRow(String word){
        Log.i("mytag2", "word " + word);
        HashMap<String, Object> map = new HashMap<String, Object>();
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        Object obj = null;
        Log.i("mytag2", "word2 " + word);
        try {
            cursor = db.rawQuery("SELECT * FROM sg_entry WHERE UNAME=?", new String[]{word + ""});
            if (cursor.getCount() > 0) {
                cursor.moveToFirst();

                for (int i = 0; i < cursor.getColumnCount(); i++) {
                    switch (cursor.getType(i)) {
                        case Cursor.FIELD_TYPE_INTEGER:
                            obj = cursor.getInt(i);

                            break;
                        case Cursor.FIELD_TYPE_STRING:
                            obj = cursor.getString(i);
                            break;
                    }
                    Log.i("mytag", " column: " + cursor.getColumnName(i) + " value: " + obj.toString());
                    map.put(cursor.getColumnName(i), obj);
                    Log.i("mytag2", "word3 " + word);
                }
            }
        }catch(Exception ex){
            Log.i("mytag2", "Exception " + ex.getMessage());
        }finally {
            Log.i("mytag2", "word4 " + word);
            if(cursor != null)cursor.close();
            Log.i("mytag2", "word5 " + word);
        }
        Log.i("mytag2", "word6 " + word);
        return map;
    }



    public String  getValueByKey(String key){


        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = null;
        String empName = "";
        try{

            cursor = db.rawQuery("SELECT value FROM config WHERE key=?", new String[] {key + ""});

            if(cursor.getCount() > 0) {

                cursor.moveToFirst();
                empName = cursor.getString(cursor.getColumnIndex("value"));
            }

        }finally {

            cursor.close();
        }
        Log.i("mytag", "got "+key+" = "+empName);
        return empName;
    }

    public void setAnybody(String table,String keyColumn,String keyValue,String valueColumn,String newValue){
        String res = "";
        SQLiteDatabase db = getReadableDatabase();
        SQLiteQueryBuilder qb = new SQLiteQueryBuilder();
        String query = "UPDATE `"+table+"` SET `"+valueColumn+"` = '"+newValue+"'  WHERE `"+keyColumn+"` = '"+keyValue+"'";
        Log.i("mytag", " query is " + query);
        Cursor c = db.rawQuery(query, null);
        c.moveToFirst();
        c.close();
    }

    public void setValueByKey(String key,String newValue){
        setAnybody("config", "key", key, "value", newValue);
    }

    public void setValueByKey(String key,int newValue){
        setValueByKey(key,String.valueOf(newValue));
    }
}
