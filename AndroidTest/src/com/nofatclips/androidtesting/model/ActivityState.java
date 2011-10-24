package com.nofatclips.androidtesting.model;

public interface ActivityState extends WrapperInterface, Iterable<WidgetState> {
	
	public String getName();
	public void setName (String name);
	public String getId();
	public void setId (String id);
	public void addWidget (WidgetState widget);
	public boolean hasWidget (WidgetState widget);
	
}
