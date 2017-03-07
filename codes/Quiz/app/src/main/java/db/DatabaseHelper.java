package db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/**
 * Function: database helper
 * Created by su on 2016/10/1.
 */

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1; // database version

    public DatabaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory,version);
    }

    public DatabaseHelper(Context context, String name, int version){
        this(context, name, null, version);
    }

    public  DatabaseHelper(Context context, String name){
        this(context, name, VERSION);
    }
    // four tables in the database
    @Override
    public void onCreate(SQLiteDatabase db) {
        String tableQuestions = "create table questions (id integer primary key autoincrement, title text unique, A text, B text, C text, D text, answer char(1))";
        String tableTakersInfor = "create table takers (id text primary key , password text, loginNum integer, correctNum integer, incorrectNum integer )";
        String tableCompletionRate = "create table completion (id integer, leftQuestion text)";
        String tableSetTime = "create table time(id integer primary key, t integer)";
        String initialTime = "insert into time(id, t) values(1,10)";// default time is 10s
        db.execSQL(tableQuestions);
        db.execSQL(tableTakersInfor);
        db.execSQL(tableCompletionRate);
        db.execSQL(tableSetTime);
        db.execSQL(initialTime);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }
}
