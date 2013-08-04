package com.nofatclips.androidtesting.efg;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.Plottable;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class EventFlowTree extends XmlGraph implements Plottable {
	
	public EventFlowTree () throws ParserConfigurationException {
		super ("eventtree.dtd","EFG");
		this.efg = getBuilder().newDocument();
		this.rootElement = this.efg.createElement("EFG");
		this.rootElement.setAttribute("id", "root");
		this.efg.appendChild(this.rootElement);
	}
	
	// Sets attributes for the whole graph (not the nodes!) They are stored as attributes of the root node
	public void setAttribute (String key, String value) {
		this.rootElement.setAttribute(key, value);
	}
	
	public void setDateTime (String value) {
		setAttribute ("session_date_time",value);
	}

	public void setAppName(String appName) {
		setAttribute ("session_app",appName);
	}

	public static EventFlowTree fromSession (Session guiTree) throws ParserConfigurationException {
		EventFlowTree efg = new EventFlowTree();
		efg.setDateTime(guiTree.getDateTime());
		efg.setAppName(guiTree.getAppName());		

		for (Trace t: guiTree) {
			efg.addTrace(t);
		}
		
		return efg;
	}
	
	public void addTrace (Trace trace) {
		resetNavigator();
		for (Transition tct: trace) {
			addEvent(tct.getEvent(),true);		
		}
	}

	// When the traverse flag is true, if an event is already present, the current position is moved after it. No new element is added.
	private boolean addEvent (UserEvent newEvent, boolean traverse) {
		if (traverse) {
			EfgEvent oldEvent = hasEvent(newEvent);
			if (oldEvent==null) {
				return addEvent(newEvent);
			} else {
				setNavigator (oldEvent);
				return false; // Returns false if the event is not added (because it already exists)
			}
		} else {
			return (this.addEvent(newEvent));
		}
	}
	
	// Adds a new child node at the current position
	protected boolean addEvent (UserEvent userEvent) {
		EfgEvent event = EfgEvent.fromUserEvent (this, userEvent);
		event.setId(userEvent.getId());
		getNavigator().appendChild(event.getElement());
		setNavigator (event);
		return true;
	}

	// Checks if the given node is present as a direct child of the current node and returns it. Returns null otherwise.
	public EfgEvent hasEvent (UserEvent event) {
		for (EfgEvent candidate: new EfgEvent (getNavigator())) {
			if (candidate.getId().equals(event.getId())) {
				return candidate;
			}
		}
		return null;
	}
	
	public Document getDom () {
		return this.efg;
	}
	
	public Element getNavigator() {
		return this.navigator;
	}
	
	public void setNavigator (Element e) {
		this.navigator = e;
	}
	
	public void setNavigator (EfgEvent e) {
		this.navigator = e.getElement();
	}

	public void resetNavigator () {
		this.navigator = this.rootElement;
	}

	private Document efg;
	private Element rootElement;
	private Element navigator;
	
}
