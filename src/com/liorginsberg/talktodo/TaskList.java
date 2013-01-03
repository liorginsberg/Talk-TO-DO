package com.liorginsberg.talktodo;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

public class TaskList implements Observer {
	private static TaskList instance = null;
	private List<Task> tasks;
	private Context context;

	private TaskDB taskDB;

	private TaskList(Context context) {
		this.context = context;
		tasks = new ArrayList<Task>();
		taskDB = new TaskDB(context);
	}

	public static TaskList getInstance(Context context) {
		if (instance == null) {
			instance = new TaskList(context);
		}
		return instance;
	}

	public List<Task> getTasks() {
		return tasks;
	}

	public Task getTaskAt(int position) {
		return tasks.get(position);
	}

	public int addTask(String title, String desc, String from, String to, String location, int isChecked, boolean updateRemoteDB) {
		int task_id = (int) taskDB.open().insertTask(title, desc, from, to, location, isChecked);
		tasks.add(new Task(task_id, title, desc, from, to, location, isChecked));
		if (updateRemoteDB) {
			SharedPreferences prefs = context.getSharedPreferences(Main.PREFS_NAME, 0);
			new HttpPostRequest().add(prefs.getString("userID", "-1"), task_id, title, desc, from, to, location, isChecked);
		}
		return task_id;
	}

	public void updateTask(int position, int task_id, String title, String desc, String from, String to, String location, int isChecked) {
		boolean updated = taskDB.updateTask(task_id, title, desc, from, to, location, isChecked);
		if (updated) {
			tasks.set(position, new Task(task_id, title, desc, from, to, location, isChecked));
		}
	}

	public boolean setDone(int task_id, int done) {
		SharedPreferences prefs = context.getSharedPreferences(Main.PREFS_NAME, 0);
		if (context.getSharedPreferences(Main.PREFS_NAME, 0).getBoolean("backupTasks", false)) {
			new HttpPostRequest().setChecked(prefs.getString("userID", "-1"), task_id, done);
		}
		return taskDB.setDone(task_id, done);
	}

	public int removeTask(int pos) {
		int taskID = tasks.get(pos).getTask_id();
		int rmRes = taskDB.removeTask(taskID);
		if (rmRes == 1) {
			if (context.getSharedPreferences(Main.PREFS_NAME, 0).getBoolean("backupTasks", false)) {
				SharedPreferences prefs = context.getSharedPreferences(Main.PREFS_NAME, 0);
				new HttpPostRequest().remove(prefs.getString("userID", "-1"), taskID);
			}
			tasks.remove(pos);
			return 1;
		}
		return -1;
	}

	public void getAllTasksFromDB() {
		tasks = taskDB.getAll();
		if (tasks.size() == 0) {
			SharedPreferences prefs = context.getSharedPreferences(Main.PREFS_NAME, 0);
			if (prefs.getBoolean("backupTasks", false)) {
				HttpPostRequest httpPostRequest = new HttpPostRequest();
				httpPostRequest.Register(this);
				httpPostRequest.getTaskFromRemoteDB(prefs.getString("userID", "-1"));
			}
		}
	}

	public void removeAllTasksFromDB() {
		taskDB.removeAllTasks();
	}

	public void update(String response) {
		JSONObject taskJASON = null;
		try {
			taskJASON = new JSONObject(response);
			JSONArray tasksArray = taskJASON.getJSONArray("tasks");
			Log.i("DATA", tasksArray.toString());

			for (int i = 0; i < tasksArray.length(); i++) {
				JSONObject t = tasksArray.getJSONObject(i);
				int row_id = t.getInt("row_id");
				String task_title = t.getString("task_title");
				String task_desc = t.getString("task_desc");
				String task_from = t.getString("task_from");
				String task_to = t.getString("task_to");
				String task_location = t.getString("task_location");
				int task_checked = t.getInt("task_checked");

				int new_id = addTask(task_title, task_desc, task_from, task_to, task_location, task_checked, false);
				SharedPreferences prefs = context.getSharedPreferences(Main.PREFS_NAME, 0);
				new HttpPostRequest().updateID(row_id, prefs.getString("userID", "-1"), new_id);
			}
		} catch (JSONException e) {
			e.printStackTrace();
		} finally {
			TaskAdapter.getInstance(context, R.layout.tasklist_item).notifyDataSetChanged();
		}
	}

}
