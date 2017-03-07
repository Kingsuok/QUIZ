package dbOperation;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import db.DatabaseHelper;
import db.DatabaseService;

/**
 *  operate takers table
 * Created by su on 2016/10/3.
 */

public class TableTakersOperation implements DatabaseService {

    private DatabaseHelper helper;

    public TableTakersOperation(Context context, String name){
        helper = new DatabaseHelper(context,name);
    }

    @Override
    public boolean add(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "insert into takers(id, password, loginNum, correctNum, incorrectNum) values(?,?,?,?,?)";
            database = helper.getWritableDatabase();
            database.execSQL(sql,params);
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
    public boolean delete(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from takers where id = ?";
            database = helper.getWritableDatabase();
            database.execSQL(sql,params);
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

    public boolean updateTakerScore(Object[] parames){
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "update takers set loginNum = ?, correctNum = ?, incorrectNum = ? where id = ?";
            database = helper.getWritableDatabase();
            database.execSQL(sql, parames);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (null != database){
                database.close();
            }
        }
        return flag;
    }
    @Override
    public boolean update(Object[] parames) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "update takers set loginNum = 0, correctNum = 0, incorrectNum = 0 where id = ?";
            database = helper.getWritableDatabase();
            database.execSQL(sql,parames);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    public boolean resetAll(){
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "update takers set loginNum = 0, correctNum = 0, incorrectNum = 0";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
            flag = true;
        } catch (Exception e){
            e.printStackTrace();
        } finally {
            if (database != null){
                database.close();
            }
        }
        return flag;
    }

    @Override
    public HashMap<String, String> viewOne(String[] selectArgs) {
        HashMap<String, String> result = new HashMap<String,String>();
        SQLiteDatabase database = null;
        try {
            String sql = "select * from takers where id = ?";
            database =  helper.getReadableDatabase();
            Cursor cursor = database.rawQuery(sql, selectArgs);
            int columns = cursor.getColumnCount();
            while (cursor.moveToNext()){
                for (int i = 0; i < columns; i++){
                    String columnName = cursor.getColumnName(i);
                    String columnValue = cursor.getString(cursor.getColumnIndex(columnName));
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

        return result;
    }

    @Override
    public List<Map<String, String>> listOnes(String[] selectArgs) {


        return null;
    }

    public HashSet<String> IDSet(){
        HashSet<String> result  = new HashSet<String>();
        SQLiteDatabase database = null;
        //String[] selectArgs = new String[]{""};
        try {
            String sql = "select id from takers";
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
    public boolean alter(Object[] params) {
        return false;
    }

    @Override
    public boolean emptyTable() {
        boolean flag = false;
        SQLiteDatabase database = null;
        try{
            String sql = "delete from takers";
            database = helper.getWritableDatabase();
            database.execSQL(sql);
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
    public boolean addColumn(Objects[] parames) {
        return false;
    }

    @Override
    public boolean deleteColumn(Objects[] parames) {
        return false;
    }


}
