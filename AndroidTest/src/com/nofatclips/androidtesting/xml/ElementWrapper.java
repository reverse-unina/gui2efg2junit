package com.nofatclips.androidtesting.xml;

import java.io.StringWriter;

import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.TransformerFactoryConfigurationError;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

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
	
	public void setAttribute(String name, String value) {
		getElement().setAttribute(name, value);
	}
	
	public String getAttribute (String name) {
		return getElement().getAttribute(name);
	}

	public boolean hasAttribute (String name) {
		return getElement().hasAttribute(name);
	}

	public void appendChild (Element child) {
		getElement().appendChild(child);
	}

	public void appendChild (ElementWrapper child) {
		getElement().appendChild(child.getElement());
	}

	public String toXml () throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(getElement());
		StringWriter autput = new StringWriter();
		getTransformer().transform(theDom, new StreamResult(autput));
		return autput.toString();
	}
	
	protected Transformer getTransformer() throws TransformerConfigurationException, TransformerFactoryConfigurationError {
		if (t instanceof Transformer) return t;
		t = TransformerFactory.newInstance().newTransformer();
		return t;
	}
	
	protected Element element;
	protected static Transformer t;
	
}
