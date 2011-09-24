package com.nofatclips.androidtesting.xml;
import java.util.Iterator;

import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class NodeListIterator implements Iterator<Element>, Iterable<Element> {

	public NodeListIterator (Element e) {
		this (e.getChildNodes());
	}
	
	public NodeListIterator(NodeList lista) {
		setItems(lista);
	}

	public Iterator<Element> iterator() {
		return this;
	}
	
	public boolean hasNext() {
		int l = this.getItems().getLength();
		return this.currentItem<l;
	}

	public Element next() {
		Element trace = (Element) this.getItems().item(this.currentItem);
		this.currentItem++;
		return trace;
	}

	public void remove() {
		// Doesn't actually remove anything
		this.currentItem++;
	}
	
	private NodeList getItems() {
		if (this.items instanceof NodeList)
			return this.items;
		return null;
	}
	
	private void setItems (NodeList items) {
		this.items = items;
		this.currentItem = 0;
	}
	
	private NodeList items = null;
	private int currentItem;
	
}
