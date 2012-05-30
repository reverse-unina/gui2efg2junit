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

	@Override
	public void setId(String id) {}

	@Override
	public void setName(String name) {}

	@Override
	public void setType(String type) {}
	
	@Override
	public WidgetState clone() {
		return null;
	}

	@Override
	public String getSimpleType() {
		return null;
	}

	@Override
	public String getTextType() {
		return null;
	}

	@Override
	public void setTextType(String inputType) {}

	@Override
	public void setSimpleType(String simpleType) {}

	@Override
	public boolean isAvailable() {
		return true;
	}

	@Override
	public void setAvailable(String a) {}

	@Override
	public int getCount() {
		return 1;
	}

	@Override
	public void setCount(int count) {}

	@Override
	public int getIndex() {
		return 0;
	}

	@Override
	public boolean isClickable() {
		return true;
	}

	@Override
	public void setClickable(String c) {}

	@Override
	public String getValue() {
		return "";
	}

	@Override
	public void setValue(String v) {}

	@Override
	public boolean isLongClickable() {
		return false;
	}

	@Override
	public void setLongClickable(String lc) {}
	
}