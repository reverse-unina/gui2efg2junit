package com.nofatclips.androidtesting;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.guitree.*;
import com.nofatclips.androidtesting.model.ActivityState;

public class ActivityMap {

	public Map<String,ActivityState> activities;
	public GuiTree doc;
	
	public ActivityMap (GuiTree theDoc) {
		this.doc = theDoc;
		this.activities = new LinkedHashMap<String, ActivityState>();
	}
	
	public void addActivity(ActivityState t) {
		addActivity(t.getId(), t);
	}

	public void addActivity(String id, ActivityState t) {
		this.activities.put(id, t);
	}

	public ActivityState getActivity(String id) {
		return this.activities.get(id);
	}
	
	public ActivityState getActivity(ActivityState t) {
		return getActivity(t.getDescriptionId());
	}
	
	public boolean hasActivity (ActivityState t) {
		return hasActivity(t.getDescriptionId());
	}
	
	public boolean hasActivity (String id) {
		return this.activities.containsKey(id);
	}

	public List<String> readStateFile (String stateFileName) {
		FileInputStream theFile;
		BufferedReader theStream = null;
		String line;
		List<String> output = new ArrayList<String>();
		try{
			theFile = new FileInputStream(stateFileName);
			theStream = new BufferedReader (new FileReader (theFile.getFD()));
			boolean first = true;
			while ( (line = theStream.readLine()) != null) {
				String dtd = (first)?"start_activity.dtd":"final_activity.dtd";
				String tag = (first)?"START_ACTIVITY":"FINAL_ACTIVITY";
				String root = "<"+tag;
				String doctype = "<!DOCTYPE " + tag + " PUBLIC \"" + tag + "\" \"" + dtd + "\">";
				output.add(line.replaceFirst(root, doctype + root));
				first = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return output;
	}
	
	public void loadActivities() {
		loadActivities(this.doc.getStateFileName());
	}
	
	public void loadActivities(String stateFile) {
		List<String> entries = readStateFile(stateFile); //"C:\\Users\\mm\\Desktop\\Applicazioni test\\Wordpress2\\15 - Screenshots E01\\activities.xml"); //this.doc.getStateFileName());
		GuiTree sandboxSession = getNewSession();
		TestCaseActivity s;
		Element e;
		for (String state: entries) {
			sandboxSession.parse(state);
			e = sandboxSession.getDom().getDocumentElement();
			s = sandboxSession.importState (e);
			addActivity(s);
		}
	}

	public GuiTree getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
