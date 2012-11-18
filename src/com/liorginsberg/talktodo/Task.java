package com.liorginsberg.talktodo;

public class Task {
    private int taskId;
    
    private String title;
    private static int nextId = 0;
    
    public int getTaskId() {
        return taskId;
    }

    
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Task(String title) {
	this.title = title;
	this.taskId = nextId;
	nextId++;
    }
   
}
