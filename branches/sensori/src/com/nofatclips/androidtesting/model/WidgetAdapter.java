package com.nofatclips.androidtesting.model;

import org.w3c.dom.Element;

public class WidgetAdapter implements WidgetState {

	@Override
	public void setElement(Element e) {
	}

	@Override
	public Element getElement() {
		return null;
	}

	@Override
	public WrapperInterface getWrapper(Element e) {
		return null;
	}

	@Override
	public String getId() {
		return "";
	}

	@Override
	public String getName() {
		return "";
	}

	@Override
	public String getType() {
		return "";
	}

	public String getUniqueId() {
		return "";
	}

	public void setId(String id) {}
	public void setName(String name) {}
	public void setType(String type) {}

	public WidgetState clone() {
		return null;
	}

	public String getSimpleType() {
		return null;
	}

	public String getTextType() {
		return null;
	}

	public void setTextType(String inputType) {}
	public void setSimpleType(String simpleType) {}

	public boolean isAvailable() {
		return true;
	}

	public void setAvailable(String a) {}

	public int getCount() {
		return 1;
	}

	public void setCount(int count) {}

	public int getIndex() {
		return 0;
	}

	public boolean isClickable() {
		return true;
	}

	public void setClickable(String c) {}

	public String getValue() {
		return "";
	}

	public void setValue(String v) {}

	public boolean isLongClickable() {
		return false;
	}

	public void setLongClickable(String lc) {}
	public void setUniqueId(String id) {}
	
}