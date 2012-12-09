package com.liorginsberg.talktodo;

import java.util.Date;

public class Task {
	
	private int taskID;
	private String title;
	private String desc;
	private Date date;
	private boolean isCrossed;
	private boolean isChecked;

	
	public Task(int taskID, String title, String desc, Date date, boolean isCrossed, boolean isChecked) {
		this.taskID = taskID;
		this.title = title;
		this.desc = desc;
		this.date = date;
		this.isCrossed = isCrossed;
		this.isChecked = isChecked;
	}
	
	public int getTask_id() {
		return taskID;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDesc() {
		return desc;
	}

	public void setDesc(String desc) {
		this.desc = desc;
	}

	public void setCrossed(boolean isCrossed) {
		this.isCrossed = isCrossed;
	}

	public boolean isCrossed() {
		return isCrossed;
	}

	public boolean isChecked() {
		return isChecked;
	}

	public void setChecked(boolean isChecked) {
		this.isChecked = isChecked;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}
	
	public String getDateText() {
		String taskDate = "Not set";
		if(date != null) {
			taskDate.toString();
		}
		return taskDate;
	}
}
