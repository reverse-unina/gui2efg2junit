package com.nofatclips.androidtesting.model;

public interface UserEvent extends UserInteraction {
	
//	public String getId();
//	public void setId(String value);
	public WidgetState getWidget();
	public void setWidget(WidgetState w);
	public String getWidgetName();
	public void setWidgetName(String n);
	public String getWidgetType();
	public void setWidgetType(String t);
	public String getDescription ();
	public void setDescription (String d);
	
}
