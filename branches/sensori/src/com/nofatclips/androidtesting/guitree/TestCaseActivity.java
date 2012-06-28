package com.nofatclips.androidtesting.guitree;

import java.util.ArrayList;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.SupportedEvent;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.NodeListWrapper;

public class TestCaseActivity extends ElementWrapper implements ActivityState {
	
	public TestCaseActivity() {
		super();
	}

	public TestCaseActivity (Element e) {
		super(e);
	}
	
	public TestCaseActivity getWrapper(Element e) {
		return new TestCaseActivity(e);
	}

	@Override
	public void setElement (Element e) {
		super.setElement(e);
		this.description = (Element) getElement().getChildNodes().item(0);
		
		/** @author nicola amatucci */
		this.supported_events =  (Element) getElement().getChildNodes().item(1);
		/** @author nicola amatucci */
	}
	
	public Iterator<WidgetState> iterator() {
		if (this.description.getNodeName().equals(DESC_TAG)) {
			return new NodeListWrapper<WidgetState> (this.description, new TestCaseWidget());
		}
		return null;
	}

	public String getName() {
		return getAttribute("name");
	}
	
	public void setName(String name) {
		setAttribute("name",name);
	}

	public String getTitle() {
		return getAttribute("title");
	}
	
	public void setTitle(String title) {
		setAttribute("title", title);
	}

	public String getId() {
		return getAttribute("id");
	}

	public void setId(String id) {
		setAttribute("id",id);		
	}

	public String getUniqueId() {
		return getAttribute("unique_id");
	}

	public void setUniqueId(String id) {
		setAttribute("unique_id",id);		
	}

	public String getScreenshot() {
		return getAttribute("screenshot");
	}

	public void setScreenshot(String fileName) {
		setAttribute("screenshot",fileName);		
	}

	public static TestCaseActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		
		Element desc = dom.createElement(DESC_TAG);
		el.appendChild(desc);
		
		/** @author nicola amatucci */
		Element supported_events = dom.createElement(SUPPORTED_EVENTS_TAG);
		el.appendChild(supported_events);
		/** @author nicola amatucci */
		
		return new TestCaseActivity (el);		
	}
	
	public static TestCaseActivity createActivity (Document dom) {
		return createActivity (dom, getTag());
	}

	public static TestCaseActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}
	
	// The main purpose of this method is to create the start activity of a transition from the final activity of the previous one
	public static TestCaseActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		TestCaseActivity newActivity = createActivity (dom);
		newActivity.copyDescriptionFrom(originalActivity);
		
		/** @author nicola amatucci */
		newActivity.copySupportedEventsFrom(originalActivity);
		/** @author nicola amatucci */
		
		return newActivity;
	}

	public void copyDescriptionFrom (ActivityState originalActivity) {
		this.setDescriptionId(originalActivity.getDescriptionId());
		for (WidgetState w: originalActivity) {
			this.addWidget (w.clone());
		}
	}
	
	public void resetDescription () {
		for (WidgetState w: this) {
			this.description.removeChild(w.getElement());
		}
	}
	
	public void setDescriptionId (String id) {
		this.description.setAttribute("id", id);
	}
	
	public String getDescriptionId() {
		if (!this.description.hasAttribute("id")) return "";
		return this.description.getAttribute("id");
	}

	/** @author nicola amatucci */
	public ArrayList<SupportedEvent> getSupportedEvents()
	{
		ArrayList<SupportedEvent> ret = new ArrayList<SupportedEvent>();
		
		if (this.supported_events != null && this.supported_events.getNodeName().equals(SUPPORTED_EVENTS_TAG))
		{			
			NodeListWrapper<SupportedEvent> list = new NodeListWrapper<SupportedEvent> (this.supported_events, new TestCaseSupportedEvent());
			
			while (list.hasNext())
				ret.add(list.next());
		}
		
		return ret;
	}
	
	public ArrayList<SupportedEvent> getSupportedEventsByWidgetUniqueId(String uid) {
		ArrayList<SupportedEvent> ret = new ArrayList<SupportedEvent>();
		
		for ( SupportedEvent se : this.getSupportedEvents() )
			if ( se.getWidgetUniqueId().equals(uid) )
				ret.add(se);
		
		return ret;
	}	
	
	public void copySupportedEventsFrom(ActivityState originalActivity) {
		for (SupportedEvent s: originalActivity.getSupportedEvents()) {
			this.addSupportedEvent(s.clone());
		}
	}
	
	public void resetSupportedEvents()
	{
		if (this.supported_events != null)
		{
			NodeList list = this.supported_events.getChildNodes();
			for (int i = 0; i < list.getLength(); i++)
				this.supported_events.removeChild(list.item(i));
		}
	}
	
	public void addSupportedEvent(SupportedEvent event)
	{
		this.supported_events.appendChild(event.getElement());
	}
	
	public boolean supportsEvent(String uid, String event)
	{
		for ( SupportedEvent se : this.getSupportedEvents() )
			if ( se.getWidgetUniqueId().equals(uid) && se.getEventType().equals(event) )
				return true;
		
		return false;
	}
	
	/*
	@Override
	public void setUsesSensorsManager(boolean yes_no) {
		setAttribute("uses_sensor_manager", (yes_no)?"TRUE":"FALSE" );
		
	}

	@Override
	public boolean getUsesSensorsManager() {
		return getAttribute("uses_sensor_manager").equals("TRUE");
	}

	@Override
	public void setUsesLocationManager(boolean yes_no) {
		setAttribute("uses_location_manager", (yes_no)?"TRUE":"FALSE" );
	}

	@Override
	public boolean getUsesLocationManager() {
		return getAttribute("uses_location_manager").equals("TRUE");
	}
	*/
	/** @author nicola amatucci */
	
	
	public TestCaseActivity clone () {
		return createActivity(this);
	}
	
	public void addWidget(WidgetState w) {
		this.description.appendChild(w.getElement());
	}
	
	public boolean hasWidget(WidgetState widgetToCheck) {
		for (WidgetState stored: this) {
			if (widgetToCheck.equals(stored))
				return true;
		}
		return false;
	}

	public static String getTag () {
		return "ACTIVITY";
	}

	public boolean isExit() {
		return getId().equals(EXIT);
	}

	public boolean isCrash() {
		return getId().equals(CRASH);
	}

	public boolean isFailure() {
		return getId().equals(FAILURE);
	}

	public void markAsExit() {
		setId(EXIT);		
	}

	public void markAsCrash() {
		setId(CRASH);	
	}

	public void markAsFailure() {
		setId(FAILURE);
	}

	private Element description;
	private Element supported_events;
	public final static String DESC_TAG = "DESCRIPTION";
	public final static String SUPPORTED_EVENTS_TAG = "SUPPORTED_EVENTS";

}
