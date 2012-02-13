package com.nofatclips.androidtesting.guitree;
import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.util.Iterator;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.*;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

import com.nofatclips.androidtesting.model.ActivityState;
import com.nofatclips.androidtesting.model.Session;
import com.nofatclips.androidtesting.model.Trace;
import com.nofatclips.androidtesting.Testable;
import com.nofatclips.androidtesting.junit.TestCaseFromSession;
import com.nofatclips.androidtesting.xml.NodeListWrapper;
import com.nofatclips.androidtesting.xml.XmlGraph;

public class GuiTree extends XmlGraph implements Session, Testable {
		
	public GuiTree () throws ParserConfigurationException {
		super ("guitree.dtd", TAG);
		this.guiTree = getBuilder().newDocument();
		Element rootElement = this.guiTree.createElement(TAG);
		this.guiTree.appendChild(rootElement);
	}
	
	@Override
	public Document getDom() {
		return this.guiTree;
	}
	
	public void parse(File f) throws SAXException, IOException, ParserConfigurationException {
		this.guiTree = getBuilder().parse(f);
	}

	public void parse(String xml) {
		try {
			this.guiTree = getBuilder().parse((new InputSource(new StringReader(xml))));
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	// Sets attributes for the whole graph (not the nodes!) They are stored as attributes of the root node
	public void setAttribute (String key, String value) {
		this.getDom().getDocumentElement().setAttribute(key, value);
	}
	
	public String getDateTime () {
		return this.getDom().getDocumentElement().getAttribute("date_time");
	}
	
	public void setDateTime (String d) {
		getDom().getDocumentElement().setAttribute("date_time", d);
	}

	public String getAppName () {
		return this.getDom().getDocumentElement().getAttribute("app");
	}

	public void setAppName (String n) {
		getDom().getDocumentElement().setAttribute("app",n);
	}

	public String getSleepAfterEvent () {
		if (!getDom().getDocumentElement().hasAttribute("event_sleep")) return "";
		return this.getDom().getDocumentElement().getAttribute("event_sleep");
	}

	public void setSleepAfterEvent (int millis) {
		setSleepAfterEvent(String.valueOf(millis));
	}
	
	public void setSleepAfterEvent (String millis) {
		getDom().getDocumentElement().setAttribute("event_sleep",millis);
	}

	public String getSleepAfterRestart () {
		if (!getDom().getDocumentElement().hasAttribute("restart_sleep")) return "";
		return this.getDom().getDocumentElement().getAttribute("restart_sleep");
	}

	public void setSleepAfterRestart (int millis) {
		setSleepAfterRestart (String.valueOf(millis));
	}

	public void setSleepAfterRestart (String millis) {
		getDom().getDocumentElement().setAttribute("restart_sleep",millis);
	}

	public String getInAndOutFocus () {
		boolean hasClassName = getDom().getDocumentElement().hasAttribute("in_and_out_focus");
		return (hasClassName)?getDom().getDocumentElement().getAttribute("in_and_out_focus"):("false");
	}	

	public String getStateFileName () {
		if (!getDom().getDocumentElement().hasAttribute("state_file")) return "";
		return getDom().getDocumentElement().getAttribute("state_file");
	}

	public void setStateFileName (String file) {
		getDom().getDocumentElement().setAttribute("state_file",file);
	}

	public String getSleepOnThrobber () {
		if (!getDom().getDocumentElement().hasAttribute("throbber_sleep")) return "";
		return getDom().getDocumentElement().getAttribute("throbber_sleep");
	}
	
	public void setSleepOnThrobber (int millis) {
		setSleepOnThrobber (String.valueOf(millis));
	}

	public void setSleepOnThrobber (String millis) {
		getDom().getDocumentElement().setAttribute("throbber_sleep",millis);
	}

	public String getPackageName() {
		boolean hasClassName = getDom().getDocumentElement().hasAttribute("package_name");
		return (hasClassName)?getDom().getDocumentElement().getAttribute("package_name"):guessPackageName();
	}
	
	public String guessPackageName() {
		String s = getAppName();
		Pattern p = Pattern.compile("[a-zA-Z0-9_]+(\\.[a-zA-Z0-9_]+)+");
		Matcher m = p.matcher(s);
		return (m.find())?m.group():null;
	}

	public String getClassName() {
		boolean hasClassName = getDom().getDocumentElement().hasAttribute("class_name");
		return (hasClassName)?getDom().getDocumentElement().getAttribute("class_name"):guessClassName();
	}
	
	public String guessClassName() {
		return getPackageName() + "." + getBaseActivity().getName();
	}
	
	public void setClassName(String cname) {
		getDom().getDocumentElement().setAttribute("class_name", cname);
	}

	public void setPackageName(String pname) {
		getDom().getDocumentElement().setAttribute("package_name", pname);
	}
	
	public void setInAndOutFocus(String val) {
		getDom().getDocumentElement().setAttribute("in_and_out_focus", val);
	}	

	public ActivityState getBaseActivity () {
		return traces().next().transitions().next().getStartActivity();
	}
	
	public static GuiTree fromXml (File f) throws ParserConfigurationException, SAXException, IOException {
		GuiTree g = new GuiTree();
		g.parse(f);
		return g;
	}

	public String getJUnit() {
		Testable t = new TestCaseFromSession(this);
		return t.getJUnit();
	}
	
	public void addTrace (Trace t) {
		getDom().getDocumentElement().appendChild(t.getElement());
	}
	
	public void addCrashedTrace (Trace t) {
		addFailedTrace (t,"crash");
	}

	public void addFailedTrace (Trace t) {
		addFailedTrace (t,"fail");
	}
	
	protected void addFailedTrace (Trace t, String failType) {
		t.setFailed(true);
		FinalActivity fail = FinalActivity.createActivity(this);
		fail.setName(failType);
		fail.setId(failType);
		fail.setTitle(failType);
		t.getFinalTransition().setFinalActivity(fail);
		addTrace(t);
	}

	public void removeTrace (Trace t) {
		getDom().getDocumentElement().removeChild(t.getElement());
	}

	// Iterator Methods
	
	public Iterator<Trace> traces() {
		Element session = getDom().getDocumentElement();
		if (session.getNodeName().equals(TAG)) {
			return new NodeListWrapper<Trace> (session, new TestCaseTrace());
		}
		return null;		
	}
	
	public Iterator<Trace> iterator() {
		return traces();
	}
	
	private Document guiTree;
	public final static String TAG = "SESSION";
	
}
