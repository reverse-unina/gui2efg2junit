package com.nofatclips.androidtesting.model;

//import org.w3c.dom.Element;

public interface Transition extends Iterable<UserInput>, WrapperInterface {

	public String getId();
	public void setId (String id);
	public ActivityState getStartActivity ();
	public void setStartActivity (ActivityState theActivity);
	public void addInput (UserInput theInput);
	public UserEvent getEvent ();
	public void setEvent (UserEvent theEvent);
	public ActivityState getFinalActivity ();
	public void setFinalActivity (ActivityState theActivity);
		
}