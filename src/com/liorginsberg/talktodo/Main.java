package com.liorginsberg.talktodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.Toast;

public class Main extends Activity {

    protected static final int ADD_TASK_SIMPLE = 100;
	private Button tbnAddTaskSimple;

	@Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        
        tbnAddTaskSimple = (Button) findViewById(R.id.btnAddTaskSimple);
        tbnAddTaskSimple.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				Intent intent = new Intent(Main.this, AddTaskSimple.class);
				startActivityForResult(intent, ADD_TASK_SIMPLE);
			}
		});
        
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == RESULT_OK && requestCode == ADD_TASK_SIMPLE) {
			Toast.makeText(this, "Task Added: "+ data.getStringExtra("newTask"), Toast.LENGTH_LONG).show();
		}
	}
	
	
}
