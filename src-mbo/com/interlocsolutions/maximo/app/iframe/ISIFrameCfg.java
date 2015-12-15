package com.interlocsolutions.maximo.app.iframe;

import java.rmi.RemoteException;

import com.interlocsolutions.maximo.app.scconfig.AbstractPortletConfig;

import psdi.mbo.MboSet;

/**
 * Mbo class for ISIFRAMECFG table.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameCfg extends AbstractPortletConfig {

	/**
	 * Constructor.
	 *
	 * @param ms the mboset owning this mbo.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	public ISIFrameCfg(MboSet ms) throws RemoteException {
		super(ms);
	}

}
