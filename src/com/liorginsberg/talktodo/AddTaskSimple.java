package com.liorginsberg.talktodo;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class AddTaskSimple extends Activity {

	private EditText etAddTaskSimple;
	private Button btnReturnTask;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task_simple);
        
        etAddTaskSimple = (EditText)findViewById(R.id.edAddTaskSimple);
        etAddTaskSimple.setText("");
        btnReturnTask = (Button)findViewById(R.id.btnReturnTask);
        btnReturnTask.setOnClickListener(new OnClickListener() {
			
			public void onClick(View v) {
				String task = etAddTaskSimple.getText().toString();
				if(task.equals("")) {
					Toast.makeText(AddTaskSimple.this, "Cant add Empty task ", Toast.LENGTH_SHORT).show();
					return;
				}
				Intent intent = new Intent(AddTaskSimple.this, Main.class);
				intent.putExtra("newTask", task);
				setResult(RESULT_OK,intent);
				finish();
				
			}
		});
        
    }

}
