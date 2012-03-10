package com.nofatclips.androidtesting.junit;

import java.lang.reflect.Field;
import java.util.HashMap;
import com.nofatclips.androidtesting.model.*;
import com.nofatclips.androidtesting.source.SourceCodeBuilder;

import static com.nofatclips.androidtesting.junit.JunitUtilities.*;

public class TestCaseFromSession implements Testable {

	public TestCaseFromSession (Session session) {
		this.aGuiTree = session;
		this.j = new SourceCodeBuilder ();
	}
	
	public String getJUnit() {

		String packageName = this.aGuiTree.getPackageName();
		String className = this.aGuiTree.getClassName();
		String testClassName = tidyName(this.aGuiTree.getAppName()) + "GuiTest"; // + ((className == "")?"":generic);
		String sleepAfterEvent = this.aGuiTree.getSleepAfterEvent ();
		String sleepAfterRestart = this.aGuiTree.getSleepAfterRestart ();
		String sleepOnThrobber = this.aGuiTree.getSleepOnThrobber();
		String inAndOutFocus = this.aGuiTree.getInAndOutFocus();
		
		this.j.setClassName(testClassName);
		this.j.includeSnippet("tc_imports.txt");

		loc ("public final static String PACKAGE_NAME = \"" + packageName + "\";");
		loc ("public final static String CLASS_NAME = \"" + className + "\";");
		loc ("public final static int SLEEP_AFTER_EVENT = " + (sleepAfterEvent.equals("")?2000:sleepAfterEvent) + ";");
		loc ("public final static int SLEEP_AFTER_RESTART = " + (sleepAfterRestart.equals("")?2000:sleepAfterRestart) + ";");
		loc ("public final static int SLEEP_ON_THROBBER = " + (sleepOnThrobber.equals("")?10000:sleepOnThrobber) + ";");
		loc ("public final static boolean FORCE_RESTART = false;").blank();
		loc ("public final static boolean IN_AND_OUT_FOCUS= " + inAndOutFocus +";").blank();
		
		for (Field f: InteractionType.class.getFields()) {
//			String fieldType = f.getType().getSimpleName();
//			String fieldName = f.getName();
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
			loc ("public final static String " + f.getName() + " = " + fieldValue + ";");
		}

		for (Field f: SimpleType.class.getFields()) {
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
			loc ("public final static String " + f.getName() + " = " + fieldValue + ";");
		}

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
				generateTest(t);
			}
		}
	
		loc ("//Helper methods for doing the actual work with instrumentation").blank();
		this.j.includeSnippet("tc_helpers.txt");

		loc ("}"); 		
		return j.toString();
	}
	
	private void generateTest(Trace aTrace, String message) {
		loc ("public void testTrace" + padLeft(aTrace.getId()) + " () {").blank();
		loc ("// Testing base activity");
		generateTest(this.aGuiTree.getBaseActivity(), "Testing base activity");
		for (Transition t: aTrace) {
			loc ("// Testing transition " + t.getId());
			for (UserInput input: t) {
				loc ("setInput (" + input.getWidgetId() + ", \"" + input.getType() + "\", \"" + escapeJava(input.getValue()) + "\");");
			}
			UserEvent e = t.getEvent();
			WidgetState w = e.getWidget();
			String idOrNot = (w.getId().equals("-1"))?"":w.getId() + ", ";
			if (e.getValue().equals("") || (e.getValue()==null) ) {
				loc ("fireEvent (" + idOrNot + "" + w.getIndex() + ", \"" + escapeJava(w.getName()) + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\");").blank();
			} else {
				loc ("fireEvent (" + idOrNot + "" + w.getIndex() + ", \"" + escapeJava(w.getName()) + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\", \"" + e.getValue() + "\");").blank();
			}
			loc ("// Testing final activity for transition " + t.getId());
			generateTest (t.getFinalActivity());
		}
		loc ("}").blank();
	}
	
	private void generateTest(ActivityState anActivity, String message) {
		loc ("retrieveWidgets();");
		loc ("solo.assertCurrentActivity(\"" + message + "\", \"" + anActivity.getName() + "\");");
		for (WidgetState w: anActivity) {
			loc ("doTestWidget(" + w.getId() + ", \"" + w.getType() + "\", \"" + escapeJava(w.getName()) + "\");");
		}
		j.blank();
	}
	
	private void generateTest (Trace t) {
		generateTest (t,"");
	}

	private void generateTest (ActivityState a) {
		generateTest (a,"");
	}
	
	private SourceCodeBuilder loc (String code) {
		return this.j.loc (code);
	}

    Session aGuiTree;
	SourceCodeBuilder j;
	
}
