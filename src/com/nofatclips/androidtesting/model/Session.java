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
	public void removeTrace (Trace theTrace);
	public String getSleepAfterRestart();
	public String getSleepAfterEvent();
	public String getSleepOnThrobber();
	public String getSleepAfterTask();
	public String getInAndOutFocus();
	public void addFailedTrace(Trace theTask);
	public void addCrashedTrace (Trace theTask);
	public void setComparationWidgets (String commaSeparatedTypes);
	public String getComparationWidgets ();
	
	public void parse (String xml);

}
