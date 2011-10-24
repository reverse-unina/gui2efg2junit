package com.nofatclips.androidtesting.model;

import java.util.Iterator;

public interface Trace extends Iterable<Transition>, WrapperInterface {

	public String getId ();
	public void setId (String id);
	public Iterator<Transition> transitions();
	public void addTransition(Transition tail);
	public void setFinalActivity(ActivityState theActivity);
	public Transition getFinalTransition ();
	
}
