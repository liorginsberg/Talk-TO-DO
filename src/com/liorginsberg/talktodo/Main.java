package com.liorginsberg.talktodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ListView;

public class Main extends Activity {

    private TaskAdapter taskAdapter;
    
    protected static final int ADD_TASK_REQUEST_CODE = 100;
	private Button tbnAddTaskSimple;
	private ListView lvTasks;

	@Override			
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
      
        TaskList.getInstance(this).getAllTasksFromDB();
        
        lvTasks = (ListView) findViewById(R.id.lvMainTaskList);

        taskAdapter = new TaskAdapter( this, R.layout.tasklist_item);
       
        lvTasks.setAdapter(taskAdapter);
        
        tbnAddTaskSimple = (Button) findViewById(R.id.btnAddTaskSimple);
        tbnAddTaskSimple.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				showAddDialog();
			}
		});
        
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (requestCode == ADD_TASK_REQUEST_CODE && resultCode == RESULT_OK) {
			taskAdapter.notifyDataSetChanged();
		}
	}
	
	private void showAddDialog() {
		Intent addTaskIntent = new Intent();
		addTaskIntent.setClass(this, AddTaskDialog.class);
		startActivityForResult(addTaskIntent, ADD_TASK_REQUEST_CODE);

	}
	
	
}
