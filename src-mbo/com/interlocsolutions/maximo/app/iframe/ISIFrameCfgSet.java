/**
 *
 */
package com.interlocsolutions.maximo.app.iframe;

import java.rmi.RemoteException;

import psdi.mbo.Mbo;
import psdi.mbo.MboServerInterface;
import psdi.mbo.MboSet;
import psdi.util.MXException;

/**
 * An MboSet for the ISIFRAMECFG table.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameCfgSet extends MboSet {

	/**
	 * Constructor.
	 *
	 * @param ms thre owning mboserver.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	public ISIFrameCfgSet(MboServerInterface ms) throws RemoteException {
		super(ms);
	}


	/**
	 * {@inerhitDoc}
	 *
	 * @see psdi.mbo.MboSet#getMboInstance(psdi.mbo.MboSet)
	 */
	@Override
	protected Mbo getMboInstance(MboSet arg0) throws MXException, RemoteException {
		return new ISIFrameCfg(arg0);
	}

}
