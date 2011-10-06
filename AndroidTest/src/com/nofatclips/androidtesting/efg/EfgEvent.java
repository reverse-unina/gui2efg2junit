package com.nofatclips.androidtesting.efg;

import java.util.Iterator;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.model.WidgetAdapter;
import com.nofatclips.androidtesting.model.WrapperInterface;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.NodeListWrapper;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class EfgEvent extends ElementWrapper implements UserEvent, Iterable<EfgEvent> {

	public EfgEvent () {
		super();
	}
	
	public EfgEvent (Element e) {
		super (e);
	}
	
	public EfgEvent (XmlGraph g) {
		super (g, "EVENT");
	}
	
	private EfgEvent (XmlGraph g, UserEvent e) {
		this (g);
		setType(e.getType());
		setValue(e.getValue());
		setWidget(e.getWidget());
	}
	
	public WrapperInterface getWrapper(Element e) {
		return new EfgEvent (e);
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

	public WidgetState getWidget() {
		return new WidgetAdapter () {

			public String getId() {
				return getWidgetId();
			}

			public String getName() {
				return getWidgetName();
			}

			public String getType() {
				return getWidgetType();
			}

			public void setId(String id) {
				setWidgetId(id);
			}

			public void setName(String name) {
				setWidgetName(name);				
			}

			public void setType(String type) {
				setWidgetType(type);
			}
			
		};
	}
	
	public void setWidget (WidgetState w) {
		setWidget (w.getName(),w.getId(),w.getType());
	}
	
	public void setWidget (String name, String id, String type) {
		setWidgetName (name);
		setWidgetId (id);
		setWidgetType (type);
	}

	public String getWidgetName() {
		return getElement().getAttribute("widget_name");
	}

	public void setWidgetName(String n) {
		getElement().setAttribute("widget_name", n);
	}

	public String getWidgetType() {
		return getElement().getAttribute("widget_type");
	}

	public void setWidgetType(String t) {
		getElement().setAttribute("widget_type", t);
	}

	public String getWidgetId() {
		return getElement().getAttribute("widget_id");
	}

	public void setWidgetId(String id) {
		getElement().setAttribute("widget_id", id);
	}

	public Iterator<EfgEvent> iterator() {
		return new NodeListWrapper<EfgEvent>(this, new EfgEvent());
	}
	
	public boolean equals (UserEvent that) {
		return ( (that.getType().equals(this.getType())) && (that.getWidgetId().equals(this.getWidgetId())) && (that.getValue().equals(this.getValue())) ); 
	}
	
	public static EfgEvent fromUserEvent (XmlGraph g, UserEvent e) {
		return new EfgEvent (g,e);
	}

	public void setId (String id) {
		getElement().setAttribute("id",id);
	}
	
	public String getId() {
		return getElement().getAttribute("id");
	}

}
