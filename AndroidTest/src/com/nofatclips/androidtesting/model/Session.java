package com.nofatclips.androidtesting.model;

import java.util.Iterator;

public interface Session extends Iterable<Trace> {
	
	public String getDateTime ();
	public String getAppName ();
	public String getPackageName();
	public String getClassName();
	public Iterator<Trace> traces();
	public ActivityState getBaseActivity();
	public void addTrace (Trace theTrace);

}
