/*******************************************************************************
 * Copyright (c) 2007 Symbian Software Limited and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 * Bala Torati (Symbian) - Initial API and implementation
 *******************************************************************************/
package org.eclipse.cdt.managedbuilder.templateengine.tests;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junit.framework.TestCase;

import org.eclipse.cdt.core.templateengine.TemplateCore;
import org.eclipse.cdt.core.templateengine.TemplateEngine;
import org.eclipse.cdt.core.templateengine.TemplateEngineHelper;
import org.eclipse.cdt.managedbuilder.core.BuildException;
import org.eclipse.cdt.managedbuilder.core.IConfiguration;
import org.eclipse.cdt.managedbuilder.core.IOption;
import org.eclipse.cdt.managedbuilder.core.IResourceConfiguration;
import org.eclipse.cdt.managedbuilder.core.ITool;
import org.eclipse.cdt.managedbuilder.core.ManagedBuildManager;
import org.eclipse.core.resources.IFolder;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;

public class TestProcesses extends TestCase {
	
	private static final String workspaceLocation = ResourcesPlugin.getWorkspace().getRoot().getRawLocation().toOSString();
	private static final String PROJECT_NAME = "TemplateEngineTestsProject"; //$NON-NLS-1$
	private static final String SOURCE_FOLDER = "Source"; //$NON-NLS-1$
	private static final String INCLUDE_FOLDER = "Include"; //$NON-NLS-1$
	private static final String FILE_NAME = "File"; //$NON-NLS-1$
	private static final String LINK = "Link"; //$NON-NLS-1$
	private static final String APPEND = "Append"; //$NON-NLS-1$
	private static final String CPP_EXT = ".cpp"; //$NON-NLS-1$
	private static final String H_EXT = ".h"; //$NON-NLS-1$
	private static final String MBS_GNU_CPP_LINK_OPTION_ID = ".*gnu.cpp.link.option.*"; //$NON-NLS-1$
	private static final String MBS_STRING_OPTION_VALUE = "MBSStringOption"; //$NON-NLS-1$
	private static final String[] MBS_STRING_LIST_OPTION_VALUES = {"MBS", "String", "List", "Option"}; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	private static final String RELEASE_CONFIG_ID = ".*release.*"; //$NON-NLS-1$
	private static final String PROJECT_TYPE = "org.eclipse.cdt.managedbuilder.core.tests.projectType"; //$NON-NLS-1$
	
	private List configList;
	
	protected void setUp() throws Exception {
		TemplateEngineTestsHelper.turnOffAutoBuild();
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		if (project.exists()) {
			project.delete(true, true, null);
		}
		configList = new ArrayList();
		//TODO: Add valid configurations for project creation
		configList.add(new IConfiguration[0]);
	}
	
	public void testCreateIncludeFolder() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*CreateIncludeFolder"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("includeDir1", INCLUDE_FOLDER + 1); //$NON-NLS-1$
		valueStore.put("includeDir2", INCLUDE_FOLDER + 2); //$NON-NLS-1$
		valueStore.put(TemplateEngineHelper.CONFIGURATIONS, configList); //$NON-NLS-1$
		
		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		IFolder folder = project.getFolder(INCLUDE_FOLDER + 1);
		assertTrue(folder.exists());
		folder = project.getFolder(INCLUDE_FOLDER + 2);
		assertTrue(folder.exists());
	}

	public void testNewManagedProject() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*NewManagedProject"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put(TemplateEngineHelper.CONFIGURATIONS, configList); //$NON-NLS-1$
		
		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}
		
		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
	}

	public void testSetMBSBooleanOptionValue() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*SetMBSBooleanOptionValue"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("id", MBS_GNU_CPP_LINK_OPTION_ID); //$NON-NLS-1$

		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		try {
			assertSetMBSOptionValues(project, MBS_GNU_CPP_LINK_OPTION_ID, IOption.BOOLEAN, false);
		} catch (BuildException e) {
			fail(e.getMessage());
		}

	}
	
	public void testSetMBSStringOptionValue() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*SetMBSStringOptionValue"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("id", MBS_GNU_CPP_LINK_OPTION_ID); //$NON-NLS-1$
		valueStore.put("StringValue", MBS_STRING_OPTION_VALUE); //$NON-NLS-1$

		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		try {
			assertSetMBSOptionValues(project, MBS_GNU_CPP_LINK_OPTION_ID, IOption.STRING, false);
		} catch (BuildException e) {
			fail(e.getMessage());
		}
	}
	
	public void testSetMBSStringListOptionValues() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*SetMBSStringListOptionValues"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("id", MBS_GNU_CPP_LINK_OPTION_ID); //$NON-NLS-1$
		
		for (int i=0; i < MBS_STRING_LIST_OPTION_VALUES.length; i++) {
			valueStore.put("StringListValue" + i, MBS_STRING_LIST_OPTION_VALUES[i]); //$NON-NLS-1$
		}

		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		try {
			assertSetMBSOptionValues(project, MBS_GNU_CPP_LINK_OPTION_ID, IOption.STRING_LIST, false);
		} catch (BuildException e) {
			fail(e.getMessage());
		}
	}
	
	public void testAppendToMBSStringOptionValue() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*AppendToMBSStringOptionValue"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("id", MBS_GNU_CPP_LINK_OPTION_ID); //$NON-NLS-1$
		valueStore.put("StringValue", MBS_STRING_OPTION_VALUE); //$NON-NLS-1$
		valueStore.put("AppendStringValue", APPEND + MBS_STRING_OPTION_VALUE); //$NON-NLS-1$

		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		try {
			assertSetMBSOptionValues(project, MBS_GNU_CPP_LINK_OPTION_ID, IOption.STRING, true);
		} catch (BuildException e) {
			fail(e.getMessage());
		}
	}
	
	public void testAppendToMBSStringListOptionValues() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*AppendToMBSStringListOptionValues"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("id", MBS_GNU_CPP_LINK_OPTION_ID); //$NON-NLS-1$
		
		for (int i=0; i < MBS_STRING_LIST_OPTION_VALUES.length; i++) {
			valueStore.put("StringListValue" + i, MBS_STRING_LIST_OPTION_VALUES[i]); //$NON-NLS-1$
		}

		for (int i=0; i < MBS_STRING_LIST_OPTION_VALUES.length; i++) {
			valueStore.put("AppendStringListValue" + i, APPEND + MBS_STRING_LIST_OPTION_VALUES[i]); //$NON-NLS-1$
		}

		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		try {
			assertSetMBSOptionValues(project, MBS_GNU_CPP_LINK_OPTION_ID, IOption.STRING_LIST, true);
		} catch (BuildException e) {
			fail(e.getMessage());
		}
	}
	
	public void testExcludeResources() {
		TemplateCore template = TemplateEngine.getDefault().getFirstTemplate(PROJECT_TYPE, null, ".*ExcludeResources"); //$NON-NLS-1$
		Map valueStore = template.getValueStore();
		valueStore.put("projectName", PROJECT_NAME); //$NON-NLS-1$
		valueStore.put("projectType", PROJECT_TYPE); //$NON-NLS-1$
		valueStore.put("location", ""); //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("isCProject", "false"); //$NON-NLS-1$ //$NON-NLS-2$
		
		for (int i=0; i < 3; i++) {
			valueStore.put("baseName" + i, "BaseName" + i); //$NON-NLS-1$ //$NON-NLS-2$
		}
		
		valueStore.put("configIdPattern", RELEASE_CONFIG_ID); //$NON-NLS-1$
		valueStore.put("filePatterns0", ".*BaseName0.*"); //Exlude BaseName0.* from Release config //$NON-NLS-1$ //$NON-NLS-2$
		valueStore.put("filePatterns1", ".*BaseName1.*"); //Exlude BaseName1.* from all other configs except from Release config //$NON-NLS-1$ //$NON-NLS-2$
		
		if (TemplateEngineTestsHelper.failIfErrorStatus(template.executeTemplateProcesses(null, false))) {
			return;
		}

		IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(PROJECT_NAME);
		assertTrue(project.exists());
		
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject().getConfigurations();
		for(int i=0; i<projectConfigs.length; i++) {
			IConfiguration config = projectConfigs[i];
			IResourceConfiguration[] resourceConfigs = config.getResourceConfigurations();
			for (int j=0; j<resourceConfigs.length; j++) {
				if (resourceConfigs[j].isExcluded()) {
					String resourcePath = resourceConfigs[i].getResourcePath();
					if (resourcePath.matches(".*BaseName0.*")) { //$NON-NLS-1$
						//Exlude BaseName0.* only from Release config
						assertTrue(config.getId().matches(RELEASE_CONFIG_ID));	
					} else if (resourcePath.matches(".*BaseName1.*")) { //$NON-NLS-1$
							//Exlude BaseName1.* from all other configs other than from Release config
							assertFalse(config.getId().matches(RELEASE_CONFIG_ID));	
					} else {
						fail(resourcePath + " shouldn't be excluded from project"); //$NON-NLS-1$
					}
				}
			}
		}
	}
	
	private void assertSetMBSOptionValues(IProject project, String id, int optionType, boolean append) throws BuildException {
		IConfiguration[] projectConfigs = ManagedBuildManager.getBuildInfo(project).getManagedProject().getConfigurations();

		for(int i=0; i<projectConfigs.length; i++) {
			IConfiguration config = projectConfigs[i];
			IOption[] globalOptions = config.getToolChain().getOptions();
			assertMBSOptionValues(id.toLowerCase(), globalOptions, optionType, append);
			
			ITool[] tools = config.getTools();
			for(int j=0; j<tools.length; j++) {
				assertMBSOptionValues(id.toLowerCase(), tools[j].getOptions(), optionType, append);
			}
		}
	}
	
	public void assertMBSOptionValues(String id, IOption[] options, int optionType, boolean append) throws BuildException {
		for (int i = 0; i < options.length; i++) {
			IOption option = options[i];
			if (option.getId().toLowerCase().matches(id)) {
				if (option.getValueType() == optionType) {
					switch (optionType) {
					case IOption.BOOLEAN:
						assertTrue(option.getBooleanValue() == true);
						break;
					case IOption.STRING:
						if (append) {
							assertTrue(option.getStringValue().equals(MBS_STRING_OPTION_VALUE + APPEND + MBS_STRING_OPTION_VALUE));
						} else {
							assertTrue(option.getStringValue().equals(MBS_STRING_OPTION_VALUE));
						}
						break;
					case IOption.STRING_LIST:
					case IOption.INCLUDE_PATH:
					case IOption.PREPROCESSOR_SYMBOLS:
					case IOption.LIBRARIES:
					case IOption.OBJECTS:
						String[] optionValues = option.getStringListValue();
						if (append) {
							assertTrue(optionValues.length == 2 * MBS_STRING_LIST_OPTION_VALUES.length);
							int j=0;
							for (; j < MBS_STRING_LIST_OPTION_VALUES.length; j++) {
								assertTrue(optionValues[j].equals(MBS_STRING_LIST_OPTION_VALUES[j]));
							}
							for (; j < optionValues.length; j++) {
								assertTrue(optionValues[j].equals(APPEND + MBS_STRING_LIST_OPTION_VALUES[j-MBS_STRING_LIST_OPTION_VALUES.length]));
							}
						} else {
							assertTrue(optionValues.length == MBS_STRING_LIST_OPTION_VALUES.length);
							for (int j=0; j < optionValues.length; j++) {
								assertTrue(optionValues[j].equals(MBS_STRING_LIST_OPTION_VALUES[j]));
							}
						}
						break;
						default:
							continue;
					}
				}
			}
		}
	}
		
}
