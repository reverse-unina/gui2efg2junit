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
	private int tracesAsync = 0;
	private List<String> crashes;
	private List<String> failures;
	
	public TraceStats() {
		crashes = new ArrayList<String>();
		failures = new ArrayList<String>();
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

	public int getTracesCrashed() {
		return tracesCrashed;
	}

	public int getTracesAsync() {
		return tracesAsync;
	}

	public String getReport() {
		return "Traces processed: " + getTraces() + NEW_LINE + 
				TAB + "success: " + getTracesSuccessful() + NEW_LINE + 
				TAB + "fail: " + getTracesFailed() + NEW_LINE + 
				((this.failures.size()>0)?(TAB + TAB + "traces: " + expandList(this.failures) + NEW_LINE):"") +
				TAB + "crash: " + getTracesCrashed() + NEW_LINE +
				((this.crashes.size()>0)?(TAB + TAB + "traces: " + expandList(this.crashes) + NEW_LINE):"") +
				"Non repeatable issues:" + getTracesAsync();
	}

}
