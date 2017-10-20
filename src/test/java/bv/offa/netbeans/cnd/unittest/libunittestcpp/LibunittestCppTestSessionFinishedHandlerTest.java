/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2017  offa
 *
 * This file is part of NBCndUnit.
 *
 * NBCndUnit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBCndUnit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBCndUnit.  If not, see <http://www.gnu.org/licenses/>.
 */
package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import java.util.regex.Matcher;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

@Tag("Test-Framework")
@Tag("LibUnittestCpp")
public class LibunittestCppTestSessionFinishedHandlerTest
{

    private static Project project;
    private static Report report;
    private LibunittestCppTestSessionFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @BeforeAll
    public static void setUpClass()
    {
        project = mock(Project.class);
        when(project.getProjectDirectory())
                .thenReturn(FileUtil.createMemoryFileSystem().getRoot());
        when(project.getLookup()).thenReturn(Lookup.EMPTY);
        report = new Report("suite", project);
    }

    @BeforeEach
    public void setUp()
    {
        handler = new LibunittestCppTestSessionFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void matchesSuccessfulTest()
    {
        assertTrue(handler.matches("Ran 60 tests in 0.100471s"));
        assertTrue(handler.matches("Ran 5 tests in 127.000288703s"));
    }

    @Test
    public void parseDataSuccessfulTest()
    {
        Matcher m = checkedMatch(handler, "Ran 5 tests in 127.000288703s");
        assertEquals("127.000288703", m.group(1));
    }

    @Test
    public void rejectsMalformedTests()
    {
        assertFalse(handler.matches("Ran 2 tests in"));
        assertFalse(handler.matches("Ran 2 tests in "));
    }

    @Test
    public void updateUIDisplaysReport()
    {
        checkedMatch(handler, "Ran 5 tests in 127.000288703s");
        when(session.getReport(127000l)).thenReturn(report);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void udpateUIFinishesSession()
    {
        checkedMatch(handler, "Ran 5 tests in 127.000288703s");
        handler.updateUI(manager, session);
        verify(manager).sessionFinished(session);
    }
}
