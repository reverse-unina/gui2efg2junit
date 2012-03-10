package com.nofatclips.androidtesting.graphviz;

import java.util.ArrayList;
import java.util.List;

import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.*;

import static com.nofatclips.androidtesting.graphviz.DotUtilities.*;

public class GuiTreeToDot implements Plottable {

	public GuiTreeToDot (GuiTree session) {
		this.session = session;
		this.nodes = new ArrayList<Node>();
		this.edges = new ArrayList<Edge>();
	}
	
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

		dot.append(EOL + "## Nodes" + EOL + EOL);
		for (Node node: this.nodes) {
			if (node.hasImage()) {
				dot.append(TAB + "subgraph cluster_" + node + "{label=\"" + node.getLabel() + "\"; " + node + "};" + EOL);
				dot.append(TAB + node + " [label=\"\", shapefile=\"" + node.getImage() + "\"];" + EOL + EOL);				
			} else {
				dot.append(TAB + node + " [label=\"" + node.getLabel() + "\"];" + EOL + EOL);
			}
		}
		
		dot.append("}");		
		return dot.toString();
	}

	private void addTransition(Transition action, boolean first) {
		Node start = getNode(action.getStartActivity()); //new Node (action.getStartActivity());
		Node finish = getNode(action.getFinalActivity()); //new Node (action.getFinalActivity());
		UserEvent event = action.getEvent();
		
		// Add main activity to nodes
		if (first) {
			this.nodes.add(start);
		}
				
		// Add new activity to nodes
		finish.setLabel(createLabel (finish));
		this.nodes.add(finish);
		
		// Add event to edges
		Edge e = new Edge(start,finish);
		e.setLabel(getCaption(event));
		e.setId(event.getId());
		this.edges.add(e);
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
	
	private Node getNode (ActivityState activity) {
		Node ret = new Node (activity);
		if (activity.isCrash()) {
			ret.setId(getCrashId());
		} else if (activity.isFailure()) {
			ret.setId(getFailId());
		}
		return ret;
	}
	
	private String getCrashId() {
		String ret = "c"+this.crashCount;
		this.crashCount++;
		return ret;
	}

	private String getFailId() {
		String ret = "f"+this.failCount;
		this.failCount++;
		return ret;
	}

	private GuiTree session;
	private List<Node> nodes;
	private List<Edge> edges;
	private int crashCount = 0;
	private int failCount = 0;
	
}