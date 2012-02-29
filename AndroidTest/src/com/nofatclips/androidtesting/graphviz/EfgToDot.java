package com.nofatclips.androidtesting.graphviz;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import com.nofatclips.androidtesting.Plottable;
import com.nofatclips.androidtesting.xml.NodeListIterator;
import com.nofatclips.androidtesting.efg.*;

import static com.nofatclips.androidtesting.graphviz.DotUtilities.*;

public class EfgToDot implements Plottable {

	public EfgToDot (EventFlowTree efg) {
		this.efg = efg.getDom();
		this.edges = new ArrayList<String>();
		this.nodes = new HashMap<String,String>();
	}
	
	public void extractEdges () {
		Element efg = (Element) this.efg.getChildNodes().item(0);
		this.extractEdges (efg);
	}
	
	private void extractEdges (Element evento) {
		String parentName = evento.getAttribute("id");
		for (Element e: new NodeListIterator (evento)) {
			String transition = parentName + " -> " + e.getAttribute("id");
			this.edges.add (transition);
			this.extractEdges(e);
		}		
	}
	
	public void extractNodes() {
		Element efg = (Element) this.efg.getChildNodes().item(0);
		this.extractNodes (efg);
	}
	
	private void extractNodes(Element evento) {
		for (Element e: new NodeListIterator (evento)) {
			String name = e.getAttribute("id");
//			String type = e.getAttribute("type");
//			String target = e.getAttribute("widget_name");
//			boolean special = e.getAttribute("widget_type").equals("null");
//			if (target.equals(""))
//				target = e.getAttribute("desc");
//			if (target.equals(""))
//				target = e.getAttribute("value");
//			if (target.equals("")) {
//				target = e.getAttribute("widget_type");
//				if (e.getAttribute("widget_id")!="") {
//					target = target + " #" + e.getAttribute("widget_id");
//				}
//			}
//			String nodeDesc = special?type:(type + "\\n'" + escapeDot(target) + "'");
			String nodeDesc = getCaption (new EfgEvent(e));
			this.nodes.put(name, nodeDesc);
			this.extractNodes(e);
		}		
	}
	
	public String getDot () {
		final String EOL = System.getProperty("line.separator");
		final String TAB = "\t";
		
		StringBuilder dot = new StringBuilder ();
		dot.append("digraph EFG {" + EOL);
		
		extractEdges();
		dot.append(EOL + "## Edges" + EOL + EOL);
		for (String t:this.edges) {
			dot.append(TAB + t + ";" + EOL);
		}
		
		extractNodes();
		dot.append(EOL + "## Nodes" + EOL + EOL);
		for (String name:this.nodes.keySet()) {
			dot.append(TAB + name + " [label=\"" + nodes.get(name) + "\"];" + EOL);
		}

		dot.append(EOL + "}");		
		return dot.toString();
	}
	
//	public static String escapeDot (String str) {
//        if (str == null) {
//            return null;
//        }
//        int sz = str.length();
//        StringBuffer out = new StringBuffer(sz * 2);
//        for (int i = 0; i < sz; i++) {
//            char ch = str.charAt(i);
//            // handle unicode
////            if (ch > 0xfff) {
////                out.append("\\u").append(hex(ch));
////            } else if (ch > 0xff) {
////                out.append("\\u0").append(hex(ch));
////            } else if (ch > 0x7f) {
////                out.append("\\u00").append(hex(ch));
////            } else 
//           	if (ch < 32) {
//                switch (ch) {
//                    case '\b' :
//                        out.append('\\');
//                        out.append('b');
//                        break;
//                    case '\n' :
//                        out.append('\\');
//                        out.append('n');
//                        break;
//                    case '\t' :
//                        out.append('\\');
//                        out.append('t');
//                        break;
//                    case '\f' :
//                        out.append('\\');
//                        out.append('f');
//                        break;
//                    case '\r' :
//                        out.append('\\');
//                        out.append('r');
//                        break;
//                    default :
//                        if (ch > 0xf) {
//                            out.append("\\u00").append(hex(ch));
//                        } else {
//                            out.append("\\u000").append(hex(ch));
//                        }
//                        break;
//                }
//            } else {
//                switch (ch) {
//                    case '"' :
//                        out.append('\\');
//                        out.append('"');
//                        break;
//                    case '\\' :
//                        out.append('\\');
//                        out.append('\\');
//                        break;
//                    default :
//                        out.append(ch);
//                        break;
//                }
//            }
//        }
//        return out.toString();
//    }
//
//    public static String  hex(char ch) {
//        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
//    }
    
	private Document efg;
	private List<String> edges;
	private Map<String,String> nodes;
}
