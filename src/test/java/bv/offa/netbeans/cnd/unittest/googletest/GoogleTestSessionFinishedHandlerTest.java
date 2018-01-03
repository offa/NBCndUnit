/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2018  offa
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
package bv.offa.netbeans.cnd.unittest.googletest;

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
@Tag("GoogleTest")
public class GoogleTestSessionFinishedHandlerTest
{
    private static Project project;
    private static Report report;
    private GoogleTestSessionFinishedHandler handler;
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
        handler = new GoogleTestSessionFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestSession()
    {
        Matcher m = checkedMatch(handler, "[==========] 1200 tests from 307 test cases ran. (1234 ms total)");
        assertEquals("1234", m.group(1));
    }

    @Test
    public void parseDataSingleTests()
    {
        Matcher m = checkedMatch(handler, "[==========] 1 test from 1 test case ran. (3 ms total)");
        assertEquals("3", m.group(1));
    }

    @Test
    public void updateUIDisplaysReport()
    {
        checkedMatch(handler, "[==========] 1 test from 1 test case ran. (3 ms total)");
        when(session.getReport(3l)).thenReturn(report);

        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void udpateUIFinishesSession()
    {
        checkedMatch(handler, "[==========] 1 test from 1 test case ran. (3 ms total)");
        handler.updateUI(manager, session);
        verify(manager).sessionFinished(session);
    }

}
