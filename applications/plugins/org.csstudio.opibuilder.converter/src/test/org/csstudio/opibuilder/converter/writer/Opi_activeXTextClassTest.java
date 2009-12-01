package org.csstudio.opibuilder.converter.writer;

import org.w3c.dom.Document;
import org.w3c.dom.Element;

import org.csstudio.opibuilder.converter.model.EdmColor;
import org.csstudio.opibuilder.converter.model.EdmDisplay;
import org.csstudio.opibuilder.converter.model.EdmException;
import org.csstudio.opibuilder.converter.model.EdmModel;
import org.csstudio.opibuilder.converter.model.Edm_activeXTextClass;
import junit.framework.TestCase;

public class Opi_activeXTextClassTest extends TestCase {

	public void testOpi_activeXTextClass() throws EdmException {

		System.setProperty("edm2xml.robustParsing", "false");
		System.setProperty("edm2xml.colorsFile", "src/test/resources/colors.list");

		Document doc = XMLFileHandler.createDomDocument();
		Element root = doc.createElement("root");
		doc.appendChild(root);

		String edlFile = "src/test/resources/EDMDisplayParser_example.edl";
		EdmModel.getInstance();
		EdmDisplay d = new EdmDisplay(EdmModel.getDisplay(edlFile));
		Edm_activeXTextClass t = new Edm_activeXTextClass(d.getSubEntity(6));

		Opi_activeXTextClass o = new Opi_activeXTextClass(doc, root, t);
		assertTrue(o instanceof OpiWidget);

		Element e = (Element)doc.getElementsByTagName("widget").item(0);
		assertEquals("org.csstudio.opibuilder.widgets.Label", e.getAttribute("typeId"));
		assertEquals("1.0", e.getAttribute("version"));

		XMLFileHandler.isElementEqual("EDM Label", "name", e);
		XMLFileHandler.isElementEqual("123", "x", e);
		XMLFileHandler.isElementEqual("50", "y", e);
		XMLFileHandler.isElementEqual("42", "width", e);
		XMLFileHandler.isElementEqual("13", "height", e);

		XMLFileHandler.isFontElementEqual("helvetica-bold-r-12.0", "font", e);

		XMLFileHandler.isColorElementEqual(new EdmColor(10), "color_foreground", e);
		XMLFileHandler.isColorElementEqual(new EdmColor(3), "color_background", e);

		XMLFileHandler.isElementEqual("At low", "text", e);
		XMLFileHandler.isElementEqual("true", "auto_size", e);

		XMLFileHandler.isElementEqual("1", "border_style", e);
		XMLFileHandler.isElementEqual("2", "border_width", e);
		XMLFileHandler.isElementEqual("true", "transparency", e);
		
		XMLFileHandler.writeXML(doc);
	}
}
