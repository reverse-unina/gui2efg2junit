package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.ElementWrapper;

public class TestCaseWidget extends ElementWrapper implements WidgetState {

	public TestCaseWidget () {
		super();
	}
	
	public TestCaseWidget (Element e) {
		super(e);
	}
	
	public WrapperInterface getWrapper(Element e) {
		return new TestCaseWidget (e);
	}

	public String getId() {
		return getElement().getAttribute("id");
	}

	public String getName() {
		return getElement().getAttribute("name");
	}

	public String getType() {
		return guessType(); // getElement().getAttribute("type");
	}

	@Override
	public String getTextType() {
		return getElement().getAttribute("text_type");
	}
	
	public String guessType() {
		String s = getElement().getAttribute("type");
		return (s.indexOf('@')==-1)?s:s.substring(0, s.indexOf('@'));
	}

	public void setId(String id) {
		getElement().setAttribute("id", id);
		
	}

	public void setName(String name) {
		getElement().setAttribute("name",name);
	}

	public void setType(String type) {
		getElement().setAttribute("type",type);
	}

	@Override
	public void setTextType(String inputType) {
		getElement().setAttribute("text_type",inputType);
	}

	public void setIdNameType (String id, String name, String type) {
		setId (id);
		setName (name);
		setType (type);
	}
	
	public static TestCaseWidget createWidget (Document dom) {
		Element el = dom.createElement(TAG);
		return new TestCaseWidget (el);		
	}

	public static TestCaseWidget createWidget (GuiTree session) {
		return createWidget (session.getDom());
	}
	
	public TestCaseWidget clone() {
		Element el = this.getElement().getOwnerDocument().createElement(TAG);
		TestCaseWidget newOne = new TestCaseWidget(el);
		newOne.setIdNameType(this.getId(), this.getName(), this.getType());
		newOne.setTextType(this.getTextType());
		return newOne;
	}

	public String getSimpleType() {
		if (getType().endsWith("null"))
			return "null";
		if (getType().endsWith("RadioButton"))
			return "radio";
		if (getType().endsWith("CheckBox"))
			return "check";
		if (getType().endsWith("Button"))
			return "button";
		if (getType().endsWith("EditText"))
			return "editText";
		if (getType().endsWith("TabHost"))
			return "tabHost";
		return "";
	}

	public final static String TAG = "WIDGET";

}
