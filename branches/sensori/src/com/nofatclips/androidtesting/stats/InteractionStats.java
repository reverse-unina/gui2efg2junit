package com.nofatclips.androidtesting.stats;

import java.util.HashSet;
import java.util.Set;

import com.nofatclips.androidtesting.model.Transition;
import com.nofatclips.androidtesting.model.UserInput;

public class InteractionStats extends StatsReport {
	
	private int eventCount = 0;
	private Set<String> events;
	private int inputCount = 0;
	private Set<String> inputs;
	
	public InteractionStats () {
		this.events = new HashSet<String>();
		this.inputs = new HashSet<String>();
	}
	
	public void analyzeInteractions(Transition step) {
		addEvent(step.getEvent().getId());
		this.eventCount++;
		for (UserInput i: step) {
			addInput(i.getId());
			this.inputCount++;
		}
	}

	public Set<String> getEvents() {
		return events;
	}

	public void addEvent(String event) {
		this.events.add(event);
	}

	public Set<String> getInputs() {
		return inputs;
	}

	public void addInput(String input) {
		this.inputs.add(input);
	}

	public int getEventCount() {
		return eventCount;
	}

	public int getInputCount() {
		return inputCount;
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
				TAB + "inputs: " + getInputCount() + NEW_LINE +
				TAB + "different inputs: " + getDifferentInputs(); 

	}

}
