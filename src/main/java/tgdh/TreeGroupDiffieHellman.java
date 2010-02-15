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

import java.io.NotSerializableException;
import java.net.InetAddress;
import java.security.Security;
import java.security.interfaces.DSAKey;
import java.security.interfaces.DSAParams;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.jgroups.Address;
import org.jgroups.stack.IpAddress;

import tgdh.comm.Communicator;
import tgdh.crypto.TgdhKeySpec;
import tgdh.tree.LeafNode;
import tgdh.tree.Tree;

/**
 * @author Markus Alexander Kuppe
 */
public class TreeGroupDiffieHellman {

	static {
		// Init BouncyCastle
		Security.addProvider(new BouncyCastleProvider());
	}

	public static TgdhGroupIdentifier newGroup(final DSAKey dsaKey,
			final TgdhKeyListener keyListener, final InetAddress inetAddress)
			throws Exception {

		// TODO set params dynamically if more than one SG
		String group = "SLP";
		String mcast_address = "228.222.11.8";
		int mcast_port = 3141;

		final TgdhGroupIdentifier groupIdentifier = new TgdhGroupIdentifier(
				mcast_address, mcast_port, group);
		keyListener.setGroupIdentifer(groupIdentifier);

		final Worker worker = createWorker(mcast_address, mcast_port, group,
				dsaKey, keyListener);
		
		// set the initial GSK to the current tree (after all we are the root and have no members yet)
		keyListener.keyChanged(worker.getTree().getSymmetricKey(512));
		
		return groupIdentifier;
	}

	public static TgdhGroupIdentifier joinGroup(final DSAKey dsaKey,
			final String multicastAddress, final int multicastPort, final String groupName,
			final TgdhKeyListener keyListener) throws Exception {
		
		final TgdhGroupIdentifier groupIdentifier = new TgdhGroupIdentifier(
				multicastAddress, multicastPort, groupName);
		keyListener.setGroupIdentifer(groupIdentifier);

		final Object lock = new Object();
		keyListener.setLock(lock);

		final Worker worker = createWorker(multicastAddress, multicastPort,
				groupName, dsaKey, keyListener);
		worker.joinGroup();
		
		// cause the current thread to sleep so we wait for the key listener to be notified
		// potential for a race condition when the worker notifies us before we call wait()
		synchronized (lock) {
			lock.wait();
		}
		
		return groupIdentifier;
	}

	private static Worker createWorker(final String multicastAddress,
			final int multicastPort, final String groupName, final DSAKey dsaKey, final TgdhKeyListener keyListener)
			throws TgdhException, NotSerializableException {
		// Initializes jGroups
		final Communicator aCommunicator = new Communicator(multicastAddress,
				multicastPort);
		final Address localaddress = aCommunicator.getLocalAdresse();

		final LeafNode owner = new LeafNode(TgdhUtil
				.getName((IpAddress) localaddress));

		final DSAParams params = dsaKey.getParams();
		final TgdhKeySpec keySpec = new TgdhKeySpec(params.getP(), params.getQ(), params.getG());

		final Tree aTree = new Tree(owner);
		aTree.setKeyParams(keySpec);
		aTree.genOwnerKeyPair();

		owner.setSignKey(owner.getPrivate());
		owner.setVerifyKey(owner.getPublic());

		return new Worker(groupName, aTree, aCommunicator, 50, keyListener);
	}
}
