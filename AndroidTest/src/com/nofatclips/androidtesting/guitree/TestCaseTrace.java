package com.nofatclips.androidtesting.guitree;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.NodeListWrapper;

public class TestCaseTrace extends ElementWrapper implements Trace {
	
	public TestCaseTrace () {
		super();
	}
	
	public TestCaseTrace (Element trace) {
		super(trace);
	}
	
	public TestCaseTrace (GuiTree session) {
		this (session.getDom());
	}
	
	public TestCaseTrace (Document dom) {
		super (dom, "TRACE");
	}
	
	public String getId () {
		return getElement().getAttribute("id");
	}

	public void setId (String id) {
		getElement().setAttribute("id",id);
	}

	public TestCaseTrace getWrapper(Element e) {
		return new TestCaseTrace (e);
	}
	
	// Iterator Methods

	public Iterator<Transition> transitions () {
		Element t = getElement();
		if (t.getNodeName()=="TRACE") {
			return new NodeListWrapper<Transition> (t, new TestCaseTransition());
		}
		return null;		
	}
	
	public Iterator<Transition> iterator() {
		return transitions();
	}

	@Override
	public void addTransition(Transition tail) {
		getElement().appendChild(tail.getElement());
	}
	
	public TestCaseTrace clone () {
		TestCaseTrace t = new TestCaseTrace (this.getElement().getOwnerDocument());
		for (Transition child: this) {
			TestCaseTransition newChild = ((TestCaseTransition)child).clone();
			t.addTransition(newChild);
		}
		return t;
	}

	@Override
	public void setFinalActivity(ActivityState theActivity) {
		Transition lastTransition = null;
		for (Transition t: this) {
			lastTransition = t;
		}
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theActivity);
		}
	}
		
}
