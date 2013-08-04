package com.nofatclips.androidtesting.source;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import com.nofatclips.androidtesting.xml.XmlGraph;

public class SourceCodeBuilder {
	
	public SourceCodeBuilder () {
		this ("");
	}
	
	public SourceCodeBuilder (String className) {
		this.s = new StringBuilder ();
		setClassName (className);
	}
	
	public void setClassName (String className) {
		this.className = className;
	}
	
	public String getClassName () {
		return this.className;
	}
	
	public SourceCodeBuilder moreIndent () {
		this.indent++;
		return this;
	}
	
	public SourceCodeBuilder lessIndent () {
		this.indent--;
		if (this.indent < 0)
			this.indent = 0;
		return this;
	}
	
	private void tab () {
		for (int i=0; i<indent; i++)
			this.s.append(TAB);
	}
	
	public boolean isGuess() {
		return guess;
	}

	public void setGuess(boolean guess) {
		this.guess = guess;
	}

	public SourceCodeBuilder loc (String code) {
		if (isGuess() && (code.length()>0) && (code.charAt(0) == '}'))
			lessIndent();
		tab();
		if (getClassName() != "")
			code = code.replaceAll("%TEST_CLASS_NAME%", getClassName());
		s.append (code + EOL);
		if (isGuess() && (code.length()>0) && (code.charAt(code.length()-1) == '{'))
			moreIndent();
		return this;
	}
	
	public SourceCodeBuilder blank () {
		s.append(EOL);
		return this;
	}
	
	public String toString () {
		return s.toString();
	}
	
	public void includeSnippet (String resourceName) {
		InputStream u = XmlGraph.class.getClassLoader().getResourceAsStream("ext/"+resourceName);
		if (u == null)
			return;
		try {
			DataInputStream in = new DataInputStream(u);
			BufferedReader code = new BufferedReader(new InputStreamReader(in));
			include (code);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void include (BufferedReader code) throws IOException {
		String line;
		while ((line = code.readLine()) != null) {
		   loc(line.trim());
		}
		blank();
	}
	
	private int indent = 0; // Indent level
	StringBuilder s;
	private boolean guess = true; // Automatically guess indentation if true
	private String className;
	
	final static String EOL = System.getProperty("line.separator");
	final static String TAB = "\t";

}