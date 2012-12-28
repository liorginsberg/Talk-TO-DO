package com.liorginsberg.talktodo;

import android.content.Context;
import android.util.Log;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

public class TaskAdapter extends ArrayAdapter<Task> {

	private static TaskAdapter instance = null;
	
	private TaskList taskList;
	
	private enum TaskPopupMenu {REMOVE, EDIT, OPEN};

	Context context;

	protected GestureDetector gt;

	private TaskAdapter(Context context, int textViewResourceId) {
		super(context, textViewResourceId, TaskList.getInstance(context).getTasks());
		this.context = context;
		this.taskList = TaskList.getInstance(context);

	}
	
	public static TaskAdapter getInstance(Context context, int textViewResourceId) {
		if(instance == null) {
			instance = new TaskAdapter(context, textViewResourceId);
		}
		return instance;
	}

	public View getView(final int position, View convertView, ViewGroup parent) {
		final ViewHolder holder;
		final Task t = TaskList.getInstance(context).getTasks().get(position);
		if (convertView == null) {
			LayoutInflater inflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			convertView = inflater.inflate(R.layout.tasklist_item, null);

			holder = new ViewHolder();
			holder.tvTitle = (TextView) convertView.findViewById(R.id.tvTaskTitle);
			holder.chbDone = (CheckBox) convertView.findViewById(R.id.chbDone);
			holder.tvDate = (TextView) convertView.findViewById(R.id.tvTaskDate);

			convertView.setTag(holder);

		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		holder.chbDone.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				if (isChecked) {
					for (Task task : taskList.getTasks()) {
						task.setChecked(0);
					}
				}
				t.setChecked(1);
				notifyDataSetChanged();

			}
		});

		holder.tvTitle.setText(t.getTitle());
		holder.tvDate.setText(t.getFromTo());

		convertView.setOnLongClickListener(new OnItemLongClickListener(position));
		int isChecked = t.isChecked();
		
		holder.chbDone.setChecked(isChecked == 0 ? false : true);
		holder.chbDone.setTag(position);
		return convertView;
	}

	static class ViewHolder {
		TextView tvTitle;
		CheckBox chbDone;
		TextView tvDate;
	}

	class OnItemLongClickListener implements OnLongClickListener {

		private int position;

		public OnItemLongClickListener(int position) {
			this.position = position;
		}

		public boolean onLongClick(View v) {
			Log.i("LONG", "pressed " + position);
			showPopupMenu(v, position);
			return false;
		}

		private void showPopupMenu(View v, final int position){
			
			PopupMenu popupMenu = new PopupMenu(context, v);
			popupMenu.getMenuInflater().inflate(R.menu.item_popup_menu, popupMenu.getMenu());
			  
			popupMenu.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {

				public boolean onMenuItemClick(MenuItem item) {
					TaskPopupMenu itemClicked = TaskPopupMenu.valueOf(item.getTitle().toString().toUpperCase()); 
					switch (itemClicked) {
					case REMOVE:
						if(TaskList.getInstance(context).removeTask(position) == 1) {
							notifyDataSetChanged();
						} else {
							Toast.makeText(context, "Could not remove Task from list", Toast.LENGTH_LONG).show();
						}
						break;
					case EDIT:
						Toast.makeText(context, "Edit Not Supported yet, wait for next version!", Toast.LENGTH_LONG).show();
					case OPEN:
						Toast.makeText(context, "Open Not Supported yet, wait for next version!", Toast.LENGTH_LONG).show();
					default:
						break;
					}
					
					return true;
				}
			});

			popupMenu.show();
		} 
	}
}
