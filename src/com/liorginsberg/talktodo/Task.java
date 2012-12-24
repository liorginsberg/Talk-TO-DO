package com.liorginsberg.talktodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

public class Task {
	
	private int taskID;
	private String title;
	private String desc;
	private Calendar calendarFrom;
	private Calendar calendarTo;
	private int isCrossed;
	private int isChecked;

	
	public Task(int taskID, String title, String desc, String from, String to, int isCrossed, int isChecked) {
		this.taskID = taskID;
		this.title = title;
		this.desc = desc;
		
		calendarFrom = stringDateToCalendar(from);
		calendarTo = stringDateToCalendar(to);
		
		this.isCrossed = isCrossed;
		this.isChecked = isChecked;
	}
	
	private Calendar stringDateToCalendar(String date) {
		 SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.US);
		 Date d = null;
		 try {
			 d = dateFormat.parse(date);
		 } catch (Exception e) {
			e.printStackTrace();
		}
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(d);
		return calendar;
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

	public void setCrossed(int isCrossed) {
		this.isCrossed = isCrossed;
	}

	public int isCrossed() {
		return isCrossed;
	}

	public int isChecked() {
		return isChecked;
	}

	public void setChecked(int isChecked) {
		this.isChecked = isChecked;
	}
	
	public String getFromTo() {
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:dd", Locale.US); 
		StringBuilder sb = new StringBuilder();
		String a = String.valueOf(calendarFrom.get(Calendar.MONTH));
		String test = sdf.format(calendarFrom.getTime());
		sb.append(test);
		sb.append(" - ");
		sb.append(sdf.format(calendarTo.getTime()));
		return sb.toString();
	}
}
