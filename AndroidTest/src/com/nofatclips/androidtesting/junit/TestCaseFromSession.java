package com.nofatclips.androidtesting.junit;

import java.lang.reflect.Field;
import java.util.HashMap;

import com.nofatclips.androidtesting.ActivityMap;
import com.nofatclips.androidtesting.model.*;
import com.nofatclips.androidtesting.source.SourceCodeBuilder;

import static com.nofatclips.androidtesting.junit.JunitUtilities.*;
import static com.nofatclips.androidtesting.model.SimpleType.NULL;

public class TestCaseFromSession implements Testable {

	public TestCaseFromSession (Session session) {
		this.aGuiTree = session;
		this.j = new SourceCodeBuilder ();
	}

	public TestCaseFromSession (Session session, ActivityMap map) {
		this(session);
		this.activities = map;
	}

	public String getJUnit() {

		String packageName = this.aGuiTree.getPackageName();
		String className = this.aGuiTree.getClassName();
		String testClassName = tidyName(this.aGuiTree.getAppName()) + "GuiTest";
		String sleepAfterEvent = this.aGuiTree.getSleepAfterEvent ();
		String sleepAfterRestart = this.aGuiTree.getSleepAfterRestart ();
		String sleepOnThrobber = this.aGuiTree.getSleepOnThrobber();
		String sleepAfterTask = this.aGuiTree.getSleepAfterTask();
		String inAndOutFocus = this.aGuiTree.getInAndOutFocus();
		setComparationWidgets (this.aGuiTree.getComparationWidgets());
		
		this.j.setClassName(testClassName);
		this.j.includeSnippet("tc_imports.txt");

		loc ("public final static String PACKAGE_NAME = \"" + packageName + "\";");
		loc ("public final static String CLASS_NAME = \"" + className + "\";");
		loc ("public final static int SLEEP_AFTER_EVENT = " + (sleepAfterEvent.equals("")?2000:sleepAfterEvent) + ";");
		loc ("public final static int SLEEP_AFTER_RESTART = " + (sleepAfterRestart.equals("")?2000:sleepAfterRestart) + ";");
		loc ("public final static int SLEEP_ON_THROBBER = " + (sleepOnThrobber.equals("")?10000:sleepOnThrobber) + ";");
		loc ("public final static int SLEEP_AFTER_TASK = " + (sleepOnThrobber.equals("")?0:sleepAfterTask) + ";");
		loc ("public final static boolean FORCE_RESTART = false;").blank();
		loc ("public final static boolean IN_AND_OUT_FOCUS= " + inAndOutFocus +";").blank();
		
		for (Field f: InteractionType.class.getFields()) {
			loc ("public final static String " + constantField(f) + ";");
		}
		for (Field f: SimpleType.class.getFields()) {
			loc ("public final static String " + constantField(f) + ";");
		}
		
		loc ("public final static String[] PRECRAWLING = {};");

		loc("");
		this.j.includeSnippet("tc_framework.txt");

		// Build a map of the leaf nodes
		HashMap<String,String> leaves = new HashMap<String,String>();
		for (Trace task: aGuiTree) {
			// Remove from the leaf map all traces including the current one 
			for (Transition action: task) {
				leaves.remove(action.getEvent().getId());
			}
			// Add the current trace to the leaf map
			leaves.put(task.getFinalTransition().getEvent().getId(), task.getId());
		}
		
		loc ("// Test Cases").blank();
		for (Trace t: aGuiTree) {
			if (leaves.containsValue(t.getId())) {
				generateTest(t, getNumber());
			}
		}
	
		loc ("//Helper methods for doing the actual work with instrumentation").blank();
		this.j.includeSnippet("tc_helpers.txt");

		loc ("}"); 		
		return j.toString();
	}
	
	public void generateTest(Trace aTrace, String message, String testNum) {
		loc ("// Generated from trace " + aTrace.getId());
		loc ("public void testTrace" + padLeft(testNum) + " () {").blank();
		loc ("// Testing base activity");
		generateTest(this.aGuiTree.getBaseActivity(), "Testing base activity");
		for (Transition t: aTrace) {
			loc ("// Testing transition " + t.getId());
			for (UserInput input: t) {
				loc ("// Setting input: " + input.getId());
				loc ("setInput (" + input.getWidgetId() + ", \"" + input.getType() + "\", \"" + escapeJava(input.getValue()) + "\");");
			}
			UserEvent e = t.getEvent();
			WidgetState w = e.getWidget();
			String idOrNot = (w.getId().equals("-1") || generatedWidget(w))?"":w.getId() + ", ";
			loc ("// Firing event: " + e.getId());
			if (e.getValue().equals("") || (e.getValue()==null) ) {
				loc ("fireEvent (" + idOrNot + "" + w.getIndex() + ", \"" + escapeJava(w.getName()) + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\");").blank();
			} else {
				loc ("fireEvent (" + idOrNot + "" + w.getIndex() + ", \"" + escapeJava(w.getName()) + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\", \"" + e.getValue() + "\");").blank();
			}
			generateTest (t.getFinalActivity());
		}
		loc ("solo.sleep (SLEEP_AFTER_TASK);");
		loc ("}").blank();
	}
	
	private void generateTest(ActivityState anActivity, String message) {
		if (anActivity.isCrash() || anActivity.isExit() || anActivity.isFailure()) {
			loc ("// This event leads to " + anActivity.getDescriptionId());
			return;
		}
		anActivity = getCompleteActivity(anActivity);
		loc ("retrieveWidgets();");
		loc ("// Testing current activity: should be " + anActivity.getId());
		loc ("solo.assertCurrentActivity(\"" + message + "\", \"" + anActivity.getName() + "\");");
		for (WidgetState w: anActivity) {
			if (!matchClass(w.getSimpleType())) continue;
			if (generatedWidget(w)) {
				loc ("doTestWidget(\"" + w.getType() + "\", \"" + escapeJava(w.getName()) + "\");");
			} else {
				loc ("doTestWidget(" + w.getId() + ", \"" + w.getType() + "\", \"" + escapeJava(w.getName()) + "\");");
			}
		}
		j.blank();
	}
	
	public void generateTest (Trace t) {
		generateTest (t, Integer.valueOf(t.getId()));
	}

	public void generateTest (Trace t, int testNum) {
		generateTest (t, "", String.valueOf(testNum));
	}

	private void generateTest (ActivityState a) {
		generateTest (a,"");
	}
	
	private SourceCodeBuilder loc (String code) {
		return this.j.loc (code);
	}
	
	private int getNumber()  {
		int ret = this.testNumber;
		this.testNumber++;
		return ret;
	}
	
	private String constantField (Field f) {
		String fieldValue = "";
		try {
			fieldValue = "\"" + f.get("").toString() + "\"";
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return f.getName() + " = " + fieldValue;
	}
	
	private ActivityState getCompleteActivity (ActivityState state) {
		if ((this.activities == null) || (state.getDescriptionId().equals("")) || (!this.activities.hasActivity(state)) ) return state;
		return this.activities.getActivity(state);
	}
	
	private void setComparationWidgets (String csv) {
		if (csv.equals("")) return;
		if (csv.equals(NULL)) {
			this.skipWidgets = true;
			return;
		}
		this.selectWidgets = true;
		this.widgetClasses = csv.split(",");
	}

	public boolean matchClass (String type) {
		if (this.skipWidgets) return false;
		if (!this.selectWidgets) return true;
		for (String storedType: this.widgetClasses) {
			if (storedType.equals(type)) return true;
		}
		return false;
	}
	
	public boolean generatedWidget (WidgetState w) {
		return (w.getType().startsWith("com.android.internal"));
	}

	Session aGuiTree;
	ActivityMap activities = null;
	SourceCodeBuilder j;
	int testNumber = 0;
	boolean selectWidgets = false;
	boolean skipWidgets = false;
	protected String[] widgetClasses;
	
}
