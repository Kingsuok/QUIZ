package dbOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDiskIOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import db.DatabaseHelper;
import db.DatabaseService;

/**
 *  operate question table
 * Created by su on 2016/10/1.
 */

public class TableQuestionOperation implements DatabaseService {
    private DatabaseHelper helper;
    public TableQuestionOperation(Context context, String name){
        helper = new DatabaseHelper(context, name);// create a database helper, but a database has not been created
    }

    @Override
    public boolean add( Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "insert into questions(title, A, B, C, D, answer) values(?,?,?,?,?,?)";
            database = helper.getWritableDatabase();
            database.execSQL(sql,params);
            flag = true;
        } catch ( SQLiteDiskIOException e){ // space is not enough exception
            e.printStackTrace();
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean delete(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "delete from questions where title = ?";
            database = helper.getWritableDatabase();
            database.execSQL(sql,params);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    @Override
    public boolean update(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "update question set A = ?, B = ?, C = ?, D = ?, answer = ? where title = ?";
            database = helper.getWritableDatabase();
            database.execSQL(sql,params);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    @Override
    public HashMap<String, String> viewOne(String[] selectArgs) {
        HashMap<String, String> result = new HashMap<String, String>();
        SQLiteDatabase database = null;
        try{
            String sql = "select * from questions where title = ?";

            database = helper.getWritableDatabase();
            Cursor cursor = database.rawQuery(sql,selectArgs);
            // get the number of column
            int columns = cursor.getColumnCount();
            while (cursor.moveToNext()){
                for (int i = 0; i < columns; i++){
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
                    // deal with blank value
                    if (columnValue == null){
                        columnValue = "";
                    }

                    result.put(columnName,columnValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        System.out.print(result);
        return result;
    }

    public HashMap<String, String> oneQuestion(String[] selectArgs){
        HashMap<String, String> result = new HashMap<String, String>();
        SQLiteDatabase database = null;
        try{
            String sql = "select * from questions where title in (select leftQuestion from completion where id = ? )";

            database = helper.getWritableDatabase();
            Cursor cursor = database.rawQuery(sql,selectArgs);
            // get the number of column
            int columns = cursor.getColumnCount();
            while (cursor.moveToNext()){
                for (int i = 0; i < columns; i++){
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
                    // deal with blank value
                    if (columnValue == null){
                        columnValue = "";
                    }

                    result.put(columnName,columnValue);
                }
            }
        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        System.out.print(result);
        return result;
    }

    public List<Map<String, String>> list5Ones(String[] selectArgs) {
        List<Map<String, String>> list  = new ArrayList<Map<String, String>>();
        SQLiteDatabase database = null;
        //String[] selectArgs = new String[]{""};
        try {
            String sql = "select * from questions where title in (select leftQuestion from completion where id = ? ) order by id limit 5";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql,selectArgs);
            int columns = cursor.getColumnCount();
            while (cursor.moveToNext()){
                Map<String, String> result = new HashMap<String, String>();
                for (int i = 0; i < columns; i++ ){
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
                    result.put(columnName,columnValue);
                    // deal with blank value
                    if (columnValue == null){
                        columnValue = "";
                    }
                }
                list.add(result);
            }


        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return list;
    }

    public HashSet<String> titleSet(){
        HashSet<String> result  = new HashSet<String>();
        SQLiteDatabase database = null;
       // String[] selectArgs = new String[]{""};
        try {
            String sql = "select title from questions";
            database = helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql,null);
            //int columns = cursor.getColumnCount();
            while (cursor.moveToNext()){


                    String columnName = cursor.getColumnName(0);
                    String columnValue = cursor.getString(cursor.getColumnIndex(columnName));

                    // deal with blank value
                    if (columnValue == null){
                        columnValue = "";
                    }

                result.add(columnValue);
            }


        } catch (Exception e){
            e.printStackTrace();
        }finally {
            if (database != null){
                database.close();
            }
        }
        return result;
    }

    @Override
    public List<Map<String, String>> listOnes(String[] selectArgs) {

        return null;
    }



    @Override
    public boolean alter(Object[] params) {
        return false;
    }

    @Override
    public boolean emptyTable() {
        SQLiteDatabase database = null;
        boolean flag = false;
        try {
            String sql = "delete from questions";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
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

    @Override
    public boolean addColumn(Objects[] parames) {
        return false;
    }

    @Override
    public boolean deleteColumn(Objects[] parames) {
        return false;
    }


}
