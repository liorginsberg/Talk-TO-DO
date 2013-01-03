package com.liorginsberg.talktodo;

public interface Subject {
	public void Register(Observer newObserver);
	public void Unregister(Observer removeObserver);
	public void notifyObserver();
}
