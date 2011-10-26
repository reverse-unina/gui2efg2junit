package com.nofatclips.androidtesting.guitree;

import java.util.Iterator;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.xml.ElementWrapper;
import com.nofatclips.androidtesting.xml.NodeListWrapper;

public class TestCaseActivity extends ElementWrapper implements ActivityState {
	
	public TestCaseActivity() {
		super();
	}

	public TestCaseActivity (Element e) {
		super(e);
	}
	
	public TestCaseActivity getWrapper(Element e) {
		return new TestCaseActivity(e);
	}

	@Override
	public void setElement (Element e) {
		super.setElement(e);
		this.description = (Element) getElement().getChildNodes().item(0); 
	}
	
	public Iterator<WidgetState> iterator() {
		if (this.description.getNodeName()==DESC_TAG) {
			return new NodeListWrapper<WidgetState> (this.description, new TestCaseWidget());
		}
		return null;
	}

	@Override
	public String getName() {
		return getElement().getAttribute("name");
	}
	
	@Override
	public void setName(String name) {
		getElement().setAttribute("name",name);
	}

	@Override
	public String getId() {
		return getElement().getAttribute("id");
	}

	@Override
	public void setId(String id) {
		getElement().setAttribute("id",id);		
	}

	public static TestCaseActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		Element desc = dom.createElement(DESC_TAG);
		el.appendChild(desc);
		return new TestCaseActivity (el);		
	}
	
	public static TestCaseActivity createActivity (Document dom) {
		return createActivity (dom, getTag());
	}

	public static TestCaseActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}
	
	// The main purpose of this method is to create the start activity of a transition from the final activity of the previous one
	public static TestCaseActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		TestCaseActivity newActivity = createActivity (dom);
		newActivity.copyDescriptionFrom(originalActivity);
//		for (WidgetState w: originalActivity) {
//			TestCaseWidget tcw = TestCaseWidget.createWidget(dom);
//			tcw.setIdNameType(w.getId(), w.getName(), w.getType());
//			newActivity.addWidget (tcw);
//		}
		return newActivity;
	}
	
//	public void copyDescriptionFrom (ActivityState originalActivity) {
//		Document dom = this.getElement().getOwnerDocument();
//		for (WidgetState w: originalActivity) {
//			TestCaseWidget tcw = TestCaseWidget.createWidget(dom);
//			tcw.setIdNameType(w.getId(), w.getName(), w.getType());
//			this.addWidget (tcw);
//		}
//	}

	public void copyDescriptionFrom (ActivityState originalActivity) {
		for (WidgetState w: originalActivity) {
			this.addWidget (w.clone());
		}
	}

	public TestCaseActivity clone () {
		return createActivity(this);
	}
	
	@Override
	public void addWidget(WidgetState w) {
		this.description.appendChild(w.getElement());
	}
	
	@Override
	public boolean hasWidget(WidgetState widgetToCheck) {
		for (WidgetState stored: this) {
			if (widgetToCheck.equals(stored))
				return true;
		}
		return false;
	}

	public static String getTag () {
		return "ACTIVITY";
	}

	private Element description;
	public final static String DESC_TAG = "DESCRIPTION";
	
}