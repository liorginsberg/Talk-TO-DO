package com.liorginsberg.talktodo;

import java.util.Calendar;
import java.util.Locale;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.DatePickerDialog.OnDateSetListener;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.app.TimePickerDialog.OnTimeSetListener;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TimePicker;
import android.widget.Toast;

public class AddTaskActivity extends Activity {

	private EditText etAddTaskTitle;
	private EditText etAddTaskDesc;
	private EditText spDateFrom;
	private EditText spTimeFrom;
	private EditText spDateTo;
	private EditText spTimeTo;

	private CheckBox chbRemindMe;
	private ImageButton btnAccept;
	private ImageButton btnCancel;

	private Calendar fromCalendar;
	private Calendar toCalendar;

	private MyGestureDetector myGestureDetector;
	private MyOnGestureListener myOnGestureListener;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.advance_add_task);

		fromCalendar = Calendar.getInstance();
		toCalendar = Calendar.getInstance();

		etAddTaskTitle = (EditText) findViewById(R.id.etAddTaskTitle);
		etAddTaskDesc = (EditText) findViewById(R.id.etAddTaskDesc);
		spDateFrom = (EditText) findViewById(R.id.spDateFrom);
		spTimeFrom = (EditText) findViewById(R.id.spTimeFrom);
		spDateTo = (EditText) findViewById(R.id.spDateTo);
		spTimeTo = (EditText) findViewById(R.id.spTimeTo);

		spDateFrom.setKeyListener(null);
		spTimeFrom.setKeyListener(null);
		spDateTo.setKeyListener(null);
		spTimeTo.setKeyListener(null);

		etAddTaskTitle.requestFocus();

		toCalendar.add(Calendar.HOUR_OF_DAY, 1);

		spDateFrom.setText(String.format(Locale.getDefault(), "%02d/%02d/%02d", fromCalendar.get(Calendar.DAY_OF_MONTH),
				fromCalendar.get(Calendar.MONTH) + 1, fromCalendar.get(Calendar.YEAR)));
		spTimeFrom
				.setText(String.format(Locale.getDefault(), "%02d:%02d", fromCalendar.get(Calendar.HOUR_OF_DAY), fromCalendar.get(Calendar.MINUTE)));

		spDateTo.setText(String.format(Locale.getDefault(), "%02d/%02d/%02d", toCalendar.get(Calendar.DAY_OF_MONTH),
				toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.YEAR)));
		spTimeTo.setText(String.format(Locale.getDefault(), "%02d:%02d", toCalendar.get(Calendar.HOUR_OF_DAY), toCalendar.get(Calendar.MINUTE)));

		chbRemindMe = (CheckBox) findViewById(R.id.chbRemindMe);

		myOnGestureListener = new MyOnGestureListener();
		myGestureDetector = new MyGestureDetector(this, myOnGestureListener);

		MyOnTouchListener myOnTouchListener = new MyOnTouchListener();
		spDateFrom.setOnTouchListener(myOnTouchListener);
		spDateTo.setOnTouchListener(myOnTouchListener);
		spTimeFrom.setOnTouchListener(myOnTouchListener);
		spTimeTo.setOnTouchListener(myOnTouchListener);

		btnAccept = (ImageButton) findViewById(R.id.btnAccept);
		btnAccept.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				String taskTitle = etAddTaskTitle.getText().toString();
				String taskDesc = etAddTaskDesc.getText().toString();
				String fromString = spDateFrom.getText().toString() + " " + spTimeFrom.getText().toString();
				String toString = spDateTo.getText().toString() + " " + spTimeTo.getText().toString();

				if (!taskTitle.isEmpty()) {
					int task_id = TaskList.getInstance(getApplicationContext()).addTask(taskTitle, taskDesc, fromString, toString, 0, 0);
					if (chbRemindMe.isChecked()) {
						Intent intent = new Intent(AddTaskActivity.this, MyBroadcastReceiver.class);
						intent.putExtra("title", taskTitle);
						intent.putExtra("desc", taskDesc);
						PendingIntent pendingIntent = PendingIntent.getBroadcast(AddTaskActivity.this.getApplicationContext(), task_id, intent, 0);
						AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
						Toast.makeText(
								getApplicationContext(),
								"Set remider for: " + fromCalendar.get(Calendar.DAY_OF_MONTH) + "/" + (fromCalendar.get(Calendar.MONTH)+1) + "  "
										+ fromCalendar.get(Calendar.HOUR_OF_DAY) + ":" + fromCalendar.get(Calendar.MINUTE), Toast.LENGTH_SHORT)
								.show();
						alarmManager.set(AlarmManager.RTC_WAKEUP, fromCalendar.getTimeInMillis(), pendingIntent);
					}
					setResult(RESULT_OK);
					finish();
				} else {
					Log.i("addTaskDialog", "The Title canot be empty");
				}
			}
		});

		btnCancel = (ImageButton) findViewById(R.id.btnCancel);
		btnCancel.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				setResult(RESULT_CANCELED);
				finish();
			}
		});
	}

	private class MyOnSetDateListener implements OnDateSetListener {

		private View v;

		public MyOnSetDateListener(View v) {
			this.v = v;
		}

		public void onDateSet(DatePicker dp, int year, int monthOfYear, int dayOfMonth) {
			switch (v.getId()) {

			case R.id.spDateFrom:
				fromCalendar.set(year, monthOfYear, dayOfMonth);

				// if "from date" is later than "to date"
				if (fromCalendar.after(toCalendar)) {

					// set to date equal to date
					toCalendar = (Calendar) fromCalendar.clone();

					// add to date 1 hour
					toCalendar.add(Calendar.HOUR_OF_DAY, 1);

					// set to time text
					String toTime = String.format(Locale.getDefault(), "%02d:%02d", toCalendar.get(Calendar.HOUR_OF_DAY),
							toCalendar.get(Calendar.MINUTE));
					spTimeTo.setText(toTime);

					// set to date text
					String toDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", toCalendar.get(Calendar.DAY_OF_MONTH),
							toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.YEAR));
					spDateTo.setText(toDate);
				}

				// set from date text
				String fromDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", fromCalendar.get(Calendar.DAY_OF_MONTH),
						fromCalendar.get(Calendar.MONTH) + 1, fromCalendar.get(Calendar.YEAR));
				spDateFrom.setText(fromDate);

				break;
			case R.id.spDateTo:
				Calendar tempCalendar = (Calendar) toCalendar.clone();
				tempCalendar.set(year, monthOfYear, dayOfMonth);

				// if to date is before from date
				if (tempCalendar.before(fromCalendar)) {

					// raise toast and do nothing
					Toast.makeText(AddTaskActivity.this, "Can not set \"To\" earlier than \"From\" ", Toast.LENGTH_SHORT).show();
				} else {

					// set the "to calendar" to be like the "from calendar" with
					// 1 hour later
					toCalendar.set(year, monthOfYear, dayOfMonth);

					if (toCalendar.before(fromCalendar)) {
						toCalendar = (Calendar) fromCalendar.clone();
						toCalendar.add(Calendar.HOUR_OF_DAY, 1);
					}

					// set to date text
					String toDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", toCalendar.get(Calendar.DAY_OF_MONTH),
							toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.YEAR));
					spDateTo.setText(toDate);
				}
				break;
			default:
				break;
			}
		}
	}

	private class MyOnSetTimeListener implements OnTimeSetListener {

		private View v;

		public MyOnSetTimeListener(View v) {
			this.v = v;
		}

		public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
			switch (v.getId()) {
			case R.id.spTimeFrom:
				fromCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				fromCalendar.set(Calendar.MINUTE, minute);

				if (fromCalendar.after(toCalendar)) {
					toCalendar.set(Calendar.HOUR_OF_DAY, fromCalendar.get(Calendar.HOUR_OF_DAY));
					toCalendar.set(Calendar.MINUTE, fromCalendar.get(Calendar.MINUTE));
					toCalendar.add(Calendar.HOUR_OF_DAY, 1);

					String toDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", toCalendar.get(Calendar.DAY_OF_MONTH),
							toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.YEAR));
					spDateTo.setText(toDate);

					String toTime = String.format(Locale.getDefault(), "%02d:%02d", toCalendar.get(Calendar.HOUR_OF_DAY),
							toCalendar.get(Calendar.MINUTE));
					spTimeTo.setText(toTime);

				}

				// set to date text
				String toDate = String.format(Locale.getDefault(), "%02d/%02d/%02d", toCalendar.get(Calendar.DAY_OF_MONTH),
						toCalendar.get(Calendar.MONTH) + 1, toCalendar.get(Calendar.YEAR));
				spDateTo.setText(toDate);

				spTimeFrom.setText(String.format(Locale.getDefault(), "%02d:%02d", fromCalendar.get(Calendar.HOUR_OF_DAY),
						fromCalendar.get(Calendar.MINUTE)));

				break;
			case R.id.spTimeTo:
				Calendar tempCalendar = (Calendar) toCalendar.clone();
				tempCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
				tempCalendar.set(Calendar.MINUTE, minute);

				if (tempCalendar.before(fromCalendar)) {
					Toast.makeText(AddTaskActivity.this, "Canot set \"To\" earlier than \"From\" ", Toast.LENGTH_SHORT).show();
				} else {
					toCalendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
					toCalendar.set(Calendar.MINUTE, minute);

					String toTime = String.format(Locale.getDefault(), "%02d:%02d", toCalendar.get(Calendar.HOUR_OF_DAY),
							toCalendar.get(Calendar.MINUTE));
					spTimeTo.setText(toTime);
				}
				break;
			default:
				break;
			}

		}

	}

	private class MyOnGestureListener implements OnGestureListener {

		View view;

		public void setView(View view) {
			this.view = view;
		}

		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return true;
		}

		public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX, float velocityY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onLongPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
			// TODO Auto-generated method stub
			return false;
		}

		public void onShowPress(MotionEvent e) {
			// TODO Auto-generated method stub

		}

		public boolean onSingleTapUp(MotionEvent e) {

			Log.i("Touched", "onSingleTap()");
			switch (view.getId()) {
			case R.id.spDateFrom:
				DatePickerDialog datePickerDialogFrom = new DatePickerDialog(AddTaskActivity.this, new MyOnSetDateListener(view),
						fromCalendar.get(Calendar.YEAR), fromCalendar.get(Calendar.MONTH), fromCalendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialogFrom.show();
				break;
			case R.id.spTimeFrom:
				TimePickerDialog timePickerDialogFrom = new TimePickerDialog(AddTaskActivity.this, new MyOnSetTimeListener(view),
						fromCalendar.get(Calendar.HOUR_OF_DAY), fromCalendar.get(Calendar.MINUTE), true);
				timePickerDialogFrom.show();
				break;
			case R.id.spDateTo:
				DatePickerDialog datePickerDialogTo = new DatePickerDialog(AddTaskActivity.this, new MyOnSetDateListener(view),
						toCalendar.get(Calendar.YEAR), toCalendar.get(Calendar.MONTH), toCalendar.get(Calendar.DAY_OF_MONTH));
				datePickerDialogTo.show();
				break;
			case R.id.spTimeTo:
				TimePickerDialog timePickerDialogTo = new TimePickerDialog(AddTaskActivity.this, new MyOnSetTimeListener(view),
						toCalendar.get(Calendar.HOUR_OF_DAY), fromCalendar.get(Calendar.MINUTE), true);
				timePickerDialogTo.show();
				break;
			default:
				break;
			}
			return true;

		}

	}

	private class MyOnTouchListener implements OnTouchListener {

		public boolean onTouch(View v, MotionEvent event) {
			myGestureDetector.setViewForListaner(v);
			return myGestureDetector.onTouchEvent(event);
		}

	}

	private class MyGestureDetector extends GestureDetector {

		private MyOnGestureListener myOnGestureListener;

		public MyGestureDetector(Context context, MyOnGestureListener listener) {
			super(context, listener);
			this.myOnGestureListener = listener;
		}

		public void setViewForListaner(View v) {
			myOnGestureListener.setView(v);
		}

	}
}
