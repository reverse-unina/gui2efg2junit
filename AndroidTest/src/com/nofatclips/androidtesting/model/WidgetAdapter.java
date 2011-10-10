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
	
}
