package com.nofatclips.androidtesting.model;
import org.w3c.dom.Element;

public interface WrapperInterface extends Cloneable {

	public void setElement (Element e);
	public Element getElement ();
	public WrapperInterface getWrapper (Element e);
	
}
