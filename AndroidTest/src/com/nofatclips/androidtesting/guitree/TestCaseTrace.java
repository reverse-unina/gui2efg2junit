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

	public boolean isFailed() {
		if (!hasAttribute("fail")) return false;
		return (getAttribute("fail").equals("true"));
	}

	public boolean isAsync() {
		if (!hasAttribute("async")) return false;
		return (getAttribute("async").equals("true"));
	}

	public void setFailed(boolean failure) {
		setAttribute("fail", (failure)?"true":"false");
	}

	public void setAsync(boolean failure) {
		setAttribute("async", (failure)?"true":"false");
	}

	public void setFailed(String failure) {
		setAttribute("fail", failure);
	}

	protected String getFailed() {
		return getAttribute("fail");
	}

	protected String getAsync() {
		return getAttribute("async");
	}

	public TestCaseTrace getWrapper(Element e) {
		return new TestCaseTrace (e);
	}
	
	// Iterator Methods

	public Iterator<Transition> transitions () {
		Element t = getElement();
		if (t.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Transition> (t, new TestCaseTransition());
		}
		return null;		
	}
	
	public Iterator<Transition> iterator() {
		return transitions();
	}

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
		t.setAsync(false);
		return t;
	}

	public void setFinalActivity(ActivityState theActivity) {
		Transition lastTransition = getFinalTransition();
		if (lastTransition != null) {
			lastTransition.setFinalActivity(theActivity);
		}
	}
	
	public Transition getFinalTransition() {
		Transition lastTransition = null;
		for (Transition t: this) {
			lastTransition = t;
		}
		return lastTransition;
	}
	
	public void setDateTime (String time) {
		setAttribute("date_time", time);
	}
	
	public String getDateTime () {
		return getAttribute("date_time");
	}
		
}
