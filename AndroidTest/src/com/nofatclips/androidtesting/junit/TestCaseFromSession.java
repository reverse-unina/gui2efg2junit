package com.nofatclips.androidtesting.junit;

import java.lang.reflect.Field;
import java.util.Locale;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.Testable;
import com.nofatclips.androidtesting.model.InteractionType;
import com.nofatclips.androidtesting.model.SimpleType;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;
import com.nofatclips.androidtesting.model.WidgetState;
import com.nofatclips.androidtesting.source.SourceCodeBuilder;

public class TestCaseFromSession implements Testable {

	public TestCaseFromSession (Session session) {
		this.aGuiTree = session;
		this.j = new SourceCodeBuilder ();
	}
	
	public String getJUnit() {

		String packageName = this.aGuiTree.getPackageName();
		String className = this.aGuiTree.getClassName();
		String testClassName = tidyName(this.aGuiTree.getAppName()) + "GuiTest"; // + ((className == "")?"":generic);
		
		this.j.setClassName(testClassName);
		this.j.includeSnippet("tc_imports.txt");

		loc ("public final static String PACKAGE_NAME = \"" + packageName + "\";");
		loc ("public final static String CLASS_NAME = \"" + className + "\";");
		loc ("public final static int SLEEP_AFTER_EVENT = 2000;");
		loc ("public final static int SLEEP_AFTER_RESTART = 2000;");
		loc ("public final static boolean FORCE_RESTART = false;").blank();

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

		loc ("// Test Cases").blank();
		for (Trace t: aGuiTree) {
			generateTest(t);
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
				loc ("setInput (" + input.getWidgetId() + ", \"" + input.getType() + "\", \"" + input.getValue() + "\");");
			}
			UserEvent e = t.getEvent();
			WidgetState w = e.getWidget();
			String idOrNot = (w.getId().equals("-1"))?"":w.getId() + ", ";
			if (e.getValue().equals("") || (e.getValue()==null) ) {
				loc ("fireEvent (" + idOrNot + "\"" + w.getName() + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\");").blank();
			} else {
				loc ("fireEvent (" + idOrNot + "\"" + w.getName() + "\", \"" + w.getSimpleType() + "\", \"" + e.getType() + "\", \"" + e.getValue() + "\");").blank();
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
	
	public String tidyName (String s) {
		int i = s.lastIndexOf('.');
		String initial = s.substring(i+1,i+2).toUpperCase();
		return initial + s.substring(i+2).replaceAll("[^a-zA-Z0-9_]", "");
	}

	// Escape method taken from Apache Commons
	public static String escapeJava (String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer out = new StringBuffer(sz * 2);
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            // handle unicode
            if (ch > 0xfff) {
                out.append("\\u").append(hex(ch));
            } else if (ch > 0xff) {
                out.append("\\u0").append(hex(ch));
            } else if (ch > 0x7f) {
                out.append("\\u00").append(hex(ch));
            } else if (ch < 32) {
                switch (ch) {
                    case '\b' :
                        out.append('\\');
                        out.append('b');
                        break;
                    case '\n' :
                        out.append('\\');
                        out.append('n');
                        break;
                    case '\t' :
                        out.append('\\');
                        out.append('t');
                        break;
                    case '\f' :
                        out.append('\\');
                        out.append('f');
                        break;
                    case '\r' :
                        out.append('\\');
                        out.append('r');
                        break;
                    default :
                        if (ch > 0xf) {
                            out.append("\\u00").append(hex(ch));
                        } else {
                            out.append("\\u000").append(hex(ch));
                        }
                        break;
                }
            } else {
                switch (ch) {
                    case '"' :
                        out.append('\\');
                        out.append('"');
                        break;
                    case '\\' :
                        out.append('\\');
                        out.append('\\');
                        break;
                    default :
                        out.append(ch);
                        break;
                }
            }
        }
        return out.toString();
    }

    public static String  hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }
    
    public static String padLeft(String s) {
        return String.format("%05d", Integer.valueOf(s));  
    }

    Session aGuiTree;
	SourceCodeBuilder j;
	
}
