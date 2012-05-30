package com.nofatclips.androidtesting.guitree;
import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.NodeListWrapper;

public class TestCaseTransition extends ElementWrapper implements Transition {

	public final static String TAG = "TRANSITION";
	
	public TestCaseTransition () {
		super();
	}

	public TestCaseTransition (Element transition)	{
		super(transition);
	}
	
	public TestCaseTransition (Document dom) {
		super (dom, TAG);
	}

	@Override
	public void setElement (Element transition) {
		super.setElement (transition);
	}
	
	public NodeList eventProperties () {
		return getElement().getChildNodes();
	}

	public TestCaseTransition getWrapper(Element e) {
		return new TestCaseTransition(e);
	}

	public StartActivity getStartActivity() {
		return new StartActivity ((Element) eventProperties().item(0));
	}
	
	public void setStartActivity (ActivityState a) {
		getElement().replaceChild(a.getElement(), getStartActivity().getElement());
	}	
	
	public Iterator<UserInput> inputs() {
		return new NodeListWrapper<UserInput>((Element) eventProperties().item(1), new TestCaseInput());
	}
	
	public Iterator<UserInput> iterator() {
		return inputs();
	}
	
	public void addInput (UserInput i) {
		eventProperties().item(1).appendChild(i.getElement());
	}

	public TestCaseEvent getEvent() {
		return new TestCaseEvent ((Element) eventProperties().item(2));
	}
	
	public void setEvent (UserEvent e) {
		getElement().replaceChild(e.getElement(), getEvent().getElement());
	}

	public FinalActivity getFinalActivity() {
		return new FinalActivity ((Element) eventProperties().item(3));
	}

	public void setFinalActivity (ActivityState a) {
		getElement().replaceChild(a.getElement(), getFinalActivity().getElement());
	}
	
	public String getId() {
		return getElement().getAttribute("id_transition");
	}

	public void setId (String id) {
		getElement().setAttribute("id_transition",id);
	}

	public static TestCaseTransition createTransition (Document dom) {
		TestCaseTransition t = new TestCaseTransition(dom);
		StartActivity sa = StartActivity.createActivity(dom);
		t.appendChild(sa); // t.setStartActivity(sa);
		Element inputz = dom.createElement("INPUTS");
		t.appendChild(inputz);
		TestCaseEvent e = TestCaseEvent.createEvent(dom);
		t.appendChild(e); // t.setEvent(e);
		FinalActivity fa = FinalActivity.createActivity(dom);
		t.appendChild(fa); // t.setFinalActivity(fa);
		return t;
	}
	
	public TestCaseTransition clone () {
		TestCaseTransition t = createTransition (this.getElement().getOwnerDocument());
		t.setStartActivity(this.getStartActivity().clone());
		for (UserInput i: this) {
			t.addInput (((TestCaseInput)i).clone());
		}
		t.setEvent(this.getEvent().clone());
		t.setFinalActivity(this.getFinalActivity().clone());
		return t;
	}
		
}
