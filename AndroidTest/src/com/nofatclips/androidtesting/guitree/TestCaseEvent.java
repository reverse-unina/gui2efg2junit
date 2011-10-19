package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class TestCaseEvent extends ElementWrapper implements UserEvent {

	public TestCaseEvent () {
		super();
	}
	
	public TestCaseEvent (Element e) {
		super (e);
	}
	
	public TestCaseEvent (XmlGraph g) {
		super (g, "EVENT");
	}

	public TestCaseEvent (Document d) {
		super (d, "EVENT");
	}

	public WrapperInterface getWrapper(Element e) {
		return new TestCaseEvent (e);
	}

	public String getType() {
		return getElement().getAttribute("type");
	}

	public void setType(String t) {
		getElement().setAttribute("type",t);
	}

	public String getValue() {
		return getElement().getAttribute("value");
	}

	public void setValue(String v) {
		getElement().setAttribute("value",v);
	}

	public void setId (String id) {
		getElement().setAttribute("id",id);
	}
	
	public String getId() {
		return getElement().getAttribute("id");
	}

	public WidgetState getWidget() {
		return new TestCaseWidget ((Element)getElement().getChildNodes().item(0));
	}

	public void setWidget(WidgetState newChild) {
		Element oldChild = getWidget().getElement();
		if (oldChild != null)
			getElement().removeChild(oldChild);
		getElement().appendChild(newChild.getElement());
	}

	public String getWidgetName() {
		return getWidget().getName();
	}

	public void setWidgetName(String n) {
		getWidget().setName(n);
	}
	
	@Override
	public void setDescription (String d) {
		getElement().setAttribute("desc",d);
	}
	
	@Override
	public String getDescription () {
		return getElement().getAttribute("desc");
	}

	public String getWidgetType() {
		return getWidget().getType();
	}

	public void setWidgetType(String t) {
		getWidget().setType(t);
	}

	public String getWidgetId() {
		return getWidget().getId();
	}

	public void setWidgetId(String id) {
		getWidget().setId(id);
	}

	public static TestCaseEvent createEvent(GuiTree theSession) {
		TestCaseEvent event = new TestCaseEvent(theSession);
		return event;
	}

	public static TestCaseEvent createEvent(Document dom) {
		TestCaseEvent event = new TestCaseEvent(dom);
		return event;
	}
	
	public TestCaseEvent clone () {
		TestCaseEvent e = createEvent (this.getElement().getOwnerDocument());
		e.setType(this.getType());
		e.setValue(this.getValue());
		e.setId(this.getId());
		e.setDescription(this.getDescription());
		e.setWidget(this.getWidget().clone());
		return e;
	}

}
