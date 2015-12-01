package com.interlocsolutions.maximo.app.iframe;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.rmi.RemoteException;

import org.junit.Test;

import com.interlocsolutions.maximo.junit.MaximoTestHarness;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;

/**
 * Test the ISIFrameCfg class.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameCfgTest extends MaximoTestHarness {

	/**
	 * Test adding a new ISIFRAMECFG with an owner record.
	 *
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testAddWithOwner() throws MXException, RemoteException {
		MboSetRemote scconfigs = getMboSet("SCCONFIG");
		MboRemote scconfig = scconfigs.add();

		MboSetRemote layouts = scconfig.getMboSet("LAYOUTALL");
		MboRemote layout = layouts.add();

		MboSetRemote msr = layout.getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.add();

		assertEquals(layout.getLong("LAYOUTID"), mr.getLong("LAYOUTID"));
	}


	/**
	 * Test adding a new ISIFRAMECFG without an owner record.
	 *
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testAddNoOwner() throws MXException, RemoteException {
		MboSetRemote msr = getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.add();

		assertTrue(mr.isNull("LAYOUTID"));
	}

}
