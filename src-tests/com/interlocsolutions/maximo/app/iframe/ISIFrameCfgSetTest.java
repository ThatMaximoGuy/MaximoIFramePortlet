package com.interlocsolutions.maximo.app.iframe;

import static junitx.framework.ObjectAssert.assertInstanceOf;

import java.rmi.RemoteException;

import org.junit.Test;

import com.interlocsolutions.maximo.junit.MaximoTestHarness;

import psdi.mbo.MboRemote;
import psdi.mbo.MboSetRemote;
import psdi.util.MXException;

/**
 * Test the ISIFrameCfgSet class.
 *
 * @author Martin Nichol
 *
 */
public class ISIFrameCfgSetTest extends MaximoTestHarness {

	/**
	 * Test that the getMboInstance method returns the correct type of object.
	 * @throws MXException if a Maximo problem occurs.
	 * @throws RemoteException if an RMI problem occurs.
	 */
	@Test
	public void testGetMboInstance() throws MXException, RemoteException {
		MboSetRemote msr = getMboSet("ISIFRAMECFG");
		MboRemote mr = msr.add();

		assertInstanceOf(ISIFrameCfg.class, mr);

	}
}
