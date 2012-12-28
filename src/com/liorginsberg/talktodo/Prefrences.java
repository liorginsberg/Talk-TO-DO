package com.liorginsberg.talktodo;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.Preference.OnPreferenceChangeListener;
import android.preference.PreferenceActivity;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.SwitchPreference;
import android.widget.Toast;

public class Prefrences extends PreferenceActivity {

	private static SwitchPreference swchFetch;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		getFragmentManager().beginTransaction().replace(android.R.id.content, new MyPreferenceFragment()).commit();
	}

	public static class MyPreferenceFragment extends PreferenceFragment implements OnPreferenceChangeListener {

		private ListPreference lInterval;
		private PreferenceCategory catTaskService;

		@Override
		public void onCreate(final Bundle savedInstanceState) {
			super.onCreate(savedInstanceState);
			addPreferencesFromResource(R.xml.prefs);
			swchFetch = (SwitchPreference) findPreference("fetchTask");
			swchFetch.setOnPreferenceChangeListener(this);

			lInterval = (ListPreference) findPreference("intervalFetchList");
			lInterval.setOnPreferenceChangeListener(this);
			
			catTaskService = (PreferenceCategory) findPreference("taskServiceCategory");

		}

		public boolean onPreferenceChange(Preference preference, Object newValue) {
			
			AlarmManager alarmManager = (AlarmManager) getActivity().getSystemService(ALARM_SERVICE);
			long intervalTime = 0;
			Intent intent = new Intent(getActivity(), TaskService.class);
			PendingIntent pendingIntent = PendingIntent.getService(getActivity(), 12345, intent, 0);
			
			if (preference == swchFetch) {
				if ((Boolean) newValue) {
					catTaskService.addPreference(lInterval);
					intervalTime = Integer.valueOf(lInterval.getValue()) * 1000;
					Toast.makeText(getActivity(), "Service: ON", Toast.LENGTH_SHORT).show();
					alarmManager.setInexactRepeating(AlarmManager.RTC, 0, intervalTime, pendingIntent);
				} else {
					Toast.makeText(getActivity(), "Service: OFF", Toast.LENGTH_SHORT).show();
					catTaskService.removePreference(lInterval);
					alarmManager.cancel(pendingIntent);
				}

			} else if(preference == lInterval) {
				if(swchFetch.isChecked()) {
					intervalTime = Integer.valueOf((String)newValue)* 1000;
					alarmManager.cancel(pendingIntent);
					alarmManager.setInexactRepeating(AlarmManager.RTC, 0, intervalTime, pendingIntent);
				}
			}
			return true;
		}
	}
}
