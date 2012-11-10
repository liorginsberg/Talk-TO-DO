package com.liorginsberg.talktodo;

import java.util.ArrayList;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.TextView;

class TaskAdapter extends ArrayAdapter<Task> {

    private ArrayList<Task> tasks;

    public TaskAdapter(Context context, int textViewResourceId,
	    ArrayList<Task> tasks) {
	super(context, textViewResourceId, tasks);
	this.tasks = tasks;
    }

    public View getView(int position, View convertView, ViewGroup parent) {
	ViewHolder holder;

	if (convertView == null) {
	    LayoutInflater inflater = (LayoutInflater) getContext()
		    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	    convertView = inflater.inflate(R.layout.tasklist_item, null);
	    holder = new ViewHolder();
	    holder.tvTitle = (TextView) convertView
		    .findViewById(R.id.tvTaskTitle);
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
}