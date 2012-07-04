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
		return getAttribute("id");
	}

	public String getUniqueId() {
		return getAttribute("unique_id");
	}

	public String getName() {
		return getAttribute("name");
	}
	
	public boolean isAvailable() {
		if (!hasAttribute("available")) return true;
		return (getAttribute("available").equals("true"));
	}

	public boolean isClickable() {
		if (!hasAttribute("clickable")) return true;
		return (getAttribute("clickable").equals("true"));
	}

	public boolean isLongClickable() {
		if (!hasAttribute("long_clickable")) return true;
		return (getAttribute("long_clickable").equals("true"));
	}

	public int getCount() {
		if (!hasAttribute("count")) return 1;
		return Integer.parseInt(getAttribute("count"));
	}
	
	public String getValue() {
		return getAttribute("value");
	}

	public String getType() {
		return guessType(); // getElement().getAttribute("type");
	}
	
	public int getIndex() {
		if (!hasAttribute("index")) return 0;
		return Integer.parseInt(getAttribute("index"));
	}
	
	public void setIndex (int index) {
		setAttribute("index", String.valueOf(index));
	}

	public String getTextType() {
		return hasAttribute("text_type")?getAttribute("text_type"):"0";
	}
	
	public String guessType() {
		String s = getAttribute("type");
		return (s.indexOf('@')==-1)?s:s.substring(0, s.indexOf('@'));
	}

	public void setId(String id) {
		setAttribute("id", id);
		
	}

	public void setUniqueId(String id) {
		setAttribute("unique_id", id);
		
	}

	public void setName(String name) {
		setAttribute("name",name);
	}
	
	public void setValue (String value) {
		setAttribute("value", value);
	}
	
	public void setAvailable (String a) {
		setAttribute("available", a);
	}

	public void setClickable (String c) {
		setAttribute("clickable", c);
	}

	public void setLongClickable (String c) {
		setAttribute("long_clickable", c);
	}

	public void setType(String type) {
		setAttribute("type",type);
	}

	public void setSimpleType(String type) {
		setAttribute("simple_type",type);
	}

	public void setTextType(String inputType) {
		setAttribute("text_type",inputType);
	}

	public void setCount(int count) {
		setAttribute("count", String.valueOf(count));
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
		newOne.setUniqueId(this.getUniqueId());
		newOne.setTextType(this.getTextType());
		newOne.setSimpleType(this.getSimpleType());
		newOne.setAvailable(this.getAvailable());
		newOne.setClickable(this.getClickable());
		newOne.setLongClickable(this.getLongClickable());
		newOne.setIndex(this.getIndex());
		newOne.setValue(this.getValue());
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
		return getAttribute("available");
	}

	protected String getClickable() {
		return getAttribute("clickable");
	}

	protected String getLongClickable() {
		return getAttribute("long_clickable");
	}

	public String getSimpleType() {
		return (hasAttribute("simple_type"))?getAttribute("simple_type"):guessSimpleType();
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
