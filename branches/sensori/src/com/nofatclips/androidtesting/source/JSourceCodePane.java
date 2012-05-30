package com.nofatclips.androidtesting.source;

import java.awt.Component;
import java.util.HashMap;

import javax.swing.Icon;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;

@SuppressWarnings("serial")
public class JSourceCodePane extends JTabbedPane {

	public JSourceCodePane () {
		this.areas = new HashMap<String, Component>();
	}
	
	public void addEnabled (String s, Icon i, Component c) {
		addTab (s,i,c,true);
//		setEnabledAt(indexOfComponent(scroll), true);
	}

	public void addDisabled (String s, Icon i, Component c) {
		addTab (s,i,c,false);
//		setEnabledAt(indexOfComponent(scroll), false);
	}
	
	public void addTab (String s, Icon i, Component c, boolean enabled) {
		JScrollPane scroll = new JScrollPane(c);
		addTab(s,i,scroll);
		areas.put(s,c);
		setEnabledAt(indexOfComponent(scroll), enabled);
	}
	
	public void enableTab (String s) {
		setEnabledAt(indexOfTab(s), true);
	}

	public void disableTab (String s) {
		setEnabledAt(indexOfTab(s), false);
	}
	
	public void showCode (String tab, String code) {
		enableTab(tab);
		Component c = areas.get(tab);
		if (c instanceof JSourceCodeArea)
			((JSourceCodeArea) c).initText(code);
	}
	
	public void setFileName (String tab, String name) {
		Component c = areas.get(tab);
		if (c instanceof JSourceCodeArea)
			((JSourceCodeArea) c).setDefaultFileName(name);
	}

	private HashMap<String,Component> areas;
	
}
