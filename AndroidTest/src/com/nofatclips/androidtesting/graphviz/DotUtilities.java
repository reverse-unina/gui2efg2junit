package com.nofatclips.androidtesting.graphviz;

import java.util.Locale;

import com.nofatclips.androidtesting.model.UserEvent;

public class DotUtilities {

	public static String getCaption (UserEvent event) {
		String type = event.getType();
		String target = event.getWidgetName();
		boolean special = event.getWidgetType().equals("null");
		if (target.equals(""))
			target = event.getDescription();
		if (target.equals(""))
			target = event.getValue();
		if (target.equals("")) {
			target = event.getWidgetType();
			if (!(event.getWidgetId().equals(""))) {
				target = target + " #" + event.getWidgetId();
			}
		}
		String nodeDesc = special?type:(type + "\\n'" + escapeDot(target) + "'");
		return nodeDesc;

	}

	public static String escapeDot (String str) {
        if (str == null) {
            return null;
        }
        int sz = str.length();
        StringBuffer out = new StringBuffer(sz * 2);
        for (int i = 0; i < sz; i++) {
            char ch = str.charAt(i);
            // handle unicode
//            if (ch > 0xfff) {
//                out.append("\\u").append(hex(ch));
//            } else if (ch > 0xff) {
//                out.append("\\u0").append(hex(ch));
//            } else if (ch > 0x7f) {
//                out.append("\\u00").append(hex(ch));
//            } else 
           	if (ch < 32) {
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

    public static String hex(char ch) {
        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
    }

}
