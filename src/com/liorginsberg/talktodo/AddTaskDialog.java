package com.liorginsberg.talktodo;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;

public class AddTaskDialog extends Activity {

	private Button btnSaveTask;
	private Button btnCancelAddTask;
	private EditText etAddTaskTitle;
	private EditText etAddTaskDesc;
	private ImageButton btnSetDate;
	private ImageButton btnSetTime;
	private EditText etDate;
	private EditText etTime;
	private Intent returnIntent;
	private int hour, minute, day, month, year;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_add_task_dialog);
		returnIntent = new Intent();
		etAddTaskTitle = (EditText) findViewById(R.id.etAddTaskTitle);
		etAddTaskDesc = (EditText) findViewById(R.id.etAddTaskDesc);
		btnSetDate = (ImageButton) findViewById(R.id.btnSetDate);
		btnSetTime = (ImageButton) findViewById(R.id.btnSetTime);
		etDate = (EditText) findViewById(R.id.etDate);
		etTime = (EditText) findViewById(R.id.etTime);
		btnSaveTask = (Button) findViewById(R.id.btnSaveTask);
		btnCancelAddTask = (Button) findViewById(R.id.btnCancelAddTask);
		etDate.setFocusable(false);
		etDate.setClickable(false);
		etTime.setFocusable(false);
		etTime.setClickable(false);

		final MyOnSetDateListener myOnSetDateListener = new MyOnSetDateListener();
		final MyOnSetTimeListener myOnSetTimeListener = new MyOnSetTimeListener();

		btnSetDate.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Calendar cal = Calendar.getInstance();
				DatePickerDialog datePickerDialog = new DatePickerDialog(AddTaskDialog.this, myOnSetDateListener, cal.get(Calendar.YEAR), cal
						.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH));
				datePickerDialog.show();
			}
		});

		btnSetTime.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				TimePickerDialog timePickerDialog = new TimePickerDialog(AddTaskDialog.this, myOnSetTimeListener, 0, 0, true);
				timePickerDialog.show();

			}
		});

		btnSaveTask.setOnClickListener(new OnClickListener() {

			private Date date;

			public void onClick(View v) {
				String taskTitle = etAddTaskTitle.getText().toString();
				String taskDesc = etAddTaskDesc.getText().toString();
				String dateTimeString = etDate.getText().toString() + " " + etTime.getText().toString();  
				SimpleDateFormat  format = new SimpleDateFormat("dd/MM/yyyy HH:mm");  
				try {  
				    date = format.parse(dateTimeString);  
				    System.out.println(dateTimeString);  
				} catch (Exception e) {    
				    e.printStackTrace();  
				}
				if (!taskTitle.equals("")) {
					TaskList.getInstance(getApplicationContext()).addTask(taskTitle, taskDesc, date, false, false);
					setResult(RESULT_OK, returnIntent);
					finish();
				} else {
					Log.i("addTaskDialog", "The Title canot be empty");
				}
			}
		});

		btnCancelAddTask.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private class MyOnSetDateListener implements OnDateSetListener {

		public void onDateSet(DatePicker dp, int year, int monthOfYear, int dayOfMonth) {
			AddTaskDialog.this.year = year;
			AddTaskDialog.this.month = monthOfYear;
			AddTaskDialog.this.day = dayOfMonth;

			etDate.setText(String.format("%02d/%02d/%02d", dayOfMonth, monthOfYear, year));
		}
	}

	private class MyOnSetTimeListener implements OnTimeSetListener {

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			AddTaskDialog.this.hour = hourOfDay;
			AddTaskDialog.this.minute = minute;
			etTime.setText(String.format("%02d:%02d", hourOfDay, minute));
		}
	}
}
