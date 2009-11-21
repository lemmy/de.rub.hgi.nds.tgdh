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

import java.security.interfaces.DSAKey;

/**
 * @author Markus Alexander Kuppe
 */
public abstract class TgdhKeyListener {
	
	private TgdhGroupIdentifier groupIdentifer;
	
	/**
	 * This method is called by TGDH to notify the listener about a key change
	 * for this Security Group
	 * 
	 * @param dsaKey 
	 */
	public abstract void keyChanged(DSAKey dsaKey);

	/**
	 * @return the groupIdentifer
	 */
	public TgdhGroupIdentifier getGroupIdentifer() {
		return groupIdentifer;
	}

	/**
	 * @param groupIdentifer the groupIdentifer to set
	 */
	public void setGroupIdentifer(TgdhGroupIdentifier groupIdentifer) {
		this.groupIdentifer = groupIdentifer;
	}
}
