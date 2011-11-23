package com.nofatclips.androidtesting;

import java.util.Collection;
import java.util.HashSet;
import java.util.Hashtable;
import java.util.Map;
import java.util.Set;

import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.*;
import static com.nofatclips.androidtesting.model.SimpleType.*;

public class ReportGenerator {
	
	private GuiTree session;
	public final static String NEW_LINE = System.getProperty("line.separator");
	public final static String BREAK = NEW_LINE + NEW_LINE;
	public static final String TAB = "\t";

	private int traces = 0;
	private int tracesSuccessful = 0;
	private int tracesFailed = 0;
	private int tracesCrashed = 0;
	
	private int transitions = 0;
	private Set<String> activity ;
	private Set<String> activityStates;
	
	private int eventCount = 0;
	private Set<String> events;
	private int inputCount = 0;
	private Set<String> inputs;

	private int widgetCount = 0;
	private int widgetSupport = 0;
	private Map<String,Integer> widgetTypes;
	private Map<String,Integer> widgets;
	private Map<String,Integer> widgetStates;
	
	public ReportGenerator(GuiTree guiTree) {
		this.session = guiTree;
		this.activity = new HashSet<String>();
		this.activityStates = new HashSet<String>();
		this.events = new HashSet<String>();
		this.inputs = new HashSet<String>();
		this.widgetTypes = new Hashtable<String, Integer>();
		this.widgets = new Hashtable<String,Integer>();
		this.widgetStates = new Hashtable<String,Integer>();
	}

	public void evaluate() {
		String s;
		for (Trace theTrace: this.session) {
			
			// Trace count
			this.traces++;
			s = theTrace.getFinalTransition().getFinalActivity().getId();
			if (s.equals("fail")) this.tracesFailed++;
			else if (s.equals("crash")) this.tracesCrashed++;
			else this.tracesSuccessful++;
			
			boolean first = true;
			for (Transition step: theTrace) {
				
				// Transition count
				this.transitions++;
				
				// Activity and states count
				this.activity.add(step.getStartActivity().getName());
				this.activityStates.add(step.getStartActivity().getId());
				if (!(step.getFinalActivity().getId().equals("fail") || step.getFinalActivity().getId().equals("crash"))) {
					this.activity.add(step.getFinalActivity().getName());
					this.activityStates.add(step.getFinalActivity().getId());
				}
				
				// Events and input count
				this.events.add(step.getEvent().getId());
				this.eventCount++;
				for (UserInput i: step) {
					this.inputs.add(i.getId());
					this.inputCount++;
				}
				
				// Widgets count
				if (first) {
					int localCount = 0;
					String key = step.getStartActivity().getName();
					String key2 = step.getStartActivity().getId();
					for (WidgetState w: step.getStartActivity()) {
						this.widgetCount++;
						localCount++;
						if (! (w.getSimpleType().equals("") || w.getSimpleType().equals(NULL)) ) {
							inc (this.widgetTypes, w.getSimpleType());
							this.widgetSupport++;
						}
					}
					this.widgets.put(key, max(localCount,this.widgets.get(key)));
					this.widgetStates.put(key2, max(localCount,this.widgetStates.get(key2)));
				}
				if (!(step.getFinalActivity().getId().equals("fail") || step.getFinalActivity().getId().equals("crash"))) {
					int localCount2 = 0;
					String key = step.getFinalActivity().getName();
					String key2 = step.getFinalActivity().getId();
					for (WidgetState w: step.getFinalActivity()) {
						this.widgetCount++;
						localCount2++;
						if (! (w.getSimpleType().equals("") || w.getSimpleType().equals(NULL)) ) {
							inc (this.widgetTypes, w.getSimpleType());
							this.widgetSupport++;
						}
					}
					this.widgets.put(key, max(localCount2,this.widgets.get(key)));
					this.widgetStates.put(key2, max(localCount2,this.widgetStates.get(key2)));
				}

				first = false;

			}
		}
	}
	
	public String getReport () {
		evaluate();
		return "Traces: " + this.traces + NEW_LINE + 
				TAB + "success: " + this.tracesSuccessful + NEW_LINE + 
				TAB + "fail: " + this.tracesFailed + NEW_LINE + 
				TAB + "crash: " + this.tracesCrashed +
				BREAK +
				"Transitions: " + this.transitions + NEW_LINE + 
				TAB + "different activity states found: " + this.activityStates.size() + NEW_LINE + 
				TAB + "different activities found: " + this.activity.size() +
				BREAK +
				"Interactions: " + (this.eventCount+this.inputCount) + NEW_LINE +
				TAB + "events: " + this.eventCount + NEW_LINE +
				TAB + "different events: " + this.events.size() + NEW_LINE +
				TAB + "inputs: " + this.inputCount + NEW_LINE +
				TAB + "different inputs: " + this.inputs.size() + 
				BREAK + 
				"Views and widget: " + this.widgetCount + NEW_LINE + 
				TAB + "supported widgets: " + this.widgetSupport + NEW_LINE + 
				expandMap(this.widgetTypes) +
				TAB + "different widgets: " + sum(this.widgets) + " <-> " + sum(this.widgetStates)
				;
	}
	
	public static int max (int a, Integer b) {
		if (b == null) return a;
		return Math.max(a, b);
	}
	
	public static int sum (Map<String,Integer> m) {
		return sum (m.values());
	}
	
	public static int sum (Collection<Integer> c) {
		int sum = 0;
		for (Integer i: c) {
			sum+=i;
		}
		return sum;
	}
	
	public static void inc (Map<String,Integer> table, String key) {
		if (table.containsKey(key)) {
			table.put(key, table.get(key)+1);
		} else {
			table.put(key, 1);
		}
	}
	
	public static String expandMap (Map<String,Integer> map) {
		StringBuilder s = new StringBuilder();
		for (Map.Entry<String,Integer> e:map.entrySet()) {
			s.append(TAB + TAB + e.getKey() + ": " + e.getValue() + NEW_LINE);
		}
		return s.toString();
	}

}
