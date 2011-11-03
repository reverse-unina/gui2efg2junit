package com.nofatclips.androidtesting;

import java.awt.EventQueue;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.*;
import java.util.regex.*;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import org.w3c.dom.Document;
import org.xml.sax.SAXException;

import com.nofatclips.androidtesting.efg.EventFlowTree;
import com.nofatclips.androidtesting.guitree.GuiTree;
import com.nofatclips.androidtesting.source.*;

import static com.nofatclips.androidtesting.Resources.*;

public class EventFlowModeler {

	public static void main(final String[] args) {
		EventQueue.invokeLater(new Runnable() {
			public void run() {
				String inputFileName = (args.length == 0) ? "" : args[0]; //"C:\\Users\\mm\\Desktop\\ING.SW\\XML\\gui_tree_dtd.xml"
				String outputFileName = (args.length > 1) ? args[1] : "";
				String packageName = (args.length > 2) ? args[2] : "";
				String efgFileName = (args.length > 3) ? args[3] : "";
//				String inputFileName = "C:\\Users\\mm\\Desktop\\opensudoku.xml";
//				String outputFileName = "C:\\Users\\mm\\Desktop\\opensudoku.java";
				Gui2EfcFrame frame = new Gui2EfcFrame(inputFileName, outputFileName, packageName, efgFileName);
				frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
				if (frame.isWindowed()) {
					frame.setVisible(true);
				} else {
					frame.processFile(inputFileName);
				}
		   }
		});
	}
}

class Gui2EfcFrame extends JFrame  {

	@SuppressWarnings("serial")
	public Gui2EfcFrame (String inputFileName, String outputFileName, String packageName, String efgFileName) {
		super();
		this.inputFileName = inputFileName;
		this.outputFileName = outputFileName;
		this.packageName = packageName;
		this.efgFileName = efgFileName;
		this.windowed = ((outputFileName.equals("")) || (inputFileName.equals("")));
		this.someLogger = Logger.getLogger(LOGGER);
		this.setSize(DEFAULT_WIDTH, DEFAULT_HEIGHT);
		
		JSourceCodeArea xmlInput = new JSourceCodeArea();
		xmlInput.setEditable(false);
		debugPane = xmlInput; // TODO: Comment this before releasing
		
		JPopupMenu m = xmlInput.getComponentPopupMenu();
		m.add (new AbstractAction("Open") {
			public void actionPerformed(ActionEvent e) {
				JFileChooser toLoad = new JFileChooser ();
				toLoad.setCurrentDirectory(new File ("."));
				toLoad.setFileFilter(new FileNameExtensionFilter(GUI_TREE_FILE_TYPE, "xml"));
				int code = toLoad.showOpenDialog (Gui2EfcFrame.this.getParent());
				if (code == JFileChooser.APPROVE_OPTION) {
					File theFile = toLoad.getSelectedFile();
					processFile (theFile);
					someLogger.fine(theFile.toString());
				}
			}
		});

		JSourceCodeArea xmlCode = new JSourceCodeArea();
		JSourceCodeArea dotCode = new JSourceCodeArea();
		dotCode.setDefaultExtension("dot");
		JSourceCodeArea jUnitCode = new JSourceCodeArea();
		jUnitCode.setDefaultExtension("java");
		if (!outputFileName.equals("")) {
			jUnitCode.setDefaultFileName(outputFileName);
		}

		schermo = new JSourceCodePane();
		schermo.addEnabled(GUI_TREE, null, xmlInput);
		schermo.addDisabled(EFG_XML, null, xmlCode);
		schermo.addDisabled(EFG_DOT, null, dotCode);
		schermo.addDisabled(JUNIT_JAVA, null, jUnitCode);

		this.add(schermo);
		this.someLogger.setLevel(Level.FINER);
		
		if (inputFileName != "") { 
			processFile(inputFileName);
		}
	}

	public void processFile(String filename) {
		setFilename(filename);
		processFile (new File (filename));
	}
	
	private void processFile(File file) {
		setFilename (file.getAbsolutePath());
		this.someLogger.finest(getFilename());
		try {
			this.guiTree = GuiTree.fromXml(file);
			this.showInputXml(this.guiTree.getDom());
			this.efg = EventFlowTree.fromSession(this.guiTree);
			showEfg();
			showDot(); // Can't show dot if efg failed
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();		
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		showTest(); // Can try to show test even if efg failed
	}
	
	public boolean isWindowed () {
		return this.windowed;
	}
	
	public String getFilename() {
		return this.inputFileName;
	}

	public void setFilename(String filename) {
		this.inputFileName = filename;
		this.setTitle(filename);
	}

	private void showInputXml(final Document doc) {
		String input="";
		try {
			input = this.guiTree.toXml();
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (TransformerException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		schermo.showCode(GUI_TREE, input);
	}

	public boolean showEfg() throws IOException, TransformerException {
		String xml="";
		try {
			xml = this.efg.toXml();
			// this.someLogger.info(xml);
		} catch (TransformerFactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		if (this.efgFileName.equals("")) {
			Pattern p = Pattern.compile("(.+)\\.[^.]+");
			Matcher m = p.matcher(inputFileName);
			if (m.find()) {
				schermo.setFileName(EFG_XML, m.group(1)+"_efg");
			}	
		} else {
			schermo.setFileName(EFG_XML, this.efgFileName);
		}

		if (isWindowed()) {
			schermo.showCode(EFG_XML, xml);
		} else {
			PrintWriter autput;
			try {
				autput = new PrintWriter (this.efgFileName);
				autput.println(xml);
				autput.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return true;
	}

	private void showDot() {
		Pattern p = Pattern.compile("(.+)\\.[^.]+");
		Matcher m = p.matcher(inputFileName);
		if (m.find()) {
			schermo.setFileName(EFG_DOT, m.group(1));
		}	
		schermo.showCode(EFG_DOT, this.efg.getDot());
	}

	private void showTest() {
		String code = this.guiTree.getJUnit();
		if (this.outputFileName.equals("")) {
			Pattern p = Pattern.compile("public class ([^ ]+) ");
			Matcher m = p.matcher(code);
			if (m.find()) {
				schermo.setFileName(JUNIT_JAVA, m.group(1));
			}
		} else {
			schermo.setFileName(JUNIT_JAVA, this.outputFileName);
		}
		if (isWindowed()) {
			schermo.showCode(JUNIT_JAVA, code);
		} else {
			if (!this.packageName.equals("")) {
				code = code.replaceAll("//package your.package.here;", "package " + this.packageName);
			}
			PrintWriter autput;
			try {
				autput = new PrintWriter (this.outputFileName);
				autput.println(code);
				autput.close();
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
	}
	
	private String inputFileName;
	private String outputFileName;
	private String packageName;
	private String efgFileName;
	private boolean windowed;
	private GuiTree guiTree;
	private EventFlowTree efg;
	private Logger someLogger;
	private JSourceCodePane schermo;
	private static final long serialVersionUID = 1L;

}