/*******************************************************************************
 * Copyright (c) 2010 Andrew Gvozdev (Quoin Inc.) and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Andrew Gvozdev (Quoin Inc.) - initial API and implementation, based on GCCErrorParserTests
 *******************************************************************************/
package org.eclipse.cdt.core.internal.errorparsers.tests;


import java.io.IOException;

import junit.framework.Test;
import junit.framework.TestSuite;

import org.eclipse.cdt.core.ErrorParserManager;


/**
 * This test is designed to exercise the error parser capabilities for GNU make.
 */
public class MakeErrorParserTests extends GenericErrorParserTests {

	private static final String[] GMAKE_ERROR_STREAM0 = {
		// Infos
		"make: [Hello.o] Error 1 (ignored)",
		"make[2]: [all] Error 2 (ignored)",
		// Warnings
		"make: [Hello.o] Error 1",
		"make: Circular .folder/file.h <- .folder/file2.h dependency dropped.",
		"make[1]: Circular folder/file.h <- Makefile dependency dropped.",
		// Errors
		"make: *** [Hello.o] Error 1",
		"make[3]: *** [Hello.o] Error 1",
		"make: *** No rule to make target `one', needed by `all'.  Stop.",
		"make: *** No rule to make target `all'.  Stop.",
		"make: *** missing.mk: No such file or directory.  Stop.",
		"make: Target `all' not remade because of errors.",
		// Ignored
		"make[3]: Nothing to be done for `all'.",
		"make[2]: `all' is up to date.",

	};
	private static final int GMAKE_ERROR_STREAM0_INFOS = 2;
	private static final int GMAKE_ERROR_STREAM0_WARNINGS = 3;
	private static final int GMAKE_ERROR_STREAM0_ERRORS = 6;

	private static final String[] GMAKE_ERROR_STREAM1 = {
		// Warning
		"Makefile1:10: include.mk: No such file or directory",
		// Errors
		"Makefile2:10: *** missing separator.  Stop.",
		"Makefile3:10: *** missing separator (did you mean TAB instead of 8 spaces?).  Stop.",
		"Makefile4:10: *** commands commence before first target. Stop.",
		"Makefile5:10: *** Recursive variable 'VAR' references itself (eventually). Stop.",
		"Makefile6:10: *** target pattern contains no `%'.  Stop.",
	};
	private static final int GMAKE_ERROR_STREAM1_WARNINGS = 1;
	private static final int GMAKE_ERROR_STREAM1_ERRORS = 5;
	private static final String[] GMAKE_ERROR_STREAM1_FILENAMES = {"Makefile1", "Makefile2",
		"Makefile3", "Makefile4", "Makefile5", "Makefile6"};

	public MakeErrorParserTests() {
		super();
	}

	public static Test suite() {
		TestSuite suite = new TestSuite(MakeErrorParserTests.class);
		return suite;
	}

	public void testGmakeSanity() throws Exception {
		assertNotNull(ErrorParserManager.getErrorParserCopy(GMAKE_ERROR_PARSER_ID));
	}

	public void testGmakeMessages0() throws IOException {
		runParserTest(GMAKE_ERROR_STREAM0, GMAKE_ERROR_STREAM0_ERRORS, GMAKE_ERROR_STREAM0_WARNINGS, GMAKE_ERROR_STREAM0_INFOS,
				null, null, new String[]{GMAKE_ERROR_PARSER_ID});
	}

	public void testGMakeMessages1() throws IOException {
		runParserTest(GMAKE_ERROR_STREAM1, GMAKE_ERROR_STREAM1_ERRORS, GMAKE_ERROR_STREAM1_WARNINGS,
				GMAKE_ERROR_STREAM1_FILENAMES, null, new String[]{GMAKE_ERROR_PARSER_ID});
	}
}
