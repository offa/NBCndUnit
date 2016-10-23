/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2016  offa
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

import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import bv.offa.netbeans.cnd.unittest.testhelper.Helper;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.frameworkIs;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasNoError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasStatus;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestSuite;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.sessionIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.suiteFrameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.timeIs;
import static org.hamcrest.CoreMatchers.allOf;
import org.mockito.InOrder;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;

public class LibunittestCppTestFinishedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.LIBUNITTESTCPP;
    private static Project project;
    private static Report report;
    private LibunittestCppTestFinishedHandler handler;
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
        handler = new LibunittestCppTestFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void matchesSuccessfulTest()
    {
        assertTrue(handler.matches("test_name::testA ... [1.551e-05s] ok"));
        assertTrue(handler.matches("test_name::testB ... [0.000108249s] ok"));
        assertTrue(handler.matches("test_name::testC ... [477.100486s] ok"));
        assertTrue(handler.matches("test_suite::test_name::test_case ... [9.217e-06s] ok"));
    }

    @Test
    public void parseDataSuccessfulTest()
    {
        Matcher m = checkedMatch(handler, "test_name::testA ... [1.551e-05s] ok");
        assertEquals("test_name", m.group(1));
        assertEquals("testA", m.group(2));
        assertEquals("1.551e-05", m.group(3));
        assertEquals("ok", m.group(4));
    }

    @Test
    public void parseDataFailedTest()
    {
        Matcher m = checkedMatch(handler, "test_name::testB ... [0.000108249s] FAIL");
        assertEquals("test_name", m.group(1));
        assertEquals("testB", m.group(2));
        assertEquals("0.000108249", m.group(3));
        assertEquals("FAIL", m.group(4));
    }

    @Test
    public void parseDataIgnoredTestCase()
    {
        Matcher m = handler.match("TestSuite::testExample::test ... [0s] SKIP A message");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testExample::test", m.group(2));
        assertEquals("0", m.group(3));
        assertEquals("SKIP", m.group(4));
    }

    @Test
    public void updateUIStartsTestIfFirstTest()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        handler.updateUI(manager, session);
        verify(manager).testStarted(session);
    }

    @Test
    public void updateUIStartsTestBeforeSuite()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        handler.updateUI(manager, session);
        InOrder inOrder = inOrder(manager);
        inOrder.verify(manager).testStarted(any(TestSession.class));
        inOrder.verify(manager).displaySuiteRunning(any(TestSession.class), any(CndTestSuite.class));
    }

    @Test
    public void updateUIDisplaysReportIfNotFirstTest()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        when(session.getReport(anyLong())).thenReturn(report);
        handler.updateUI(manager, session);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void updateUIStartsNewSuiteIfFirstSuite()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(allOf(matchesTestSuite("TestSuite"),
                                                suiteFrameworkIs(FRAMEWORK))));
        verify(manager).displaySuiteRunning(eq(session), argThat(allOf(matchesTestSuite("TestSuite"),
                                                                        suiteFrameworkIs(FRAMEWORK))));
    }

    @Test
    public void updateUIStartsNewSuiteIfNewSuiteStarted()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        Helper.createCurrentTestSuite("TestSuit", FRAMEWORK, session);
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(allOf(matchesTestSuite("TestSuite"),
                                                suiteFrameworkIs(FRAMEWORK))));
        verify(manager).displaySuiteRunning(eq(session), argThat(allOf(matchesTestSuite("TestSuite"),
                                                                        suiteFrameworkIs(FRAMEWORK))));
    }

    @Test
    public void updateUIDoesNothingIfSameSuite()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        CndTestSuite suite = new CndTestSuite("TestSuite", FRAMEWORK);
        when(session.getCurrentSuite()).thenReturn(suite);
        handler.updateUI(manager, session);

        verify(session, never()).addSuite(any(CndTestSuite.class));
        verify(manager, never()).displaySuiteRunning(any(TestSession.class), any(TestSuite.class));
    }

    @Test
    public void updateUIAddsTestCase()
    {
        checkedMatch(handler, "TestSuite::testCase ... [1.551e-05s] ok");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(matchesTestCase("testCase", "TestSuite")));
    }

    @Test
    public void updateUISetsTestCaseInformation()
    {
        checkedMatch(handler, "TestSuite::testCase ... [477.100486s] ok");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(allOf(matchesTestCase("testCase", "TestSuite"),
                                                    frameworkIs(FRAMEWORK),
                                                    sessionIs(session),
                                                    timeIs(477100),
                                                    hasNoError())));
    }

    @Test
    public void updateUISetsErrorOnFailure()
    {
        checkedMatch(handler, "TestSuite::testCase ... [0.000108249s] FAIL");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(hasError()));
    }

    @Test
    public void updateUISetsSkippedOnIgnored()
    {
        checkedMatch(handler, "TestSuite::testCase ... [0s] SKIP A message");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(hasStatus(Status.SKIPPED)));
    }

}
