package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class TestCaseInput extends ElementWrapper implements UserInput {

	public TestCaseInput () {
		super();
	}
	
	public TestCaseInput (Element e) {
		super (e);
	}
	
	public TestCaseInput (XmlGraph g) {
		super (g, TAG);
	}
	
	public TestCaseInput (Document d) {
		super (d, TAG);
	}
	
	public WrapperInterface getWrapper(Element e) {
		return new TestCaseInput (e);
	}

	public String getType() {
		return getElement().getAttribute("input_type");
	}

	public String getName() {
		return getElement().getAttribute("input_name");
	}

	public String getWidgetType() {
		return getElement().getAttribute("widget_type");
	}

	public String getWidgetId() {
		return getElement().getAttribute("input_id");
	}

	public String getValue() {
		return getElement().getAttribute("input_value");
	}
	
	public boolean hasValue() {
		return hasAttribute("input_value");
	}
	
	public void setWidget(WidgetState w) {
		setName (w.getType());
		setWidgetId(w.getId());
		setWidgetType (w.getSimpleType());
	}
	
	public void setWidgetId (String id) {
		getElement().setAttribute("input_id", id);		
	}
	
	public void setName (String name) {
		getElement().setAttribute("input_name", name);
	}
	
	public void setType (String type) {
		getElement().setAttribute("input_type", type);
	}

	public void setWidgetType (String type) {
		getElement().setAttribute("widget_type", type);
	}

	public void setValue (String value) {
		getElement().setAttribute("input_value", value);
	}
	
	public void setId (String id) {
		getElement().setAttribute("id",id);
	}
	
	public String getId() {
		return getElement().getAttribute("id");
	}

	public static TestCaseInput createInput(GuiTree theSession) {
		TestCaseInput input = new TestCaseInput(theSession);
		return input;
	}

	public static TestCaseInput createInput(Document theSession) {
		return new TestCaseInput(theSession);
	}
	
	public TestCaseInput clone () {
		TestCaseInput i = createInput(getElement().getOwnerDocument());
		i.setName(this.getName());
		i.setWidgetId(this.getWidgetId());
		i.setWidgetType(this.getWidgetType());				
		if (hasValue()) i.setValue(this.getValue());
		i.setType(this.getType());
		i.setId (this.getId());
		return i;
	}

	public static String TAG = "INPUT";
	
}