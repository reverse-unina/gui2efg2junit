package com.nofatclips.androidtesting.guitree;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.model.ActivityState;

public class FinalActivity extends TestCaseActivity {
	
	public FinalActivity(Element item) {
		super (item);
	}
	
	public final static String getTag () {
		return "FINAL_ACTIVITY";
	}
	
	public static FinalActivity createActivity (GuiTree session) {
		return createActivity (session.getDom());
	}
	
	public static FinalActivity createActivity (Document dom) {
		return (FinalActivity) createActivity (dom, getTag());
	}

	public static FinalActivity createActivity (Document dom, String tag) {
		Element el = dom.createElement(tag);
		Element desc = dom.createElement(DESC_TAG);
		el.appendChild(desc);
		return new FinalActivity (el);
	}
	
	public static FinalActivity createActivity (ActivityState originalActivity) {
		Document dom = originalActivity.getElement().getOwnerDocument();
		FinalActivity newActivity = createActivity (dom);
		newActivity.setName(originalActivity.getName());
		newActivity.setTitle(originalActivity.getTitle());
		newActivity.setId(originalActivity.getId());
		newActivity.copyDescriptionFrom(originalActivity);
		newActivity.setUniqueId(originalActivity.getUniqueId());
		newActivity.setScreenshot(originalActivity.getScreenshot());
		return newActivity;
	}

	public FinalActivity clone () {
		return createActivity(this);
	}

}
