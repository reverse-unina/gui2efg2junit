package com.nofatclips.androidtesting.model;

public interface UserEvent extends WrapperInterface {
	
	public String getType();
	public void setType(String type);
	public String getValue();
	public void setValue(String value);
	public String getId();
	public void setId(String value);
	public WidgetState getWidget();
	public void setWidget(WidgetState w);
	public String getWidgetName();
	public void setWidgetName(String n);
	public String getWidgetType();
	public void setWidgetType(String t);
	public String getWidgetId();
	public void setWidgetId(String id);
	
}
