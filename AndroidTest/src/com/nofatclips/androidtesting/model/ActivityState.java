package com.nofatclips.androidtesting.model;

public interface ActivityState extends WrapperInterface, Iterable<WidgetState> {
	
	public String getName();
	public void setName (String name);
	public void addWidget (WidgetState widget);
	
}
