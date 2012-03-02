package com.nofatclips.androidtesting.model;

public interface ActivityState extends WrapperInterface, Iterable<WidgetState> {
	
	public String getName();
	public void setName (String name);
	public String getTitle();
	public void setTitle(String title);
	public String getId();
	public void setId (String id);
	public String getUniqueId();
	public void setUniqueId (String id);
	public String getScreenshot();
	public void setScreenshot (String screenshot);
	public void addWidget (WidgetState widget);
	public boolean hasWidget (WidgetState widget);
	
}
