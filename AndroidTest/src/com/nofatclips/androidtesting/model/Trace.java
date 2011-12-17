package com.nofatclips.androidtesting.model;

import java.util.Iterator;

public interface Trace extends Iterable<Transition>, WrapperInterface {

	public String getId ();
	public void setId (String id);
	public Iterator<Transition> transitions();
	public void addTransition(Transition tail);
	public void setFinalActivity(ActivityState theActivity);
	public Transition getFinalTransition ();
	public boolean isFailed();
	public void setFailed (boolean failure);
	public boolean isAsync();
	public void setAsync (boolean failure);
	public String getDateTime ();
	public void setDateTime (String time);
	
}
