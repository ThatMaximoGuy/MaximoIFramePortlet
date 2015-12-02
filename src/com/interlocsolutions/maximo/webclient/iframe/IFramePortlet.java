package com.interlocsolutions.maximo.webclient.iframe;

import java.rmi.RemoteException;

import psdi.util.MXException;
import psdi.webclient.controls.PortletDataInstance;

/**
 * A data container for the IFramePortlet jsp file.
 *
 * @author Martin Nichol
 *
 */
public class IFramePortlet extends PortletDataInstance {

	/**
	 * Constructor.
	 */
	public IFramePortlet() {
		super();
	}

	/**
	 * @return return the vertical size of the iframe.
	 */
	public int getSizeY() {
		try {
			return getDataBean().getMbo().getInt("SIZEY");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MXException e) {
			e.printStackTrace();
		}
		return 200;
	}

	/**
	 * @return return the url to display.
	 */
	public String getUrl() {
		try {
			return getDataBean().getMbo().getString("URL");
		} catch (RemoteException e) {
			e.printStackTrace();
		} catch (MXException e) {
			e.printStackTrace();
		}
		return "";
	}
}
