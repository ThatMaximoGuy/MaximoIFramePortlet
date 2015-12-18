package com.interlocsolutions.maximo.webclient.iframe;

import java.rmi.RemoteException;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;
import psdi.webclient.beans.startcntr.PortletAppBean;

/**
 * @author Martin Nichol
 *
 */
public class IFramePortletBean extends PortletAppBean {

	/**
	 *  Constructor.
	 */
	public IFramePortletBean() {
		super();
	}

	@Override
	public void initializeApp() throws MXException, RemoteException {
		super.initializeApp();

		MboSetRemote msr = getMbo().getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.getMbo();
		if (mr == null) {
			msr.add();
			save();
			fireDataChangedEvent();
		}
	}

}
