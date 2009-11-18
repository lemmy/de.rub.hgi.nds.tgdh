/*******************************************************************************
 * Copyright (c) 2009 Markus Alexander Kuppe
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/

package tgdh;

/**
 * @author Markus Alexander Kuppe
 */
public class TgdhGroupIdentifier {

	private String groupName;
	private int mcastPort;
	private String mcastAddress;
	private Worker worker;

	/**
	 * @param mcast_address
	 * @param mcast_port
	 * @param group
	 * @param worker 
	 */
	public TgdhGroupIdentifier(String mcast_address, int mcast_port, String group, Worker worker) {
		this.mcastAddress = mcast_address;
		this.mcastPort = mcast_port;
		this.groupName = group;
		this.worker = worker;
	}

	/**
	 * @return the groupName
	 */
	public String getGroupName() {
		return groupName;
	}

	/**
	 * @return the mcastPort
	 */
	public int getMulticastPort() {
		return mcastPort;
	}

	/**
	 * @return the mcastAddress
	 */
	public String getMulticastAddress() {
		return mcastAddress;
	}

	public byte[] getSessionKey() {
		int keyBits = 512;
		return worker.getTree().getSymmetricKey(keyBits);
	}
}
