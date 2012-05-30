package com.nofatclips.androidtesting.graphviz;

import com.nofatclips.androidtesting.model.ActivityState;

public class Node {
	
	private String id="";
	private String label="";
	private String image="";
	private String name="";
	
	public Node (String id) {
		setId(id);
	}
	
	public Node (ActivityState s) {
		this (s.getUniqueId());
		setImage (s.getScreenshot());
		setName (s.getName());
		setLabel (s.getId());
	}
	
	public String getLabel() {
		return this.label;
	}
	
	public void setLabel(String label) {
		this.label = label;
	}
	
	public boolean hasImage() {
		return (!getImage().equals(""));
	}
	
	public String getImage() {
		return this.image;
	}
	
	public void setImage(String image) {
		this.image = image;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setName(String name) {
		this.name = name;
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}
	
	public String toString() {
		return  getId();
	}

}
