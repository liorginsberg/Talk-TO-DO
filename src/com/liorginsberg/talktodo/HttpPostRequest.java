package com.liorginsberg.talktodo;

import java.util.ArrayList;

import android.util.Log;

import com.turbomanage.httpclient.AsyncCallback;
import com.turbomanage.httpclient.HttpResponse;
import com.turbomanage.httpclient.ParameterMap;
import com.turbomanage.httpclient.android.AndroidHttpClient;

public class HttpPostRequest implements Subject {

	private ArrayList<Observer> observers;
	private String response;
	private AndroidHttpClient httpClient;
	private ParameterMap params;

	public HttpPostRequest() {
		observers = new ArrayList<Observer>();
		httpClient = new AndroidHttpClient("http://liorginsberg.com/android/tasklist/task_backup_service.php");
		httpClient.setMaxRetries(5);
	}

	public void getTaskFromRemoteDB(String user_id) {
		params = httpClient.newParams();
		params.add("method", "getAllTasks").add("user_id", user_id);
		httpClient.post("", params, new AsyncCallback() {

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete(HttpResponse httpResponse) {
				response = httpResponse.getBodyAsString();
				notifyObserver();
			}
		});
	}

	public void add(String user_id, int task_id, String title, String desc, String from, String to, String location, int checked) {
		params = httpClient.newParams().add("method", "addTask").add("user_id", user_id).add("task_id", String.valueOf(task_id))
				.add("task_title", title).add("task_desc", desc).add("task_from", from).add("task_to", to).add("task_location", location)
				.add("task_checked", String.valueOf(checked));
		httpClient.post("", params, new AsyncCallback() {

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete(final HttpResponse httpResponse) {
				response = httpResponse.getBodyAsString();
			}
		});
	}

	public void remove(String user_id, int task_id) {
		params = httpClient.newParams().add("method", "removeTask").add("user_id", user_id).add("task_id", String.valueOf(task_id));
		httpClient.post("", params, new AsyncCallback() {

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete(final HttpResponse httpResponse) {
				response = httpResponse.getBodyAsString();
			}
		});
	}

	public void setChecked(String user_id, int task_id, int task_checked) {
		params = httpClient.newParams().add("method", "updateCheck").add("user_id", user_id).add("task_id", String.valueOf(task_id))
				.add("task_check", String.valueOf(task_checked));
		httpClient.post("", params, new AsyncCallback() {

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete(final HttpResponse httpResponse) {
				response = httpResponse.getBodyAsString();
			}
		});
	}

	public void updateID(int row_id, String user_id, int newTaskID) {
		params = httpClient.newParams().
		add("method", "updateID").
		add("row_id", String.valueOf(row_id)).
		add("user_id", user_id).
		add("task_id", String.valueOf(newTaskID));
		httpClient.post("", params, new AsyncCallback() {

			@Override
			public void onError(Exception e) {
				e.printStackTrace();
			}

			@Override
			public void onComplete(final HttpResponse httpResponse) {
				response = httpResponse.getBodyAsString();
			}
		});

	}

	public void Register(Observer newObserver) {
		observers.add(newObserver);

	}

	public void Unregister(Observer removeObserver) {
		observers.remove(removeObserver);

	}

	public void notifyObserver() {
		for (Observer observer : observers) {
			observer.update(response);
		}

	}

}
