package or.connect.todolist;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by moulib on 1/25/15.
 */
public class TasksDatabase extends SQLiteOpenHelper {

    // Database Version
    private static final int DATABASE_VERSION = 1;

    // Database Name
    private static final String DATABASE_NAME = "tasksDatabase";

    // Tasks table name
    private static final String TABLE_TASKS = "tasks";

    // Todo Table Columns names
    private static final String KEY_ID = "taskId";
    private static final String KEY_NAME = "taskName";
    private static final String KEY_PRIORITY = "taskPriority";

    public TasksDatabase(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    // creating initial tables
    @Override
    public void onCreate(SQLiteDatabase db) {
        // SQL for creating the tables
        // Construct a table for tasks
        String CREATE_TASKS_TABLE = "CREATE TABLE " + TABLE_TASKS + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_NAME + " TEXT,"
                + KEY_PRIORITY + " INTEGER" + ")";
        db.execSQL(CREATE_TASKS_TABLE);
    }

    // This method is called when database is upgraded like
    // modifying the table structure, adding constraints to database, etc.
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion,
                          int newVersion) {
        // SQL for upgrading the tables
        if (newVersion == 1) {
            // Wipe older tables if existed
            db.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS);
            // Create tables again
            onCreate(db);
        }
    }

    // Insert record into the database
    public void addTask(Task task) {
        // Open database connection
        SQLiteDatabase db = this.getWritableDatabase();
        // Define values for each field
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_PRIORITY, task.getTaskPriority());

        // Insert Row
        db.insertOrThrow(TABLE_TASKS, null, values);
        db.close(); // Closing database connection
    }

    // Returns a single task by id
    public Task getTask(int id) {
        // Open database for reading
        SQLiteDatabase db = this.getReadableDatabase();

        // Construct and execute query
        Cursor cursor = db.query(TABLE_TASKS,  // TABLE
                new String[] { KEY_ID, KEY_NAME, KEY_PRIORITY }, // SELECT
                KEY_ID + "= ?", new String[] { String.valueOf(id) },  // WHERE, ARGS
                null, null, null, null); // GROUP BY, HAVING, ORDER BY
        if (cursor != null) {
            cursor.moveToFirst();
        }

        // Load result into model object
        Task task = new Task(cursor.getString(1), cursor.getInt(2));
        task.setTaskId(cursor.getInt(cursor.getColumnIndexOrThrow(KEY_ID)));

        // return task
        return task;
    }

    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> tasks = new ArrayList<Task>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Task task = new Task(cursor.getString(1), cursor.getInt(2));
                task.setTaskId(cursor.getInt(0));
                // Adding task to list
                tasks.add(task);
            } while (cursor.moveToNext());
        }

        Collections.sort(tasks);

        // return tasks
        return tasks;
    }

    public Cursor getTaskCursor() {
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_TASKS;

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // return cursor
        return cursor;
    }

    public int getTaskCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TASKS;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    public int updateTask(Task task) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();
        // Setup fields to update
        ContentValues values = new ContentValues();

        values.put(KEY_NAME, task.getTaskName());
        values.put(KEY_PRIORITY, task.getTaskPriority());

        // Updating row
        int result = db.update(TABLE_TASKS, values, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getTaskId()) });

        // Close the database
        db.close();

        return result;
    }

    public void deleteTask(Task task) {
        // Open database for writing
        SQLiteDatabase db = this.getWritableDatabase();

        // Delete the record with the specified id
        db.delete(TABLE_TASKS, KEY_ID + " = ?",
                new String[] { String.valueOf(task.getTaskId()) });

        // Close the database
        db.close();
    }
}
