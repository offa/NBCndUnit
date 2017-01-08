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

package bv.offa.netbeans.cnd.unittest.cpputest.teamcity;

import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.cpputest.TestSessionInformation;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import java.util.regex.Matcher;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class CppUTestTCSuiteFinishedHandlerTest
{
    private static Project project;
    private static Report report;
    private TestSessionInformation info;
    private CppUTestTCSuiteFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @BeforeClass
    public static void setUpClass()
    {
        project = mock(Project.class);
        when(project.getProjectDirectory())
                .thenReturn(FileUtil.createMemoryFileSystem().getRoot());
        when(project.getLookup()).thenReturn(Lookup.EMPTY);
        report = new Report("suite", project);
    }

    @Before
    public void setUp()
    {
        info = new TestSessionInformation();
        handler = new CppUTestTCSuiteFinishedHandler(info);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestSuite()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testSuiteFinished name='TestSuite']");
        assertEquals("TestSuite", m.group(1));
    }

    @Test
    public void updateUIHasNoInteraction()
    {
        handler.updateUI(manager, session);
    }

    @Test
    public void updateUIDisplaysReport()
    {
        info.setTimeTotal(16l);
        when(session.getReport(info.getTimeTotal())).thenReturn(report);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void udpateUIFinishesSession()
    {
        info.setTimeTotal(16l);
        handler.updateUI(manager, session);
        verify(manager).sessionFinished(session);
        assertEquals(0L, info.getTimeTotal());
    }

}
