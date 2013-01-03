package com.liorginsberg.talktodo;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Calendar;
import java.util.Locale;

import org.json.JSONException;
import org.json.JSONObject;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.preference.PreferenceManager;

public class TaskService extends IntentService {

	public static final int TASK_SERVICE_NOTIFICATION_ID = 11211121;

	public TaskService() {
		super("TaskService");
	}

	@Override
	protected void onHandleIntent(Intent intent) {
		URL url;
		try {
			url = new URL("http://mobile1-tasks-dispatcher.herokuapp.com/task/random");
			new addTaskFromWeb().execute(url);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		}
	}

	private class addTaskFromWeb extends AsyncTask<URL, Integer, String> {

		@Override
		protected String doInBackground(URL... urls) {
			String res = "";
			try {
				res = getFromWeb(urls[0]);
			} catch (Exception e) {
				e.printStackTrace();
			}
			return res;
		}

		private String getFromWeb(URL url) throws Exception {
			HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
			String response;
			InputStream in = new BufferedInputStream(urlConnection.getInputStream());
			InputStreamReader inReader = new InputStreamReader(in);
			BufferedReader bufferedReader = new BufferedReader(inReader);
			StringBuilder responseBuilder = new StringBuilder();
			for (String line = bufferedReader.readLine(); line != null; line = bufferedReader.readLine()) {
				responseBuilder.append(line);
			}
			response = responseBuilder.toString();

			return response;
		}

		@Override
		protected void onPostExecute(String result) {
			Calendar def = Calendar.getInstance();

			String from = String.format(Locale.getDefault(), "%02d/%02d/%02d %02d:%02d", def.get(Calendar.DAY_OF_MONTH), def.get(Calendar.MONTH) + 1,
					def.get(Calendar.YEAR), def.get(Calendar.HOUR_OF_DAY), def.get(Calendar.MINUTE));

			def.add(Calendar.HOUR_OF_DAY, 1);

			String to = String.format(Locale.getDefault(), "%02d/%02d/%02d %02d:%02d", def.get(Calendar.DAY_OF_MONTH), def.get(Calendar.MONTH) + 1,
					def.get(Calendar.YEAR), def.get(Calendar.HOUR_OF_DAY), def.get(Calendar.MINUTE));

			JSONObject taskJASON = null;
			String topic = "";
			try {
				taskJASON = new JSONObject(result);
				topic = taskJASON.getString("topic");
				TaskList.getInstance(getApplicationContext()).addTask(topic, taskJASON.getString("description"), from, to, "hashlosha 4, tel-aviv", 0, true);
				TaskAdapter.getInstance(getApplicationContext(), R.layout.tasklist_item).notifyDataSetChanged();
			} catch (JSONException e) {
				e.printStackTrace();
			}

			Context context = getApplicationContext();
			Intent myIntent = new Intent(context, Main.class);
			PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, myIntent, 0);

			SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
			
			if (prefs.getBoolean("notifyNewTask", true)) {

				NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);

				Notification notification = new Notification.Builder(context).setContentTitle("New Task").setContentText(topic)
						.setSmallIcon(R.drawable.no1).setContentIntent(pendingIntent).setTicker("New Task").build();
				notification.flags |= Notification.FLAG_AUTO_CANCEL;
				notification.defaults |= Notification.DEFAULT_SOUND;
				notificationManager.notify(TASK_SERVICE_NOTIFICATION_ID, notification);
			}
		}
	}
}
