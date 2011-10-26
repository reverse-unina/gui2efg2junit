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
	
	@Override
	public boolean isAvailable() {
		if (!getElement().hasAttribute("available")) return true;
		return (getElement().getAttribute("available").equals("true"));
	}

	@Override
	public boolean isClickable() {
		if (!getElement().hasAttribute("clickable")) return true;
		return (getElement().getAttribute("clickable").equals("true"));
	}

	@Override
	public int getCount() {
		if (!getElement().hasAttribute("count")) return 1;
		return Integer.parseInt(getElement().getAttribute("count"));
	}

	public String getType() {
		return guessType(); // getElement().getAttribute("type");
	}
	
	public int getIndex() {
		if (!getElement().hasAttribute("index")) return 0;
		return Integer.parseInt(getElement().getAttribute("index"));
	}
	
	public void setIndex (int index) {
		getElement().setAttribute("index", String.valueOf(index));
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
	
	@Override
	public void setAvailable (String a) {
		getElement().setAttribute("available", a);
	}

	@Override
	public void setClickable (String c) {
		getElement().setAttribute("clickable", c);
	}

	public void setType(String type) {
		getElement().setAttribute("type",type);
	}

	public void setSimpleType(String type) {
		getElement().setAttribute("simple_type",type);
	}

	@Override
	public void setTextType(String inputType) {
		getElement().setAttribute("text_type",inputType);
	}

	@Override
	public void setCount(int count) {
		getElement().setAttribute("count", String.valueOf(count));
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
		newOne.setSimpleType(this.getSimpleType());
		newOne.setAvailable(this.getAvailable());
		newOne.setClickable(this.getClickable());
		newOne.setIndex(this.getIndex());
		if (this.getCount() != 1) {
			newOne.setCount(this.getCount());
		}
		return newOne;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof WidgetState)) return false;
		WidgetState that = (WidgetState)o;
		return ( (this.getId()==that.getId()) && (this.getName()==that.getName()) && (this.getType()==that.getType()) && 
				(this.getTextType()==that.getTextType()) && (this.getCount()==that.getCount()) );
	}
	
	protected String getAvailable () {
		return getElement().getAttribute("available");
	}

	protected String getClickable() {
		return getElement().getAttribute("clickable");
	}

	public String getSimpleType() {
		Boolean flag = getElement().hasAttribute("simple_type");
		return (flag)?getElement().getAttribute("simple_type"):guessSimpleType();
	}

	public String guessSimpleType() {
		String type = getType(); 
		if (type.endsWith("null"))
			return "null";
		if (type.endsWith("RadioButton"))
			return "radio";
		if (type.endsWith("CheckBox"))
			return "check";
		if (type.endsWith("Button"))
			return "button";
		if (type.endsWith("EditText"))
			return "editText";
		if (type.endsWith("TabHost"))
			return "tabHost";
		return "";
	}

	public final static String TAG = "WIDGET";

}