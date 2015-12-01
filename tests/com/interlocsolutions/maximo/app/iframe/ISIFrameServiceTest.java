package com.interlocsolutions.maximo.app.iframe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.rmi.RemoteException;
import java.util.HashMap;
import java.util.List;

import org.jdom.Element;
import org.junit.Before;
import org.junit.Test;

import com.interlocsolutions.maximo.app.scconfig.ISStartCenterLoader;
import com.interlocsolutions.maximo.junit.MaximoTestHarness;

import psdi.app.scconfig.LayoutRemote;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.security.UserInfo;
import psdi.server.MXServer;
import psdi.util.MXException;

/**
 * Test IFrameService class.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameServiceTest extends MaximoTestHarness {

	/** the service to use for tests. */
	private ISIFrameService service;


	/**
	 * Setup fields for the test.
	 *
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Before
	public void setupService() throws MXException, RemoteException {
		service = (ISIFrameService) MXServer.getMXServer().lookup("ISIFRAME");
	}


	/**
	 * Test default constructor.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testConstructor() throws RemoteException {
		ISIFrameService s = new ISIFrameService();
		assertNotNull(s);
	}

	/**
	 * Test the portletName method.
	 */
	@Test
	public void testPortletName() {
		assertEquals("ISIFRAME", service.portletName());

	}


	/**
	 * Test the xmlNodeName method.
	 */
	@Test
	public void testXmlNodeName() {
		assertEquals("isiframe", service.xmlNodeName());

	}


	/**
	 * Test the load portlet method. It should take an XML element and create
	 * and populate an ISIFRAMECFG row.
	 *
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testLoadPortlet() throws MXException, RemoteException {
		Element parent = new Element("layout");
		Element isiframeXml = new Element(service.xmlNodeName());
		isiframeXml.setAttribute("url", "JUNITURL");
		isiframeXml.setAttribute("sizey", "5");
		parent.addContent(isiframeXml);

		UserInfo ui = MXServer.getMXServer().getSystemUserInfo();
		HashMap<String, String> handlers = new HashMap<String, String>();
		handlers.put(service.xmlNodeName(), "ISIFRAME");

		MboSetRemote scconfigs = getMboSet("SCCONFIG");
		MboRemote scconfig = scconfigs.add();

		MboSetRemote layouts = scconfig.getMboSet("LAYOUTALL");
		LayoutRemote layout = (LayoutRemote) layouts.add();

		ISStartCenterLoader loader = new ISStartCenterLoader(ui, handlers);

		service.loadPortlet(parent, loader, layout);

		MboSetRemote msr = layout.getMboSet("ISIFRAMECFG");
		assertEquals(1, msr.count());

		MboRemote mr = msr.getMbo(0);
		assertEquals("JUNITURL", mr.getString("URL"));
		assertEquals(5, mr.getInt("sizey"));
	}


	/**
	 * Test the savePortlet method. It should take an ISIFRAMECFG row and create
	 * and populate an XML element.
	 *
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testSavePortlet() throws MXException, RemoteException {
		MboSetRemote scconfigs = getMboSet("SCCONFIG");
		MboRemote scconfig = scconfigs.add();

		MboSetRemote layouts = scconfig.getMboSet("LAYOUTALL");
		LayoutRemote layout = (LayoutRemote) layouts.add();

		MboSetRemote msr = layout.getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.add();
		mr.setValue("URL", "JUNIT");
		mr.setValue("SIZEY", 10);

		UserInfo ui = MXServer.getMXServer().getSystemUserInfo();
		HashMap<String, String> handlers = new HashMap<String, String>();
		handlers.put(service.portletName(), "ISIFRAME");

		ISStartCenterLoader loader = new ISStartCenterLoader(ui, handlers);

		Element parent = new Element("test");
		service.savePortlet(parent, loader, layout);

		@SuppressWarnings("unchecked")
		List<Element> children = parent.getChildren();
		assertEquals(1, children.size());

		Element child = children.get(0);
		assertEquals(service.xmlNodeName(), child.getName());
		assertEquals("JUNIT", child.getAttributeValue("url"));
		assertEquals("10", child.getAttributeValue("sizey"));
	}
}
