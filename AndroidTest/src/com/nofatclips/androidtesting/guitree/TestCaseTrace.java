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
	
	public final static String TAG = "TRACE";
	
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
		super (dom, TAG);
	}
	
	public String getId () {
		return getAttribute("id");
	}

	public void setId (String id) {
		setAttribute("id",id);
	}

	@Override
	public boolean isFailed() {
		if (!hasAttribute("fail")) return false;
		return (getAttribute("fail").equals("true"));
	}

	@Override
	public void setFailed(boolean failure) {
		setAttribute("fail", (failure)?"true":"false");
	}

	public void setFailed(String failure) {
		setAttribute("fail", failure);
	}

	protected String getFailed() {
		return getAttribute("fail");
	}

	@Override
	public TestCaseTrace getWrapper(Element e) {
		return new TestCaseTrace (e);
	}
	
	// Iterator Methods

	@Override
	public Iterator<Transition> transitions () {
		Element t = getElement();
		if (t.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Transition> (t, new TestCaseTransition());
		}
		return null;		
	}
	
	@Override
	public Iterator<Transition> iterator() {
		return transitions();
	}

	@Override
	public void addTransition(Transition tail) {
		appendChild(tail.getElement());
	}
	
	@Override
	public TestCaseTrace clone () {
		TestCaseTrace t = new TestCaseTrace (getElement().getOwnerDocument());
		for (Transition child: this) {
			TestCaseTransition newChild = ((TestCaseTransition)child).clone();
			t.addTransition(newChild);
		}
		t.setFailed(getFailed());
		return t;
	}

	@Override
	public void setFinalActivity(ActivityState theActivity) {
		Transition lastTransition = getFinalTransition();
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theActivity);
		}
	}
	
	@Override
	public Transition getFinalTransition() {
		Transition lastTransition = null;
		for (Transition t: this) {
			lastTransition = t;
		}
		return lastTransition;
	}
		
}
