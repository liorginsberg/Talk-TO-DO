package com.liorginsberg.talktodo;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.RelativeLayout;

public class Main extends Activity {

	private TaskAdapter taskAdapter;

	protected static final int ADD_TASK_REQUEST_CODE = 100;
	private ImageButton btnAddTaskSimple;
	private ImageButton ibCal;
	private ImageButton ibSet;
	private ListView lvTasks;
	private RelativeLayout tutOverlay;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_main);
		
		tutOverlay = (RelativeLayout) findViewById(R.id.tutOverlay);
		tutOverlay.setVisibility(RelativeLayout.GONE);

		
		TaskList.getInstance(this).getAllTasksFromDB();

		lvTasks = (ListView) findViewById(R.id.lvMainTaskList);

		taskAdapter = TaskAdapter.getInstance(this, R.layout.tasklist_item);

		View v = getLayoutInflater().inflate(R.layout.list_footer, null);
		lvTasks.addFooterView(v);

		lvTasks.setAdapter(taskAdapter);
		ibSet = (ImageButton) findViewById(R.id.btnSet);
		ibSet.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent p = new Intent(Main.this, Prefrences.class);
				startActivity(p);
			}
		});
		ibCal = (ImageButton) findViewById(R.id.btnCalender);
		ibCal.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent calIntent = new Intent(Main.this, Calander.class);
				startActivity(calIntent);
			}
		});
		btnAddTaskSimple = (ImageButton) findViewById(R.id.btnAddTaskSimple);
		btnAddTaskSimple.setOnClickListener(new OnClickListener() {

			public void onClick(View v) {
				Intent addTaskIntent = new Intent();
				addTaskIntent.setClass(Main.this, AddTaskActivity.class);
				startActivityForResult(addTaskIntent, ADD_TASK_REQUEST_CODE);
			}
		});

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
			taskAdapter.notifyDataSetChanged();
		}
	}
}
