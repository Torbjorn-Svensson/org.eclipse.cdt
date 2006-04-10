/********************************************************************************
 * Copyright (c) 2002, 2006 IBM Corporation. All rights reserved.
 * This program and the accompanying materials are made available under the terms
 * of the Eclipse Public License v1.0 which accompanies this distribution, and is 
 * available at http://www.eclipse.org/legal/epl-v10.html
 * 
 * Initial Contributors:
 * The following IBM employees contributed to the Remote System Explorer
 * component that contains this file: David McKnight, Kushal Munir, 
 * Michael Berger, David Dykstal, Phil Coulthard, Don Yantzi, Eric Simpson, 
 * Emily Bruner, Mazen Faraj, Adrian Storisteanu, Li Ding, and Kent Hawley.
 * 
 * Contributors:
 * {Name} (company) - description of contribution.
 ********************************************************************************/

package org.eclipse.rse.model;
import java.util.EventListener;

import org.eclipse.swt.widgets.Shell;

/**
 * Interface that listeners interesting in changes to remote
 * system connections and subsystems can listen implement and
 * subsequently register their interest in via SystemRegistry.
 */
public interface ISystemResourceChangeListener extends EventListener
{
	/**
	 * This is the method in your class that will be called when a
	 *  system resource changes.
	 * @see ISystemResourceChangeEvent
	 */
    public void systemResourceChanged(ISystemResourceChangeEvent event);
    /**
     * This is the method in your class that will be called to return the
     * shell for your viewer
     */
    public Shell getShell();
}