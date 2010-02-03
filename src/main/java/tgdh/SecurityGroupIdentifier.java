/*******************************************************************************
 * Copyright (c) 2010 Markus Alexander Kuppe
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Markus Alexander Kuppe (ecf-dev <at> lemmster <dot> de) - initial API and implementation
 ******************************************************************************/
package tgdh;

public interface SecurityGroupIdentifier {
	/**
	 * @return the groupName
	 */
	public String getGroupName();

	/**
	 * @return the mcastPort
	 */
	public int getMulticastPort();

	/**
	 * @return the mcastAddress
	 */
	public String getMulticastAddress();
}
