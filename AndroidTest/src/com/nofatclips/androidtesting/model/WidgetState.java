package com.nofatclips.androidtesting.model;

public interface WidgetState extends WrapperInterface {

	public String getId();
	public void setId(String id);
	public String getName();
	public void setName(String name);
	public String getType();
	public void setType(String type);
	public String getTextType();
	public void setTextType (String inputType);
	public WidgetState clone();
	public String getSimpleType();
	public void setSimpleType(String simpleType);
	public int getCount();
	public void setCount(int count);
	public boolean isAvailable();
	public void setAvailable (String a);
	public boolean isClickable();
	public void setClickable (String c);
	public int getIndex();
	
}