package com.nofatclips.androidtesting.graphviz;

import java.util.ArrayList;
//import java.util.LinkedHashMap;
import java.util.List;
//import java.util.Map;

import com.nofatclips.androidtesting.Plottable;
import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.*;

import static com.nofatclips.androidtesting.graphviz.DotUtilities.*;

public class GuiTreeToDot implements Plottable {

	public GuiTreeToDot (GuiTree session) {
		this.session = session;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
//		this.edges = new LinkedHashMap<String,String>();
//		this.nodes = new LinkedHashMap<String,String>();
//		this.states = new LinkedHashMap<String,String>();
//		this.labels = new LinkedHashMap<String, String>();
//		this.count = 0;
	}
	
//	private String getCount() {
//		String out = "s"+this.count;
//		this.count++;
//		return out;
//	}
	
	public String getDot () {
		
		boolean first = true;
		for (Trace t: this.session) {
			Transition action = t.getFinalTransition();
			addTransition(action, first);
			first = false;
		}
		
		StringBuilder dot = new StringBuilder ();
		dot.append("digraph GuiTree {" + EOL + EOL);
		dot.append(TAB + "graph [nodesep=1, fontsize=36];" + EOL);
		dot.append(TAB + "node [fontsize=36];" + EOL);
		dot.append(TAB + "edge [fontsize=36, headport=ne];" + EOL);
		
		dot.append(EOL + "## Edges" + EOL + EOL);
		for (Edge edge: this.edges) {
			dot.append(TAB + edge + " [label=\"" + edge.getId() + ": " + edge.getLabel() + "\"];" + EOL);
		}
//		for (String edge:this.edges.keySet()) {
//			dot.append(TAB + edge + " [label=\"" + edges.get(edge) + "\"];" + EOL);
//		}

		dot.append(EOL + "## Nodes" + EOL + EOL);
		for (Node node: this.nodes) {
			if (node.hasImage()) {
				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + node.getLabel() + "\"; " + node + "};" + EOL);
				dot.append(TAB + node + " [label=\"\", shapefile=\"" + node.getImage() + "\"];" + EOL + EOL);				
			} else {
				dot.append(TAB + node + " [label=\"" + node.getLabel() + "\"];" + EOL + EOL);
			}
		}
//		for (String node:this.nodes.keySet()) {
//			String val = nodes.get(node);
//			String label = labels.get(node);
//			if (val.equals("")) {
//				dot.append(TAB + node + " [label=\"" + label + "\"];" + EOL + EOL);
//			} else {
////				dot.append(TAB + node + " [label=\"" + node + "\" image=\"" + val + "\"];" + EOL);
//				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + label  + "\"; " + node + "};" + EOL);
//				dot.append(TAB + node + " [label=\"\", shapefile=\""+val+"\"];" + EOL + EOL);
//			}
//		}

		dot.append("}");		
		return dot.toString();
	}

	private void addTransition(Transition action, boolean first) {
		Node start = new Node (action.getStartActivity());
		Node finish = new Node (action.getFinalActivity());
//		String start = action.getStartActivity().getId();
//		String finish = action.getFinalActivity().getId();
//		String finishShot = action.getFinalActivity().getScreenshot();
//		String nodeName = action.getFinalActivity().getUniqueId();
		UserEvent event = action.getEvent();
		
		// Add main activity to nodes
		if (first) {
//			String startShot = action.getStartActivity().getScreenshot();
//			String rootName = action.getStartActivity().getUniqueId();
//			this.states.put(start, rootName);
			this.nodes.add(start);
//			this.nodes.put(rootName, startShot);
//			first = false;
		}
		
//		// Add new activity to state map
//		if (!(this.states.containsKey(finish))) {
//			this.states.put(finish, nodeName);
//		}
		
		// Add new activity to nodes
//		String imageName = (abnormalState(finish))?finish:(finishShot);
//		String imageName = (finishShot.equals(""))?finish:(finishShot);
//		this.nodes.put(nodeName, finishShot);
		
		finish.setLabel(createLabel (finish));
		this.nodes.add(finish);
//		if (abnormalState(finish)) {
//			this.labels.put(nodeName, "");
//		} else if (finish.equals(nodeName) || finish.equals("")) {
//			this.labels.put(nodeName, nodeName);
//		} else {
//			this.labels.put(nodeName, nodeName + " = " + finish);
//		}
		
		// Add event to edges
		Edge e = new Edge(start,finish);
		e.setLabel(getCaption(event));
		e.setId(event.getId());
		this.edges.add(e);
//		String pass = this.states.get(start) + " -> " + nodeName;
//		this.edges.put (pass,event.getId() + ": " + getCaption(event));
	
	}

	private String createLabel (Node state) {
		String label = state.getLabel();
		String id = state.getId();
		if (label.equals(state.getId())) return label;
		if (abnormalState(label)) return label;
		if (id.equals("")) return label;
		if (label.equals("")) return id;
		return id + " = " + label;
	}

	protected boolean abnormalState (String id) {
		return ((id.equals("exit")) || (id.equals("crash")) || (id.equals("fail")));
	}
	    
	private GuiTree session;
	private List<Node> nodes;
	private List<Edge> edges;
	
//	final String EOL = System.getProperty("line.separator");
//	final String TAB = "\t";

//	private Map<String,String> edges;
//	private Map<String,String> nodes;
//	private Map<String,String> labels;
//	private Map<String,String> states;
//	private int count;
	
}