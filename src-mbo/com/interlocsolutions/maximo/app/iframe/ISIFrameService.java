package com.interlocsolutions.maximo.app.iframe;

import java.rmi.RemoteException;

import org.jdom.Element;

import com.interlocsolutions.maximo.app.scconfig.CustomPortletHandler;
import com.interlocsolutions.maximo.app.scconfig.ISStartCenterLoader;

import psdi.app.scconfig.LayoutRemote;
import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.server.AppService;
import psdi.server.MXServer;
import psdi.util.MXException;

/**
 * A custom service for the Interloc IFrame Porlet.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameService extends AppService implements CustomPortletHandler {

	/**
	 * Constructor.
	 *
	 * @throws RemoteException if an RMI problem occurs.
	 */
	public ISIFrameService() throws RemoteException {
		super();
	}


	/**
	 * Constructor.
	 *
	 * @param mxServer the owning mxserver.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	public ISIFrameService(MXServer mxServer) throws RemoteException {
		super(mxServer);
	}


	@Override
	public void loadPortlet(Element arg0, ISStartCenterLoader arg1, LayoutRemote arg2) throws MXException, RemoteException {
		Element iframeXml = arg0.getChild(xmlNodeName());

		MboSetRemote msr = arg2.getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.add();
		mr.setValue("URL", iframeXml.getAttributeValue("url"));
		mr.setValue("SIZEY", iframeXml.getAttributeValue("sizey"));
	}


	@Override
	public String portletName() {
		return "ISIFRAME";
	}


	@Override
	public void savePortlet(Element arg0, ISStartCenterLoader arg1, LayoutRemote arg2) throws MXException, RemoteException {
		MboSetRemote msr = null;
		try {
			msr = arg2.getMboSet("ISIFRAMECFG");
			MboRemote mr = msr.getMbo(0);
			Element iframeXml = new Element(xmlNodeName());
			iframeXml.setAttribute("url", mr.getString("URL"));
			iframeXml.setAttribute("sizey", mr.getString("SIZEY"));
			arg0.addContent(iframeXml);
		} finally {
			if (msr != null) {
				msr.close();
			}
		}
	}


	@Override
	public String xmlNodeName() {
		return "isiframe";
	}
}
