package com.nofatclips.androidtesting.source;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;

import javax.swing.*;
import javax.swing.filechooser.FileNameExtensionFilter;

@SuppressWarnings("serial")
public class JSourceCodeArea extends JTextArea {

	public JSourceCodeArea () {
		super();
		setTabSize(3);
		JPopupMenu m = new JPopupMenu();
		m.add(new SaveAction());
		this.setComponentPopupMenu(m);
	}

	public String getDefaultExtension() {
		return defaultExtension;
	}

	public void setDefaultExtension(String defaultExtension) {
		this.defaultExtension = defaultExtension;
	}

	public String getDefaultPath() {
		return defaultPath;
	}

	public void setDefaultPath(String defaultPath) {
		this.defaultPath = defaultPath;
	}

	public String getDefaultFileName() {
		return defaultFileName;
	}

	public void setDefaultFileName(String defaultFileName) {
		this.defaultFileName = defaultFileName;
	}
	
	public String getDefaultFilePath () {
		return this.getDefaultPath() + "//" + this.getDefaultFileName() + "." + this.getDefaultExtension();
	}
	
	public void initText (String text) {
		setText (text);
		setCaretPosition(0);
	}

	private String defaultPath = ".";
	private String defaultFileName = "untitled";
	private String defaultExtension = "xml";

	public class SaveAction extends AbstractAction {

		public SaveAction() {
			super();
			putValue (Action.NAME, "Save");
		}

		public SaveAction(String name, Icon icon) {
			super(name, icon);
		}

		public SaveAction(String name) {
			super(name);
		}

		public void actionPerformed(ActionEvent e) {
			JFileChooser toSave = new JFileChooser ();
			toSave.setCurrentDirectory(new File (JSourceCodeArea.this.getDefaultPath()));
			toSave.setSelectedFile(new File (JSourceCodeArea.this.getDefaultFilePath ()));
			toSave.setFileFilter(new FileNameExtensionFilter("Files", JSourceCodeArea.this.getDefaultExtension()));
			int code = toSave.showSaveDialog(JSourceCodeArea.this.getParent());
			File fileName = toSave.getSelectedFile();
			try {
				if (code == JFileChooser.APPROVE_OPTION)
					saveFile (fileName);
			} catch (FileNotFoundException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}			
		}

		private void saveFile(File fileName) throws FileNotFoundException {
			PrintWriter autput = new PrintWriter (fileName);
			String text = JSourceCodeArea.this.getText();
			System.out.println(text);
			autput.println(text);
			autput.close();
		}
		
	}
	
}
