package com.liorginsberg.talktodo;

import java.util.Calendar;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class Prefrences extends PreferenceActivity {

	private static SwitchPreference swchFetch;

	private static String toastText = "";

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {

		private ListPreference lInterval;
	
		private CheckBoxPreference chbServiceNotify;

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.prefs);
			swchFetch = (SwitchPreference) findPreference("fetchTask");
			swchFetch.setOnPreferenceChangeListener(this);

			lInterval = (ListPreference) findPreference("intervalFetchList");
			lInterval.setOnPreferenceChangeListener(this);
			setIntervalSummery(Integer.valueOf(lInterval.getValue()));
			
			chbServiceNotify = (CheckBoxPreference) findPreference("notifyNewTask");
			chbServiceNotify.setOnPreferenceChangeListener(this);

		}

		public boolean onPreferenceChange(Preference preference, Object newValue) {

			AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
			long intervalTime = 0;
			Intent intent = new Intent(getActivity(), TaskService.class);
			PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 12345, intent, 0);

			if (preference == swchFetch) {
				// if to ON
				if ((Boolean) newValue) {
				//	catTaskService.addPreference(lInterval);
					intervalTime = Integer.valueOf(lInterval.getValue()) * 1000;
					long startTime = 0;

					if (lInterval.getValue().equals("86400")) {
						Calendar midnight = Calendar.getInstance();
						midnight.set(Calendar.HOUR_OF_DAY, 0);
						midnight.set(Calendar.MINUTE, 0);
						midnight.set(Calendar.SECOND, 0);
						midnight.add(Calendar.DAY_OF_MONTH, 1);
						startTime = midnight.getTimeInMillis();
						toastText = "Every 24 Hours at 00:00";
					}
					Toast.makeText(getActivity(), "Service ON: " + toastText, Toast.LENGTH_SHORT).show();
					alarmManager.setInexactRepeating(AlarmManager.RTC, startTime, intervalTime, pendingIntent);
				}
				// if OFF
				else {
					Toast.makeText(getActivity(), "Service OFF", Toast.LENGTH_SHORT).show();
				//	catTaskService.removePreference(lInterval);
					alarmManager.cancel(pendingIntent);
				}
			} else if (preference == lInterval) {
				if (swchFetch.isChecked()) {
					long startTime = 0;
					if (newValue.equals("86400")) {
						Calendar midnight = Calendar.getInstance();
						midnight.set(Calendar.HOUR_OF_DAY, 0);
						midnight.set(Calendar.MINUTE, 0);
						midnight.set(Calendar.SECOND, 0);
						midnight.add(Calendar.DAY_OF_MONTH, 1);
						startTime = midnight.getTimeInMillis();
						toastText = "Every 24 Hours at 00:00";
					} else if (newValue.equals("10")) {
						toastText = "Every 10 Seconds starting Now";
					} else if (newValue.equals("60")) {
						toastText = "Every Minute starting Now";
					} else if (newValue.equals("900")) {
						toastText = "Every 15 Minutes starting Now";
					}
					setIntervalSummery(Integer.valueOf((String)newValue));
					intervalTime = Integer.valueOf((String) newValue) * 1000;
					alarmManager.cancel(pendingIntent);
					alarmManager.setInexactRepeating(AlarmManager.RTC, startTime, intervalTime, pendingIntent);
					Toast.makeText(getActivity(), "Service ON: " + toastText, Toast.LENGTH_SHORT).show();
				}
			}
			return true;
		}

		private void setIntervalSummery(int val) {

			switch (val) {
			case 10:
				lInterval.setSummary("Every 10 Seconds");
				break;
			case 60:
				lInterval.setSummary("Every Minute");
				break;
			case 900:
				lInterval.setSummary("Every 15 Minutes");
				break;
			case 86400:
				lInterval.setSummary("Every 24 Hours at 00:00");
				break;
			default:
				break;
			}
		}
	}
}
