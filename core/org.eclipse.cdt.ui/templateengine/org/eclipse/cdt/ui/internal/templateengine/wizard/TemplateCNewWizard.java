/*******************************************************************************
 * Copyright (c) 2007 Intel Corporation and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Intel Corporation - initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.ui.internal.templateengine.wizard;

import java.util.ArrayList;

import org.eclipse.jface.wizard.IWizard;
import org.eclipse.swt.widgets.Composite;

import org.eclipse.cdt.core.templateengine.TemplateInfo;
import org.eclipse.cdt.ui.templateengine.Template;
import org.eclipse.cdt.ui.templateengine.TemplateEngineUI;
import org.eclipse.cdt.ui.wizards.EntryDescriptor;
import org.eclipse.cdt.ui.wizards.ICNewWizard;
import org.eclipse.cdt.ui.wizards.IWizardItemsListListener;

/**
 *
 */
public class TemplateCNewWizard implements ICNewWizard {
	/**
	 * Creates and returns an array of items to be displayed 
	 */
	public EntryDescriptor[] createItems(boolean supportedOnly, IWizard wizard) {
		Template[] templates = TemplateEngineUI.getDefault().getTemplates();
		ArrayList items = new ArrayList();
		
		for (int k=0; k < templates.length; k++) {
			TemplateInfo templateInfo = templates[k].getTemplateInfo();

			items.add(new EntryDescriptor(templates[k].getTemplateId(),
						templateInfo.getProjectType(),
						templates[k].getLabel(),
						templateInfo.isCategory(),
						null,
						null));
		}
		return (EntryDescriptor[])items.toArray(new EntryDescriptor[items.size()]);
	}

	public void setDependentControl(Composite parent,
			IWizardItemsListListener page) {
		//nothing to do?
	}
	
}
