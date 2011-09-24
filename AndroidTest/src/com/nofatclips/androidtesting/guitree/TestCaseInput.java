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

	public String getWidgetId() {
		return getElement().getAttribute("input_id");
	}

	public String getValue() {
		return getElement().getAttribute("input_value");
	}
	
	public void setWidget(WidgetState w) {
		setName (w.getType());
		setWidgetId(w.getId());
		setType (w.getSimpleType());
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
	
	public void setValue (String value) {
		getElement().setAttribute("input_value", value);
	}

	public static TestCaseInput createInput(GuiTree theSession) {
		TestCaseInput input = new TestCaseInput(theSession);
		return input;
	}

	public static TestCaseInput createInput(Document theSession) {
		return new TestCaseInput(theSession);
	}

	public TestCaseInput clone () {
		TestCaseInput i = createInput(this.getElement().getOwnerDocument());
		i.setWidgetId(this.getWidgetId());
		return i;
	}

	public static String TAG = "INPUT";
	
}