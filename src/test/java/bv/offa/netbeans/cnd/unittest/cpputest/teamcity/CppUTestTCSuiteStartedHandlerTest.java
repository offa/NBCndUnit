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

import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestSuite;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestSuite;
import java.util.regex.Matcher;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class CppUTestTCSuiteStartedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST_TC;
    private static Project project;
    private static Report report;
    private CppUTestTCSuiteStartedHandler handler;
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
        handler = new CppUTestTCSuiteStartedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestSuite()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        assertEquals("TestSuite", m.group(1));
    }

    @Test
    public void updateUIStartsTestIfFirstTest()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        handler.updateUI(manager, session);
        verify(manager).testStarted(session);
    }

    @Test
    public void updateUIStartsTestBeforeSuite()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        handler.updateUI(manager, session);
        InOrder inOrder = inOrder(manager);
        inOrder.verify(manager).testStarted(any(TestSession.class));
        inOrder.verify(manager).displaySuiteRunning(any(TestSession.class), any(CndTestSuite.class));
    }

    @Test
    public void updateUIDisplaysReportIfNotFirstTest()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        when(session.getReport(anyLong())).thenReturn(report);
        handler.updateUI(manager, session);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void updateUIStartsNewSuiteIfFirstSuite()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(matchesTestSuite("TestSuite")));
        verify(manager).displaySuiteRunning(eq(session), argThat(matchesTestSuite("TestSuite")));
    }

    @Test
    public void updateUIStartsNewSuiteIfNewSuiteStarted()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        createCurrentTestSuite("TestSuiteFirst", FRAMEWORK, session);
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(matchesTestSuite("TestSuite")));
        verify(manager).displaySuiteRunning(eq(session), argThat(matchesTestSuite("TestSuite")));
    }

    @Test
    public void updateUIDoesNothingIfSameSuite()
    {
        checkedMatch(handler, "##teamcity[testSuiteStarted name='TestSuite']");
        createCurrentTestSuite("TestSuite", FRAMEWORK, session);
        handler.updateUI(manager, session);
        verify(session, never()).addSuite(any(CndTestSuite.class));
        verify(manager, never()).displaySuiteRunning(any(TestSession.class), any(TestSuite.class));
    }

}
