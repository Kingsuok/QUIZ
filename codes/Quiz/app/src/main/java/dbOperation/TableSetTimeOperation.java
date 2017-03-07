package dbOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.preference.PreferenceActivity;

import java.util.HashMap;


import db.DatabaseHelper;

/**
 * Created by su on 2016/10/5.
 */

public class TableSetTimeOperation {
    private DatabaseHelper helper;
    public TableSetTimeOperation(Context context, String name){
        helper = new DatabaseHelper(context, name);// create a database helper, but a database has not been created
    }

    public boolean update(Object[] parames){
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "update time set t = ? where id = 1";
            database = helper.getWritableDatabase();
            database.execSQL(sql, parames);
            flag = true;
        }catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    public String getTime(){
        String result = new String();
        SQLiteDatabase database = null;
        try {
            String sql = "select t from time where id = 1";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql,null);
            while (cursor.moveToNext()){


                String columnName = cursor.getColumnName(0);
                String columnValue = cursor.getString(cursor.getColumnIndex(columnName));

                // deal with blank value
                if (columnValue == null){
                    columnValue = "";
                }

                result = columnValue;
            }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return result;
    }

}
