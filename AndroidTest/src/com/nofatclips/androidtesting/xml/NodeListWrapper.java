package com.nofatclips.androidtesting.xml;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nofatclips.androidtesting.model.WrapperInterface;

public class NodeListWrapper<E extends WrapperInterface> implements Iterator<E> {

	public NodeListWrapper (Element parent, Class<E> classe) {
		this (parent.getChildNodes(), classe);
	}
	
	public NodeListWrapper (WrapperInterface parent, Class<E> classe) {
		this (parent.getElement(), classe);
	}

	public NodeListWrapper (NodeList lista, Class<E> classe) {
		this.iterator = new NodeListIterator (lista);
		this.classe = classe;
		try {
			this.costruisci = this.classe.getConstructor(NodeListWrapper.elemento);
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public NodeListWrapper (Element parent, E wrapper) {
		this (parent.getChildNodes(), wrapper);
	}

	public NodeListWrapper (WrapperInterface parent, E wrapper) {
		this (parent.getElement(), wrapper);
	}

	public NodeListWrapper (NodeList lista, E wrapper) {
		this.iterator = new NodeListIterator (lista);
		this.aWrapper = wrapper;
	}

	// Delegation
	public boolean hasNext() {
		return this.iterator.hasNext();
	}

	public E next() {
		Element unwrapped = this.iterator.next();
		E wrapped = null;
		try {
			wrapped = wrap (unwrapped);
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return wrapped;
	}

	public void remove() {
		this.iterator.remove();
	}
	
	@SuppressWarnings("unchecked")
	private E wrap (Element e) throws IllegalArgumentException, InstantiationException, IllegalAccessException, InvocationTargetException {
		if (aWrapper instanceof WrapperInterface)
			return ((E) aWrapper.getWrapper(e));
		if (classe instanceof Class)
			return this.costruisci.newInstance (e);
		return null;
	}

	private Iterator<Element> iterator;
	static Class<Element> elemento = Element.class;
	private WrapperInterface aWrapper;
	private Class<E> classe;
	Constructor<E> costruisci;

}
