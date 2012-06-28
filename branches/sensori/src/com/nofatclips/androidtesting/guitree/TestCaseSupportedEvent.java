package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.SupportedEvent;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.ElementWrapper;

public class TestCaseSupportedEvent extends ElementWrapper implements SupportedEvent
{
	public TestCaseSupportedEvent () {
		super();
	}
	
	public TestCaseSupportedEvent (Element e) {
		super(e);
	}
	
	@Override
	public WrapperInterface getWrapper(Element e) {
		return new TestCaseSupportedEvent(e);
	}	
	
	@Override
	public String getWidgetUniqueId() {
		return getAttribute("widget_uid");
	}

	@Override
	public void setWidgetUniqueId(String uid) {
		setAttribute("widget_uid", uid);
	}

	@Override
	public void setEventType(String eventType) {
		setAttribute("event_type", eventType);
	}

	@Override
	public String getEventType() {
		return getAttribute("event_type");
	}

	public static TestCaseSupportedEvent createSupportedEvent (Document dom) {
		Element el = dom.createElement(TAG);
		return new TestCaseSupportedEvent (el);		
	}

	public static TestCaseSupportedEvent createSupportedEvent (GuiTree session) {
		return createSupportedEvent (session.getDom());
	}	
	
	@Override
	public TestCaseSupportedEvent clone() {
		Element el = this.getElement().getOwnerDocument().createElement(TAG);
		TestCaseSupportedEvent newOne = new TestCaseSupportedEvent(el);
		newOne.setWidgetUniqueId(this.getWidgetUniqueId());
		newOne.setEventType(this.getEventType());
		return newOne;
	}
	
	@Override
	public boolean equals(Object o) {
		if (!(o instanceof SupportedEvent)) return false;
		SupportedEvent that = (SupportedEvent)o;
		return (
				this.getWidgetUniqueId().equals(that.getWidgetUniqueId()) &&
				this.getEventType().equals(that.getEventType())
		);
	}
	
	public final static String TAG = "SUPPORTED_EVENT";
}
