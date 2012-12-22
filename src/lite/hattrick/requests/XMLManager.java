package lite.hattrick.requests;

import java.io.File;
import java.io.InputStream;
import java.io.StringWriter;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;


/**
 * DOCUMENT ME!
 *
 * @author thomas.werth
 */
public class XMLManager  {

	/**
	 * Creates a new instance of XMLManager
	 */
	private XMLManager() {
	}

	/**
	 * liefert den Value des attributes sonst ""
	 */
	public static String getAttributeValue(Element ele, String attributeName) {
		try {
			if ((ele != null) && (attributeName != null)) {
				return ele.getAttribute(attributeName);
			}
		} catch (Exception e) {
			return "";
		}

		return "";
	}

	/////////////////////////////////////////////////////////////////////////////////
	//Helper
	///////////////////////////////////////////////////////////////////////////////


	public static String getFirstChildNodeValue(Element ele) {
		try {
			if (ele.getFirstChild() != null) {
				return ele.getFirstChild().getNodeValue();
			}
		} catch (Exception e) {
		}

		return "";
	}

	public static Document parseFile(String dateiname) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;

		try {
			//Validierung, Namensräume einschalten
			//factory.setValidating ( false );
			//factory.setNamespaceAware ( true );
			builder = factory.newDocumentBuilder();

			doc = builder.parse(new File(dateiname));
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * Parse XML from input stream.
	 */
	public static Document parseFile(InputStream xmlStream) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;

		try {
			builder = factory.newDocumentBuilder();
			doc = builder.parse(xmlStream);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	/**
	 * Parse XML fro file.
	 */
	public static Document parseFile(File datei) {
		final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
		DocumentBuilder builder = null;
		Document doc = null;

		try {
			//Validierung, Namensräume einschalten
			//factory.setValidating ( false );
			//factory.setNamespaceAware ( true );
			builder = factory.newDocumentBuilder();

			doc = builder.parse(datei);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static Document parseString(String inputString) {
		//Fix to remove commented tag
		int indexComm = inputString.indexOf("<!--");

		while (indexComm > -1) {
			final int endComm = inputString.indexOf("-->");
			final String comment = inputString.substring(indexComm, endComm + 3);
			inputString = inputString.replaceAll(comment, "");
			indexComm = inputString.indexOf("<!--");
		}

		Document doc = null;

		try {
			final java.io.ByteArrayInputStream input = new java.io.ByteArrayInputStream(inputString.getBytes("UTF-8"));
			final DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
			DocumentBuilder builder = null;

			builder = factory.newDocumentBuilder();

			doc = builder.parse(input);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return doc;
	}

	public static void writeXML(Document doc, String filename) {
		//Transformer creation for DOM rewriting into XML file
		Transformer serializer = null;
		DOMSource source = null;
		File file = null;
		StreamResult result = null;

		try {
			//You can do any modification to the document here
			serializer = TransformerFactory.newInstance().newTransformer();
			source = new DOMSource(doc);
			file = new File(filename);
			result = new StreamResult(file);

			serializer.transform(source, result);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/**
	 * Parse the league table.
	 */
	protected void parseTabelle(Document doc) {
		Element ele = null;
		Element tmpEle = null;
		String titel = "";

		ele = doc.getDocumentElement();

		tmpEle = (Element) ele.getElementsByTagName("LeagueName").item(0);

		titel = " " + ele.getElementsByTagName("LeagueName").getLength();
		titel += (" " + ele.getElementsByTagName("LeagueName").item(0).getNodeType());
		titel += (" "
				+ ele.getElementsByTagName("LeagueLevelUnitName").item(0).getAttributes().getLength());
	}

	public static String getXML(Document doc) {
		//Transformer creation for DOM rewriting into XML String
		Transformer serializer = null;
		DOMSource source = null;
		StreamResult result = null;
		String xml = "";
		try {
			//You can do any modification to the document here
			serializer = TransformerFactory.newInstance().newTransformer();
			source = new DOMSource(doc);
			StringWriter sw = new StringWriter();
			result = new StreamResult(sw);
			serializer.transform(source, result);
			xml = sw.toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return xml;
	}
}
