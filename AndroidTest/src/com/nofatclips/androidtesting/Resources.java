package com.nofatclips.androidtesting;

import java.io.PrintWriter;
import java.io.StringWriter;

import javax.swing.JTextArea;

public class Resources {

	public static final int DEFAULT_WIDTH = 960;
	public static final int DEFAULT_HEIGHT = 540;
	public final static String GUI_TREE = "GUI Tree";
	public final static String EFG_XML = "Event Flow Tree (XML)";
	public final static String EFG_DOT = "Event Flow Tree (Dot)";
	public final static String JUNIT_JAVA = "JUnit Test";
	public final static String GUI_TREE_FILE_TYPE = "XML Gui Tree";
	public final static String LOGGER = "dek.xml.test";
	
	// Does debug when console and logging are not available.
	// Set it to one of the textareas which will be used for the output.
	public static JTextArea debugPane = null;

	public static void debug (String s) {
		if (debugPane instanceof JTextArea) 
			debugPane.setText(s);
		throw new Error();
	}

	public static void debug (Throwable e) {
		StringWriter traceWriter = new StringWriter();
		PrintWriter printWriter = new PrintWriter(traceWriter, false);
		e.printStackTrace(printWriter);
		printWriter.close();
		String faultMessage = traceWriter.getBuffer().toString();
		debug (faultMessage);
	}	

}
