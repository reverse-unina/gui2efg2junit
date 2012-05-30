package com.nofatclips.androidtesting.stats;

import java.util.*;

import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Element;

import com.nofatclips.androidtesting.ActivityMap;
import com.nofatclips.androidtesting.efg.EventFlowTree;
import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.model.*;
import com.nofatclips.androidtesting.xml.NodeListIterator;

import static com.nofatclips.androidtesting.model.SimpleType.*;

public class ReportGenerator extends StatsReport {
	
	private GuiTree session;
	private EventFlowTree efg;
	private ActivityMap activities;
	
	private TraceStats traceReport = new TraceStats();
	private InteractionStats eventReport = new InteractionStats();
	
	private int transitions = 0;
	private Set<String> activity;
	private Set<String> activityStates;

	private int widgetCount = 0;
	private int widgetSupport = 0;
	private Map<String,Integer> widgetTypes;
	private Map<String,Integer> widgets;
	private Map<String,Integer> widgetStates;
	
	// Data inferred from the Event Flow Tree
	private int actualTraces=0;
	private int actualTransitions=0;
	
	public ReportGenerator(GuiTree guiTree) {
		this (guiTree,null);
	}

	public ReportGenerator(GuiTree guiTree, EventFlowTree efg) {
		this.session = guiTree;
		this.activity = new HashSet<String>();
		this.activityStates = new HashSet<String>();
		this.widgetTypes = new Hashtable<String, Integer>();
		this.widgets = new Hashtable<String,Integer>();
		this.widgetStates = new Hashtable<String,Integer>();
		if (efg!=null) {
			this.efg=efg;
		} else {
			try {
				this.efg=EventFlowTree.fromSession(guiTree);
			} catch (ParserConfigurationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	public ReportGenerator(GuiTree guiTree, EventFlowTree efg, ActivityMap map) {
		this (guiTree, efg);
		this.activities = map;
	}

	public void evaluate() {
		
		for (Trace theTrace: this.session) {
			// Trace count
			traceReport.analyzeTrace(theTrace);

			boolean first = true;
			for (Transition step: theTrace) {				
				
				// Transition count
				this.transitions++;

				// Events and input count
				eventReport.analyzeInteractions(step);
				
				// Widgets count
				if (first) {
					countWidgets(step.getStartActivity());
				}
				countWidgets(step.getFinalActivity());
				first = false;

			}
		}
		
		countLeaves();

	}
	
	private void countLeaves() {
		Element efg = (Element) this.efg.getDom().getChildNodes().item(0);
		this.countLeaves (efg,0);
	}
	
	private void countLeaves(Element evento, int depth) {
		boolean atLeastOneChild = false;
		for (Element e: new NodeListIterator (evento)) {
			atLeastOneChild = true;
			this.countLeaves(e,depth+1);
		}
		if (!atLeastOneChild) {
			this.actualTraces++;
			this.actualTransitions+=depth;
		}
	}

	public String getReport () {
		evaluate();
		return this.traceReport + NEW_LINE +
				"Actual traces: " + this.actualTraces + " (for " + this.actualTransitions + " transitions)" + 
				BREAK +
				"Transitions: " + this.transitions + NEW_LINE + 
				TAB + "different activity states found: " + this.activityStates.size() + NEW_LINE + 
				TAB + "different activities found: " + this.activity.size() +
				BREAK +
				this.eventReport +
				BREAK + 
				"Views and widgets: " + this.widgetCount + NEW_LINE + 
				TAB + "supported widgets: " + this.widgetSupport + NEW_LINE + 
				expandMap(this.widgetTypes) +
				TAB + "different widgets: " + sum(this.widgets) + " <-> " + sum(this.widgetStates)
				;
	}
	
	public void countWidgets (ActivityState activity) {
		if (activity.isFailure() || activity.isCrash()) return;
		int localCount = 0;
		String key = activity.getName();
		String key2 = activity.getId();
		
		activity = getCompleteActivity(activity);
		
		for (WidgetState w: activity) {
			this.widgetCount++;
			localCount++;
			if (! (w.getSimpleType().equals("") || w.getSimpleType().equals(NULL)) ) {
				inc (this.widgetTypes, w.getSimpleType());
				this.widgetSupport++;
			}
		}
		this.activity.add(key);
		this.widgets.put(key, max(localCount,this.widgets.get(key)));
		this.activityStates.add(key2);
		this.widgetStates.put(key2, max(localCount,this.widgetStates.get(key2)));
	}

	private ActivityState getCompleteActivity (ActivityState state) {
		if ((this.activities == null) || (state.getDescriptionId().equals("")) || (!this.activities.hasActivity(state)) ) return state;
		return this.activities.getActivity(state);
	}

}
