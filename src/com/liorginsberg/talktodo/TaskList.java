package com.liorginsberg.talktodo;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;

public class TaskList {
    private static TaskList instance = null;
    private List<Task> tasks;
    private Context context;

    private TaskList(Context context) {
	this.context = context;
	tasks = new ArrayList<Task>();
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
}
