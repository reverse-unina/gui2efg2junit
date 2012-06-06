package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class TestCaseInput extends TestCaseInteraction implements UserInput {

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
//		return getElement().getAttribute("input_type");
		return getAttribute("type");
	}

	public String getName() {
//		return getElement().getAttribute("input_name");
		return getWidget().getName();
	}

//	public String getWidgetType() {
////		return getElement().getAttribute("widget_type");
//		return getWidget().getType();
//	}
//
//	public String getWidgetId() {
////		return getElement().getAttribute("input_id");
//		return getWidget().getId();
//	}

	public String getValue() {
//		return getElement().getAttribute("input_value");
		return getAttribute("value");
	}
	
	public boolean hasValue() {
		return hasAttribute("value");
	}
	
//	public void setWidget(WidgetState w) {
//		setName (w.getType());
//		setWidgetId(w.getId());
//		setWidgetType (w.getSimpleType());
//	}
	
//	public void setWidgetId (String id) {
////		getElement().setAttribute("input_id", id);
//		getWidget().setId(id);
//	}
	
//	public void setWidgetName (String name) {
////		getElement().setAttribute("input_name", name);
////		getWidget().setName(name);
//		super.setWidgetName (name);
//	}
	
	public void setType (String type) {
//		getElement().setAttribute("input_type", type);
		setAttribute("type",type);
	}

//	public void setWidgetType (String type) {
////		getElement().setAttribute("widget_type", type);
//		getWidget().setType(type);
//	}

	public void setValue (String value) {
//		getElement().setAttribute("input_value", value);
		setAttribute("value", value);
	}
	
	public void setId (String id) {
		setAttribute("id",id);
	}
	
	public String getId() {
		return getAttribute("id");
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
//		TestCaseEvent e = createEvent (this.getElement().getOwnerDocument());
		i.setType(this.getType());
		if (this.hasValue()) {
			i.setValue(this.getValue());
		}
		i.setId(this.getId());
//		if (this.hasDescription()) {
//			e.setDescription(this.getDescription());
//		}
		i.setWidget(this.getWidget().clone());
		return i;
	}

//	public TestCaseInput clone () {
//		TestCaseInput i = createInput(getElement().getOwnerDocument());
//		i.setName(this.getName());
//		i.setWidgetId(this.getWidgetId());
//		i.setWidgetType(this.getWidgetType());				
//		if (hasValue()) i.setValue(this.getValue());
//		i.setType(this.getType());
//		i.setId (this.getId());
//		return i;
//	}
	
//	public WidgetState getWidget() {
//		return new TestCaseWidget ((Element)getElement().getChildNodes().item(0));
//	}
//
//	public void setWidget(WidgetState newChild) {
//		Element oldChild = getWidget().getElement();
//		if (oldChild != null)
//			getElement().removeChild(oldChild);
//		getElement().appendChild(newChild.getElement());
//	}

	public static String TAG = "INPUT";

}