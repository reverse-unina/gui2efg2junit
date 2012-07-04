package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class TestCaseEvent extends TestCaseInteraction implements UserEvent {

	public TestCaseEvent () {
		super();
	}
	
	public TestCaseEvent (Element e) {
		super (e);
	}
	
	public TestCaseEvent (XmlGraph g) {
		super (g, TAG);
	}

	public TestCaseEvent (Document d) {
		super (d, TAG);
	}

	public WrapperInterface getWrapper(Element e) {
		return new TestCaseEvent (e);
	}

	public String getType() {
		return getAttribute("type");
	}

	public void setType(String t) {
		setAttribute("type",t);
	}

	public boolean hasValue() {
		return hasAttribute("value");
	}
	
	public String getValue() {
		return getAttribute("value");
	}

	public void setValue(String v) {
		setAttribute("value",v);
	}

	public void setId (String id) {
		setAttribute("id",id);
	}
	
	public String getId() {
		return getAttribute("id");
	}

	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	public void setScreenshot(String fileName) {
		setAttribute("screenshot",fileName);		
	}
	
	public void setDescription (String d) {
		setAttribute("desc",d);
	}
	
	public boolean hasDescription () {
		return hasAttribute("desc");
	}
	
	public String getDescription () {
		return getAttribute("desc");
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
		TestCaseEvent that = createEvent (getElement().getOwnerDocument());
		that.setType(this.getType());
		if (this.hasValue()) {
			that.setValue(this.getValue());
		}
		that.setId(this.getId());
		if (this.hasDescription()) {
			that.setDescription(this.getDescription());
		}
		that.setWidget(this.getWidget().clone());
		return that;
	}

	public static String TAG = "EVENT";
	
}
