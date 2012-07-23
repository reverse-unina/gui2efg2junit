package com.nofatclips.androidtesting.stats;

import java.util.ArrayList;
import java.util.List;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Trace;

public class TraceStats extends StatsReport {

	private int traces = 0;
	private int tracesSuccessful = 0;
	private int tracesFailed = 0;
	private int tracesCrashed = 0;
	private int tracesExit = 0;
	private int tracesAsync = 0;
	private List<String> crashes;
	private List<String> failures;
	private List<String> exits;
	
	public TraceStats() {
		crashes = new ArrayList<String>();
		failures = new ArrayList<String>();
		exits = new ArrayList<String>();
	}
	
	public void analyzeTrace (Trace theTrace) {
		this.traces++;
		ActivityState a = theTrace.getFinalTransition().getFinalActivity();
		if (a.isFailure()) {
			this.tracesFailed++;
			this.failures.add(theTrace.getId());
		} else if (a.isCrash()) {
			this.tracesCrashed++;
			this.crashes.add(theTrace.getId());
		} else if (a.isExit()) {
			this.tracesExit++;
			this.exits.add(theTrace.getId());
		} else {
			this.tracesSuccessful++;
		}
		if (theTrace.isAsync()) this.tracesAsync++;
	}

	public int getTraces() {
		return traces;
	}

	public int getTracesSuccessful() {
		return tracesSuccessful;
	}

	public int getTracesFailed() {
		return tracesFailed;
	}

	public int getTracesExit() {
		return tracesExit;
	}

	public int getTracesCrashed() {
		return tracesCrashed;
	}

	public int getTracesAsync() {
		return tracesAsync;
	}
	
	public String printList (List<String> list) {
		return ((list.size()>0)?(TAB + TAB + "traces: " + expandList(list) + NEW_LINE):"");
	}

	public String getReport() {
		return "Traces processed: " + getTraces() + NEW_LINE + 
				TAB + "success: " + getTracesSuccessful() + NEW_LINE + 
				TAB + "fail: " + getTracesFailed() + NEW_LINE + 
				printList (this.failures) +
				TAB + "crash: " + getTracesCrashed() + NEW_LINE +
				printList (this.crashes) +
				TAB + "exit: " + getTracesExit() + NEW_LINE + 
				printList (this.exits) +
				"Non repeatable issues:" + getTracesAsync();
	}

}
