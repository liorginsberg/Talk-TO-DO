package com.liorginsberg.talktodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

public class Main extends Activity {

    private TaskAdapter taskAdapter;
    
    protected static final int ADD_TASK_REQUEST_CODE = 100;
	private ImageButton tbnAddTaskSimple;
	private ImageButton ibCal;
	private ListView lvTasks;

	@Override			
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);
      
        TaskList.getInstance(this).getAllTasksFromDB();
        
        lvTasks = (ListView) findViewById(R.id.lvMainTaskList);

        taskAdapter = new TaskAdapter( this, R.layout.tasklist_item);
       
        lvTasks.setAdapter(taskAdapter);
        
        ibCal = (ImageButton) findViewById(R.id.btnCalender);
        ibCal.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Toast.makeText(Main.this, "Wait for next release!", Toast.LENGTH_SHORT).show();
			}
		});
        tbnAddTaskSimple = (ImageButton) findViewById(R.id.btnAddTaskSimple);
        tbnAddTaskSimple.setOnClickListener(new OnClickListener() {
			
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
