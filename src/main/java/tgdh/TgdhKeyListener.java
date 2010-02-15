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
public abstract class TgdhKeyListener {
	
	protected TgdhGroupIdentifier groupIdentifer;
	protected Object lock = new Object();
	
	/**
	 * This method is called by TGDH to notify the listener about a key change
	 * for this Security Group
	 */
	public abstract void keyChanged(byte[] keyBits);

	/**
	 * @param groupIdentifier
	 */
	void setGroupIdentifer(TgdhGroupIdentifier groupIdentifier) {
		this.groupIdentifer = groupIdentifier;
	}

	/**
	 * @param lock
	 */
	public void setLock(Object lock) {
		this.lock = lock;
	}
}
