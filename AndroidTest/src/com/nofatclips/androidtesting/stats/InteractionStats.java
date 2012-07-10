package com.nofatclips.androidtesting.stats;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserEvent;
import com.nofatclips.androidtesting.model.UserInput;

public class InteractionStats extends StatsReport {
	
	private int eventCount = 0;
	private Set<String> events;
	private Map<String,Integer> eventTypes;

	private int inputCount = 0;
	private Set<String> inputs;
	private Map<String,Integer> inputTypes;

	
	public InteractionStats () {
		this.events = new HashSet<String>();
		this.inputs = new HashSet<String>();
		this.eventTypes = new Hashtable<String, Integer>();
		this.inputTypes = new Hashtable<String, Integer>();
	}
	
	public void analyzeInteractions(Transition step) {
		addEvent(step.getEvent());
		this.eventCount++;
		for (UserInput i: step) {
			addInput(i);
			this.inputCount++;
		}
	}

	public Set<String> getEvents() {
		return this.events;
	}

	public void addEvent (UserEvent e) {
		if (!getEvents().contains(e.getId())) {
			inc (this.eventTypes, e.getType());
		}
		addEvent (e.getId());
	}

	public void addEvent(String event) {
		this.events.add(event);
	}
	
	public Set<String> getInputs() {
		return this.inputs;
	}
	
	public void addInput (UserInput i) {
		if (!getInputs().contains(i.getId())) {
			inc (this.inputTypes, i.getType());
		}
		addInput (i.getId());
	}

	public void addInput(String input) {
		this.inputs.add(input);
	}

	public int getEventCount() {
		return this.eventCount;
	}

	public int getInputCount() {
		return this.inputCount;
	}
	
	public int getInteractionCount() {
		return getEventCount() + getInputCount();
	}
	
	public int getDifferentEvents() {
		return getEvents().size();
	}

	public int getDifferentInputs() {
		return getInputs().size();
	}
	
	public int getDifferentInteractions() {
		return getDifferentEvents() + getDifferentInputs();
	}

	public String getReport() {
		return 	"Interactions: " + getInteractionCount() + NEW_LINE +
				TAB + "different interactions: " + getDifferentInteractions() + NEW_LINE +
				TAB + "events: " + getEventCount() + NEW_LINE +
				TAB + "different events: " + getDifferentEvents() + NEW_LINE +
				expandMap(this.eventTypes) +
				TAB + "inputs: " + getInputCount() + NEW_LINE +
				TAB + "different inputs: " + getDifferentInputs() + NEW_LINE +
				expandMap(this.inputTypes);

	}

}
