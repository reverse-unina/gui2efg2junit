package com.nofatclips.androidtesting.xml;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.*;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.xml.sax.EntityResolver;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;

public abstract class XmlGraph {

	public abstract Document getDom ();

	public XmlGraph () {
		this ("","");
	}
	
	public XmlGraph (String systemId, String publicId) {
		this.setDoctype(systemId, publicId);
	}
	
	public void setDoctype (String systemId, String publicId) {
		this.doctype_public = publicId;
		this.doctype_system = systemId;
	}

	public String toXml () throws TransformerFactoryConfigurationError, TransformerException {
		DOMSource theDom = new DOMSource(this.getDom());
		StringWriter autput = new StringWriter();
		Transformer t = TransformerFactory.newInstance().newTransformer();
		t.setOutputProperty(OutputKeys.DOCTYPE_SYSTEM, this.doctype_system);
		t.setOutputProperty(OutputKeys.DOCTYPE_PUBLIC, this.doctype_public);
		t.setOutputProperty(OutputKeys.INDENT, this.indent);
		t.setOutputProperty(OutputKeys.METHOD, this.method);
		t.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "" + indent_amount);
		t.transform(theDom, new StreamResult(autput));
		return autput.toString();
	}
	
	static protected DocumentBuilder getBuilder () throws ParserConfigurationException {
		if (builder instanceof DocumentBuilder)
			return builder;
		DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		//this.someLogger.setLevel(Level.INFO);
		try {
			if (isValidation()) {
				factory.setValidating(true);
				factory.setIgnoringElementContentWhitespace(true);
			}
			
			builder = factory.newDocumentBuilder();
			builder.setEntityResolver(new EntityResolver (){
				public InputSource resolveEntity(String publicId, String systemId) throws SAXException, IOException {
					//someLogger.fine(publicId + '-' + systemId);
					String dtdFileName = systemId.substring(systemId.lastIndexOf("/")+1);
					InputStream u = XmlGraph.class.getClassLoader().getResourceAsStream("ext/"+dtdFileName);
					return (u == null)?null:new InputSource (u);
				}
			});
			
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			throw e;
		}
		return builder;
	}
	
	public static boolean isValidation() {
		return validation;
	}

	public static void setValidation(boolean v) {
		validation = v;
	}

	protected String doctype_system = "";
	protected String doctype_public = "";
	protected String indent = "yes";
	protected int indent_amount = 4;
	protected String method = "xml";
	static boolean validation = true;
	static private DocumentBuilder builder = null;
}
