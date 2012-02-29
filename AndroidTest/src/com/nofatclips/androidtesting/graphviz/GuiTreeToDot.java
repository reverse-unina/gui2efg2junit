package com.nofatclips.androidtesting.graphviz;

import java.util.LinkedHashMap;
import java.util.Map;

import com.nofatclips.androidtesting.Plottable;
import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.*;

import static com.nofatclips.androidtesting.graphviz.DotUtilities.*;

public class GuiTreeToDot implements Plottable {

	public GuiTreeToDot (GuiTree session) {
		this.session = session;
		this.edges = new LinkedHashMap<String,String>();
		this.nodes = new LinkedHashMap<String,String>();
		this.states = new LinkedHashMap<String,String>();
		this.count = 0;
	}
	
	private String getCount() {
		String out = "s"+this.count;
		this.count++;
		return out;
	}
	
	public String getDot () {
		final String EOL = System.getProperty("line.separator");
		final String TAB = "\t";
		
		StringBuilder dot = new StringBuilder ();
		dot.append("digraph GuiTree {" + EOL + EOL);
		dot.append(TAB + "graph [nodesep=1, fontsize=36];" + EOL);

		boolean first = true;
		for (Trace t: this.session) {
			Transition action = t.getFinalTransition();
			String start = action.getStartActivity().getId();
			String finish = action.getFinalActivity().getId();
			UserEvent event = action.getEvent();
			
			// Add main activity to nodes
			if (first) {
				String tmp = getCount();
				this.states.put(start, tmp);
				this.nodes.put(tmp, start+".jpg");
				first = false;
			}

			String nodeName = getCount();

			// Add new activity to state map
			if (!(this.states.containsKey(finish))) {
				this.states.put(finish, nodeName);
			}
			
			// Add new activity to nodes
			String imageName = (abnormalState(finish))?finish:(finish+".jpg");
			this.nodes.put(nodeName, imageName);
			
			// Add event to edges
			String pass = this.states.get(start) + " -> " + nodeName;
			this.edges.put (pass,event.getId() + ": " + getCaption(event));
		}
		
		
		dot.append(EOL + "## Edges" + EOL);
		dot.append(EOL + TAB + "edge [fontsize=36, headport=ne];" + EOL + EOL);
		for (String edge:this.edges.keySet()) {
			dot.append(TAB + edge + " [label=\"" + edges.get(edge) + "\"];" + EOL);
		}

		dot.append(EOL + "## Nodes" + EOL + EOL);
		for (String node:this.nodes.keySet()) {
			String val = nodes.get(node);
			if (abnormalState(val)) {
				dot.append(TAB + node + " [label=\"" + val + "\"];" + EOL + EOL);
			} else {
//				dot.append(TAB + node + " [label=\"" + node + "\" image=\"" + val + "\"];" + EOL);
				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + node + "\"; " + node + "};" + EOL);
				dot.append(TAB + node + " [label=\"\", shapefile=\""+val+"\"];" + EOL + EOL);
			}
		}

		dot.append(EOL + "}");		
		return dot.toString();
	}

//	public String getCaption (UserEvent event) {
//		Element e = event.getElement();
//		Element widget = event.getWidget().getElement();
//		String type = e.getAttribute("type");
//		String target = widget.getAttribute("name");
//		boolean special = widget.getAttribute("type").equals("null");
//		if (target.equals(""))
//			target = e.getAttribute("desc");
//		if (target.equals(""))
//			target = e.getAttribute("value");
//		if (target.equals("")) {
//			target = e.getAttribute("widget_type");
//			if (e.getAttribute("widget_id")!="") {
//				target = target + " #" + e.getAttribute("widget_id");
//			}
//		}
//		String nodeDesc = special?type:(type + "\\n'" + escapeDot(target) + "'");
//		return nodeDesc;
//
//	}
	
	protected boolean abnormalState (String id) {
		return ((id.equals("exit")) || (id.equals("crash")) || (id.equals("fail")));
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
//    public static String hex(char ch) {
//        return Integer.toHexString(ch).toUpperCase(Locale.ENGLISH);
//    }
    
	private GuiTree session;
	private Map<String,String> edges;
	private Map<String,String> nodes;
	private Map<String,String> states;
	private int count;
	
}