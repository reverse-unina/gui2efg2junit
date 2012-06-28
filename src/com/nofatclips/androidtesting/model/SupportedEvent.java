package com.nofatclips.androidtesting.model;

public interface SupportedEvent extends WrapperInterface
{
	public static final String NO_UID = "w0";
	public static final String GENERIC_ACTIVITY_UID = "act0";
	
	public String getWidgetUniqueId();
	public void setWidgetUniqueId(String uid);
	public void setEventType(String eventType);
	public String getEventType();
	public SupportedEvent clone();
}
