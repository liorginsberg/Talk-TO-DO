package com.liorginsberg.talktodo;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import android.content.Context;

public class TaskList {
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

	public int addTask(String title, String desc, String from, String to, int isChecked, int isCrossed){
		int task_id = (int) taskDB.open().insertTask(title, desc, from, to, isChecked, isCrossed);
		tasks.add(new Task(task_id, title, desc, from, to, isChecked, isCrossed));
		return task_id;
	}
	
	public int removeTask(int pos) {
		int taskID = tasks.get(pos).getTask_id();
		int rmRes = taskDB.removeTask(taskID);
		if(rmRes == 1) {
			tasks.remove(pos);
			return 1;
		}
		return -1;
	}

	public void getAllTasksFromDB() {
		tasks = taskDB.getAll();
	}
	
	public void removeAllTasksFromDB() {
		taskDB.removeAllTasks();
	}
	public void populateListWithRandomTasks(int amount) {
		for (int i = 0; i < amount; i++) {
			tasks.add(new Task(i ,"auto generated task " + i, "desc for " + i, "" , "", 0, 1));
		}
	}
}
