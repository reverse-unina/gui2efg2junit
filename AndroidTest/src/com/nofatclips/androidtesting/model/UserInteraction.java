package com.nofatclips.androidtesting.model;

public interface UserInteraction extends WrapperInterface {
	
	public String getType();
	public void setType (String type);
	public String getWidgetId();
	public void setWidgetId (String widgetId);
	public String getValue();
	public void setValue (String value);
	public String getId();
	public void setId (String id);
	
}
