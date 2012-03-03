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
			String nodeDesc = getCaption (new EfgEvent(e));
			this.nodes.put(name, nodeDesc);
			this.extractNodes(e);
		}		
	}
	
	public String getDot () {		
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
    
	private Document efg;
	private List<String> edges;
	private Map<String,String> nodes;

}