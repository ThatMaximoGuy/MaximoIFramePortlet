package com.interlocsolutions.maximo.tools;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import org.jaxen.JaxenException;
import org.jaxen.jdom.JDOMXPath;
import org.jdom.Content;
import org.jdom.Document;
import org.jdom.Element;
import org.jdom.JDOMException;
import org.jdom.Namespace;
import org.jdom.input.SAXBuilder;
import org.jdom.output.Format;
import org.jdom.output.XMLOutputter;

import com.martiansoftware.jsap.FlaggedOption;
import com.martiansoftware.jsap.JSAP;
import com.martiansoftware.jsap.JSAPException;
import com.martiansoftware.jsap.JSAPResult;
import com.martiansoftware.jsap.Switch;

/**
 * Update Maximo configuration for the IFramePortlet.
 *
 * @author Martin Nichol
 *
 */
public class IFramePortletEnvSetup {

	/**
	 * Main method, executed from command line.
	 *
	 * @param args command line parameters.
	 */
	public static void main(String[] args) {
		JSAP jsap = new JSAP();
		FlaggedOption componentsXMLPath = (new FlaggedOption("componentspath")).setStringParser(JSAP.STRING_PARSER)
				.setDefault("../../applications/maximo/properties/components.xsd").setRequired(false).setLongFlag("componentspath");
		componentsXMLPath.setHelp("The path to the components.xsd file.");

		FlaggedOption componentsRegistryXMLPath = (new FlaggedOption("componentregistrypath")).setStringParser(JSAP.STRING_PARSER)
				.setDefault("../../applications/maximo/properties/component-registry.xml").setRequired(false)
				.setLongFlag("componentregistrypath");
		componentsXMLPath.setHelp("The path to the component-registry.xml file.");

		FlaggedOption controlRegistryXMLPath = (new FlaggedOption("controlregistrypath")).setStringParser(JSAP.STRING_PARSER)
				.setDefault("../../applications/maximo/properties/control-registry.xml").setRequired(false)
				.setLongFlag("controlregistrypath");
		componentsXMLPath.setHelp("The path to the control-registry.xml file.");

		FlaggedOption presentationXMLPath = (new FlaggedOption("presentationpath")).setStringParser(JSAP.STRING_PARSER)
				.setDefault("../../applications/maximo/properties/presentation.xsd").setRequired(false)
				.setLongFlag("presentationpath");
		componentsXMLPath.setHelp("The path to the presentation.xml file.");

		Switch helpSwitch = (new Switch("help")).setLongFlag("help").setShortFlag('h');
		helpSwitch.setHelp("Print this help information.");
		try {
			jsap.registerParameter(componentsXMLPath);
			jsap.registerParameter(componentsRegistryXMLPath);
			jsap.registerParameter(controlRegistryXMLPath);
			jsap.registerParameter(presentationXMLPath);
			jsap.registerParameter(helpSwitch);
			JSAPResult config = jsap.parse(args);
			IFramePortletEnvSetup ies = new IFramePortletEnvSetup();

			ies.updateComponentRegistry(config.getString("componentregistrypath"),
					IFramePortletEnvSetup.class.getClassLoader().getResourceAsStream("component-registry.fragment.xml"));
			ies.updateComponents(config.getString("componentspath"),
					IFramePortletEnvSetup.class.getClassLoader().getResourceAsStream("components.fragment.xsd"));
			ies.updateControlRegistry(config.getString("controlregistrypath"),
					IFramePortletEnvSetup.class.getClassLoader().getResourceAsStream("control-registry.fragment.xml"));
			ies.updatePresentation(config.getString("presentationpath"),
					IFramePortletEnvSetup.class.getClassLoader().getResourceAsStream("presentation.fragment.xsd"));
		} catch (JSAPException e) {
			System.err.println((new StringBuilder()).append("ERROR: ").append(e.getMessage()).toString());
			System.err.println(jsap.getHelp());
			System.exit(1);
		} catch (Exception e) {
			e.printStackTrace();
			System.err.println((new StringBuilder()).append("ERROR: ").append(e.getMessage()).toString());
			System.exit(2);
		}
	}


	/**
	 * Update the component-registry.xml file.
	 *
	 * @param componentRegistryFileName the filename of the file to update.
	 * @param fragmentFile the fragment to add to the component-registry file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	public void updateComponentRegistry(String componentRegistryFileName, InputStream fragmentFile)
			throws JDOMException, IOException, JaxenException
	{
		if (componentRegistryFileName == null) {
			System.err.println("Cannot update component-registry.xml.  Filename not provided.");
			return;
		}
		if (fragmentFile == null) {
			System.err.println("Cannot update component-registry.xml.  Fragment file not found.");
			return;
		}
		System.out.println("Processing " + componentRegistryFileName);

		Document componentRegistry = loadXmlFile(componentRegistryFileName);
		Document componentRegistryFragment = loadXmlFile(fragmentFile);

		splice(componentRegistry, (Element) componentRegistryFragment.getRootElement().detach(), "/component-registry",
				"//component-descriptor[@name=\"isiframeportlet\"]");

		saveXmlFile(componentRegistry, componentRegistryFileName);
	}


	/**
	 * Update the comonents.xsd file.
	 *
	 * @param componentsFileName the filename of the file to update.
	 * @param fragmentFile the fragment to add to the comonents.xsd file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	public void updateComponents(String componentsFileName, InputStream fragmentFile)
			throws JDOMException, IOException, JaxenException
	{
		if (componentsFileName == null) {
			System.err.println("Cannot update components.xsd.  Filename not provided.");
			return;
		}
		if (fragmentFile == null) {
			System.err.println("Cannot update components.xsd.  Fragment file not found.");
			return;
		}
		System.out.println("Processing " + componentsFileName);

		Document components = loadXmlFile(componentsFileName);
		Document componentsFragment = loadXmlFile(fragmentFile);
		Element fragment = (Element) componentsFragment.getRootElement().detach();
		splice(components, fragment, "/xsd:schema", "//xsd:element[@name=\"isiframeportlet\"]");

		Namespace xsd = Namespace.getNamespace("xsd", components.getRootElement().getNamespaceURI());
		Element element = new Element("element", xsd);
		element.setAttribute("ref", fragment.getAttributeValue("name"));

		splice(components, element, "//xsd:complexType[@name=\"componentListType\"]/xsd:choice",
				"//xsd:complexType[@name=\"componentListType\"]/xsd:choice/xsd:element[@ref=\"isiframeportlet\"]");
		saveXmlFile(components, componentsFileName);
	}


	/**
	 * Update the component-registry.xml file.
	 *
	 * @param controlRegistryFileName the filename of the file to update.
	 * @param fragmentFile the fragment to add to the component-registry.xml
	 *        file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	public void updateControlRegistry(String controlRegistryFileName, InputStream fragmentFile)
			throws JDOMException, IOException, JaxenException
	{
		if (controlRegistryFileName == null) {
			System.err.println("Cannot update component-registry.xml.  Filename not provided.");
			return;
		}
		if (fragmentFile == null) {
			System.err.println("Cannot update component-registry.xml.  Fragment file not found.");
			return;
		}
		System.out.println("Processing " + controlRegistryFileName);

		Document controlRegistry = loadXmlFile(controlRegistryFileName);
		Document controlRegistryFragment = loadXmlFile(fragmentFile);
		splice(controlRegistry, (Element) controlRegistryFragment.getRootElement().detach(), "/control-registry",
				"//control-descriptor[@name=\"isiframeportlet\"]");

		saveXmlFile(controlRegistry, controlRegistryFileName);
	}


	/**
	 * Update the presentation.xsd file.
	 *
	 * @param presentationFileName the filename of the file to update.
	 * @param fragmentFile the fragment to add to the presentation.xsd file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	public void updatePresentation(String presentationFileName, InputStream fragmentFile)
			throws JDOMException, IOException, JaxenException
	{
		if (presentationFileName == null) {
			System.err.println("Cannot update presentation.xsd.  Filename not provided.");
			return;
		}
		if (fragmentFile == null) {
			System.err.println("Cannot update presentation.xsd.  Fragment file not found.");
			return;
		}
		System.out.println("Processing " + presentationFileName);

		Document presentation = loadXmlFile(presentationFileName);
		Document presentationFragment = loadXmlFile(fragmentFile);
		Element fragment = (Element) presentationFragment.getRootElement().detach();

		splice(presentation, fragment, "/xsd:schema", "//xsd:element[@name=\"isiframeportlet\"]");

		Namespace xsd = Namespace.getNamespace("xsd", presentation.getRootElement().getNamespaceURI());
		Element element = new Element("element", xsd);
		element.setAttribute("ref", fragment.getAttributeValue("name"));
		splice(presentation, element, "//xsd:element[@name=\"startcenter-pane\"]/xsd:complexType/xsd:choice",
				"//xsd:element[@name=\"startcenter-pane\"]/xsd:complexType/xsd:choice/xsd:element[@ref=\"isiframeportlet\"]");

		saveXmlFile(presentation, presentationFileName);
	}


	/**
	 * Load an xml file from the file with the given name.
	 *
	 * @param filename the name of the file to load.
	 * @return a Document representing the xml file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 */
	private Document loadXmlFile(String filename) throws JDOMException, IOException {
		return loadXmlFile(new FileInputStream(filename));
	}


	/**
	 * Load an xml file from an input stream.
	 *
	 * @param istream the stream from which to load the xml file.
	 * @return a Document representing the xml file.
	 * @throws JDOMException if an XML problem occurs.
	 * @throws IOException if an IO problem occurs.
	 */
	private Document loadXmlFile(InputStream istream) throws JDOMException, IOException {
		SAXBuilder builder = new SAXBuilder();
		Document doc = builder.build(istream);
		return doc;
	}


	/**
	 * Save the given xml document to the named file.
	 *
	 * @param document the xml document to save.
	 * @param filename the file to save to.
	 * @throws IOException if an IO problem occurs.
	 */
	private void saveXmlFile(Document document, String filename) throws IOException {
		Format format = Format.getPrettyFormat();
		format.setLineSeparator("\n");
		XMLOutputter out = new XMLOutputter(format);
		out.output(document, new FileOutputStream(filename));
	}


	/**
	 * Find content matching the given XPath search string.
	 *
	 * @param document the document to search
	 * @param search the XPath search expression.
	 * @return a list of Content that matches the expression.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	@SuppressWarnings("rawtypes")
	private List findXPath(Document document, String search) throws JaxenException {
		JDOMXPath xpath = new JDOMXPath(search);
		xpath.addNamespace("xsd", document.getRootElement().getNamespaceURI());

		List nodes = xpath.selectNodes(document);
		return nodes;
	}


	/**
	 * Splace a fragment into the document at the insertion point. Remove any
	 * duplicates first.
	 *
	 * @param document the document to insert into.
	 * @param fragment the xml fragment to insert.
	 * @param insertion the parent node underwhich to insert the fragment.
	 * @param check an expression for finding duplicates of fragment to be
	 *        deleted.
	 * @throws JaxenException if an XPath problem occurs.
	 */
	@SuppressWarnings("rawtypes")
	private void splice(Document document, Element fragment, String insertion, String check) throws JaxenException {
		List duplicates = findXPath(document, check);
		Iterator i = duplicates.iterator();
		while (i.hasNext()) {
			System.out.println("   Removing duplicate node");
			Content c = (Content) i.next();
			c.detach();
		}

		List parent = findXPath(document, insertion);
		if (parent.size() == 0) {
			System.err.println("Unable to find insertion node: " + insertion);
		}
		Element e = (Element) parent.get(0);
		e.addContent(0, fragment);
	}

}
