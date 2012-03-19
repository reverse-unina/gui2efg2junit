package com.nofatclips.androidtesting;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileReader;
import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.guitree.*;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class ActivityMap {

	public Map<String,TestCaseActivity> activities;
	public GuiTree doc;
	
	public ActivityMap (GuiTree theDoc) {
		this.doc = theDoc;
		this.activities = new HashMap<String, TestCaseActivity>();
	}
	
	public void addActivity(TestCaseActivity t) {
		addActivity(t.getId(), t);
	}

	public void addActivity(String id, TestCaseActivity t) {
		this.activities.put(id, t);
	}

	public TestCaseActivity getActivity(String id) {
		return this.activities.get(id);
	}
	
	public TestCaseActivity getActivity(TestCaseActivity t) {
		return getActivity(t.getDescriptionId());
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
		Session sandboxSession = getNewSession();
		TestCaseActivity s;
		Element e;
		for (String state: entries) {
//			System.out.println(state);
			sandboxSession.parse(state);
			e = ((XmlGraph)sandboxSession).getDom().getDocumentElement();
//			System.out.println(e.getTagName());
			s = this.doc.importState (e);
			addActivity(s);
		}
	}

	public Session getNewSession() {
		try {
			return new GuiTree();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

}
