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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.frameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasNoError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasStatus;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestSuite;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.sessionIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.suiteFrameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.timeIs;
import java.util.regex.Matcher;
import static org.hamcrest.CoreMatchers.allOf;
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
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class CppUTestTestHandlerTest
{
    private static final TestSessionInformation INFO = new TestSessionInformation();
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST;
    private static Project project;
    private static Report report;
    private CppUTestTestHandler handler;
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
        handler = new CppUTestTestHandler(INFO);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }
    
    @Test
    public void matchesTestCaseAndDetectsIgnored()
    {
        Matcher m = checkedMatch(handler, "IGNORE_TEST(TestSuite, testCase) - 7 ms");
        assertNotNull(m.group(1));
    }
    
    @Test
    public void parsesDataTestCase()
    {
        Matcher m = checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        assertEquals("TEST(TestSuite, testCase) - 84 ms", m.group());
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("84", m.group(5));
    }
    
    @Test
    public void parsesDataTestCaseWhichFailed()
    {
        Matcher m = checkedMatch(handler, "TEST(TestSuite, testThatFailed)");
        assertEquals("TestSuite", m.group(2));
        assertEquals("testThatFailed", m.group(3));
        assertNull(m.group(4));
    }
    
    @Test
    public void rejectsMalformedTestCase()
    {
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 1"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - a"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - abc ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) -  ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 11 ms "));
        assertFalse(handler.matches("TEST(TestSuite, )"));
        assertFalse(handler.matches("TEST(TestSuite, testCase, wrong) - 5 ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 5 ms - 7 ms"));
    }
    
    @Test
    public void suiteTimeParsing()
    {
        final String input[] = new String[]
        {
            "TEST(TestSuite1, testCase1) - 17005 ms",
            "TEST(TestSuite2, testCase1) - 8 ms",
            "TEST(TestSuite2, testCase2) - 25 ms",
            "TEST(TestSuite3, testCase1) - 0 ms",
        };
        
        final long expected = 17005L + 8L + 25L;
        long time = 0L;
        
        for( String line : input )
        {
            Matcher m = checkedMatch(handler, line);
            time += Long.valueOf(m.group(5));
        }
        
        assertEquals(expected, time);
    }
    
    @Test
    public void matchesTestCaseWithOutputNoTime()
    {
        assertTrue(handler.matches("TEST(TestSuite, testCase)"));
    }
    
    @Test
    public void updateUIStartsTestIfFirstTest()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(manager).testStarted(session);
    }
    
    @Test
    public void updateUIStartsStartsTestBeforeSuite()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        InOrder inOrder = inOrder(manager);
        inOrder.verify(manager).testStarted(any(TestSession.class));
        inOrder.verify(manager).displaySuiteRunning(any(TestSession.class), any(CndTestSuite.class));
    }
    
    @Test
    public void updateUIDisplaysReportIfNotFirstTest()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        when(session.getReport(anyLong())).thenReturn(report);
        handler.updateUI(manager, session);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }
    
    @Test
    public void updateUIStartsNewSuiteIfFirstSuite()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(allOf(matchesTestSuite("TestSuite"), 
                                                suiteFrameworkIs(FRAMEWORK))));
        verify(manager).displaySuiteRunning(eq(session), argThat(allOf(matchesTestSuite("TestSuite"), 
                                                                        suiteFrameworkIs(FRAMEWORK))));
    }
    
    @Test
    public void updateUIAddsTestCase()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(matchesTestCase("testCase", "TestSuite")));
    }
    
    @Test
    public void updateUISetsTestCaseInformation()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(allOf(matchesTestCase("testCase", "TestSuite"), 
                                                    frameworkIs(FRAMEWORK), 
                                                    sessionIs(session),
                                                    timeIs(84),
                                                    hasNoError())));
    }
    
    @Test
    public void updateUISetsSkippedOnIgnored()
    {
        checkedMatch(handler, "IGNORE_TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(hasStatus(Status.SKIPPED)));
    }
    
    @Test
    public void updateUIIgnoresTimeIfSeparated()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase)");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(matchesTestCase("testCase", "TestSuite")));
    }
}
