package com.nofatclips.androidtesting.xml;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import com.nofatclips.androidtesting.model.WrapperInterface;

public abstract class ElementWrapper implements WrapperInterface {
	
	public ElementWrapper() {
		super();
	}
	
	public ElementWrapper (Element e) {
		super();
		setElement(e);
	}
	
	public ElementWrapper (XmlGraph g, String tag) {
		this (g.getDom(), tag);
	}

	public ElementWrapper (Document d, String tag) {
		this (d.createElement(tag));
	}
	
	public void setElement(Element e) {
		this.element = e;
	}

	public Element getElement() {
		return this.element;
	}
	
	public void appendChild (Element child) {
		getElement().appendChild(child);
	}

	public void appendChild (ElementWrapper child) {
		getElement().appendChild(child.getElement());
	}

	Element element;

}
