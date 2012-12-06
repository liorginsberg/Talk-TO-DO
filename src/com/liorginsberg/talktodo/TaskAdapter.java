package com.liorginsberg.talktodo;

import java.util.ArrayList;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.OnGestureListener;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class TaskAdapter extends ArrayAdapter<Task> {

	private ArrayList<Task> tasks;

	public TaskAdapter(Context context, int textViewResourceId, ArrayList<Task> tasks) {
		super(context, textViewResourceId, tasks);
		this.tasks = tasks;
	}

	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tasklist_item, null);
			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTaskTitle);
			holder.chbDone = (CheckBox) convertView.findViewById(R.id.chbDone);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.tvTitle.setText(tasks.get(position).getTitle());
		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		CheckBox chbDone;
	}

	private final OnTouchListener listItemOnTouchListener = new OnTouchListener() {
		
		public boolean onTouch(View v, MotionEvent event) {
			return false;
		}
	};
	
	private final OnGestureListener listGestureListener = new OnGestureListener() {

		public boolean onDown(MotionEvent e) {
			// TODO Auto-generated method stub
			return false;
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
			// TODO Auto-generated method stub
			return false;
		}

	};

}