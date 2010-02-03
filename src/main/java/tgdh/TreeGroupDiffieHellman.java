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

	public static TgdhGroupIdentifier newGroup(DSAKey dsaKey,
			TgdhKeyListener keyListener, InetAddress inetAddress)
			throws Exception {

		// TODO set params dynamically if more than one SG
		String group = "SLP";
		String mcast_address = "228.222.11.8";
		int mcast_port = 3141;

		TgdhGroupIdentifier groupIdentifier = new TgdhGroupIdentifier(
				mcast_address, mcast_port, group);
		keyListener.setGroupIdentifer(groupIdentifier);

		Worker worker = createWorker(mcast_address, mcast_port, group,
				dsaKey, keyListener);

		return groupIdentifier;
	}

	public static TgdhGroupIdentifier joinGroup(DSAKey dsaKey,
			String multicastAddress, int multicastPort, String groupName,
			TgdhKeyListener keyListener) throws Exception {
		
		TgdhGroupIdentifier groupIdentifier = new TgdhGroupIdentifier(
				multicastAddress, multicastPort, groupName);
		keyListener.setGroupIdentifer(groupIdentifier);

		Worker worker = createWorker(multicastAddress, multicastPort,
				groupName, dsaKey, keyListener);
		worker.joinGroup();
		return groupIdentifier;
	}

	private static Worker createWorker(String multicastAddress,
			int multicastPort, String groupName, DSAKey dsaKey, TgdhKeyListener keyListener)
			throws TgdhException, NotSerializableException {
		// Initializes jGroups
		Communicator aCommunicator = new Communicator(multicastAddress,
				multicastPort);
		Address localaddress = aCommunicator.getLocalAdresse();

		LeafNode owner = new LeafNode(TgdhUtil
				.getName((IpAddress) localaddress));

		DSAParams params = dsaKey.getParams();
		TgdhKeySpec keySpec = new TgdhKeySpec(params.getP(), params.getQ(), params.getG());

		Tree aTree = new Tree(owner);
		aTree.setKeyParams(keySpec);
		aTree.genOwnerKeyPair();

		owner.setSignKey(owner.getPrivate());
		owner.setVerifyKey(owner.getPublic());

		Worker worker = new Worker(groupName, aTree, aCommunicator, 50, keyListener);
		return worker;
	}
}
