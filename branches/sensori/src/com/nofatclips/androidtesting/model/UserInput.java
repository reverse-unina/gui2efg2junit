package com.nofatclips.androidtesting.model;

public interface UserInput extends UserInteraction {

//	public String getId();
//	public void setId(String value);
	public WidgetState getWidget();
	public void setWidget(WidgetState w);
	public String getWidgetName();
	public void setWidgetName(String n);
	public String getWidgetType();
	public void setWidgetType(String t);

}
