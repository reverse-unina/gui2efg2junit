package com.nofatclips.androidtesting.junit;

import java.util.Locale;

import com.nofatclips.androidtesting.ActivityMap;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Testable;

public class JunitUtilities {

	public static String tidyName (String s) {
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
    
    public static String exportToJunit (Testable t) {
    	if (t instanceof Session) {
    		TestCaseFromSession converter = new TestCaseFromSession((Session)t);
    		return converter.getJUnit();
    	}
    	return "";
    }

    public static String exportToJunit (Testable t, ActivityMap m) {
    	if (t instanceof Session) {
    		TestCaseFromSession converter = new TestCaseFromSession((Session)t, m);
    		return converter.getJUnit();
    	}
    	return "";
    }

}
