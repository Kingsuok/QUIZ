package dbOperation;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import db.DatabaseHelper;
import db.DatabaseService;

/**
 * operate completion table, very taker and the questions which he or she has not finish are stored in the table
 * when a taker finished one question, then a record was deleted form this table. the left questions are stored.
 * the completion table :
 * takerid     leftQuestion
 *   su             1
 *   su             2
 *   su             3
 *   na             1
 *   na             2
 * Created by su on 2016/10/3.
 */

public class TableCompletionOperation implements DatabaseService {
    private DatabaseHelper helper;

    public TableCompletionOperation(Context context, String name){
        helper = new DatabaseHelper(context, name);
    }

    @Override
    public boolean add(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "insert into completion(id, leftQuestion) values(?, ?)";
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
    public boolean delete(Object[] params) {
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from completion where id = ? and leftQuestion = ?";
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

    public boolean deleteBasedOnId(Object[] params){
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from completion where id = ?";
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

    public boolean deleteBasedOnTitle(Object[] params){
        boolean flag = false;
        SQLiteDatabase database = null;
        try {
            String sql = "delete from completion where leftQuestion = ?";
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
    public boolean update(Object[] parames) {

        return false;
    }

    @Override
    public Map<String, String> viewOne(String[] selectArgs) {
        return null;
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
            String sql = "delete from completion";
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
