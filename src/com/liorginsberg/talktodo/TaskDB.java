package com.liorginsberg.talktodo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

public class TaskDB {

	public static final String KEY_ROWID = "task_id";
	public static final String KEY_TASK_TITLE = "task_title";
	public static final String KEY_TASK_DESC = "task_desc";

	private static final String DATABASE_NAME = "TaskDB";
	private static final String TASKS_TABLE = "TasksTable";
	private static final int DATABASE_VERSION = 1;

	private String[] columns = {KEY_ROWID, KEY_TASK_TITLE, KEY_TASK_DESC};
	
	private final Context context;
	private SQLiteDatabase tasksDB;

	private DBHelper tasksDBHelper;
	

	public TaskDB(Context context) {
		this.context = context;
	}

	public TaskDB open() throws SQLException {
		tasksDBHelper = new DBHelper(context);
		tasksDB = tasksDBHelper.getWritableDatabase();
		return this;	
	}
	
	public void close() {
		tasksDBHelper.close();
	}
	
	public long insertTask(String title, String desc) {
		ContentValues cv = new ContentValues();
		cv.put(KEY_TASK_TITLE, title);
		cv.put(KEY_TASK_DESC, desc);
		return tasksDB.insert(TASKS_TABLE, null, cv);
	}
	
	public int removeTask(long taskID) {
		int rowEffectedCount = -1;
		try{
			open();
			rowEffectedCount = tasksDB.delete(TASKS_TABLE, KEY_ROWID + "=" + taskID , null);
		} catch (SQLException e) {
			Log.i("SQLite", "Could not delete task " + taskID);
		} finally {
			close();
		}
		return rowEffectedCount;
	}
	
	public List<Task> getAll() {
		ArrayList<Task> tasksList = new ArrayList<Task>();
		try{
			open();
			
			Cursor c = tasksDB.query(TASKS_TABLE, columns, null, null, null, null, null);
			
			int iRow = c.getColumnIndex(KEY_ROWID);
			int iTitle = c.getColumnIndex(KEY_TASK_TITLE);
			int iDesc = c.getColumnIndex(KEY_TASK_DESC);
			
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()) {
				tasksList.add(new Task(c.getInt(iRow), c.getString(iTitle), c.getString(iDesc), new Date(), false, false));
			}
		} catch (SQLException e) {
			Toast.makeText(context, "Could Not open database error", Toast.LENGTH_LONG).show();
		}finally {
			close();
		}
		return tasksList;
	}
	
	public void removeAllTasks() {
		try{
			open();
			tasksDB.delete(TASKS_TABLE, "1" , null);
		}catch (SQLException e) {
			Toast.makeText(context, "Could Not open database error", Toast.LENGTH_LONG).show();
		}finally{
			close();
		}
	}
	
	private static class DBHelper extends SQLiteOpenHelper {

		public DBHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}

		@Override
		public void onCreate(SQLiteDatabase db) {
			db.execSQL("CREATE TABLE " + TASKS_TABLE + " (" +
					KEY_ROWID + " INTEGER PRIMARY KEY AUTOINCREMENT ," +
					KEY_TASK_TITLE + " TEXT NOT NULL, " +
					KEY_TASK_DESC + " TEXT);");
		}

		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + TASKS_TABLE);
			onCreate(db);
		}
	}

	
}
