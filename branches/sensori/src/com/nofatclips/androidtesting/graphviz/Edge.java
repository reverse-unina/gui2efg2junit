package com.nofatclips.androidtesting.graphviz;

public class Edge {
	
	private Node from;
	private Node to;
	private String id="";
	private String label="";
	
	public Edge (Node from, Node to) {
		setNodes(from,to);
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public Node getFrom() {
		return from;
	}
	
	public void setFrom(Node from) {
		this.from = from;
	}
	
	public Node getTo() {
		return to;
	}
	
	public void setTo(Node to) {
		this.to = to;
	}
	
	public void setNodes (Node from, Node to) {
		setFrom(from);
		setTo(to);
	}
	
	@Override
	public String toString() {
		return from.getId() + " -> " + to.getId(); 
	}
	
}
