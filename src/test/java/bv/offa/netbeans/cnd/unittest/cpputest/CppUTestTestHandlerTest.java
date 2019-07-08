/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2019  offa
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
import static bv.offa.netbeans.cnd.unittest.testhelper.MockArgumentMatcher.hasStatus;
import static bv.offa.netbeans.cnd.unittest.testhelper.MockArgumentMatcher.isSuiteOfFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.MockArgumentMatcher.isTest;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestCaseSubject.assertThat;
import static com.google.common.truth.Truth.assertThat;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

@Tag("Test-Framework")
@Tag("CppUTest")
public class CppUTestTestHandlerTest
{
    private static final TestSessionInformation INFO = new TestSessionInformation();
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST;
    private static Project project;
    private static Report report;
    private CppUTestTestHandler handler;
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
        handler = new CppUTestTestHandler(INFO);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void matchesTestCaseAndDetectsIgnored()
    {
        Matcher m = checkedMatch(handler, "IGNORE_TEST(TestSuite, testCase) - 7 ms");
        assertThat(m.group(1)).isNotNull();
    }

    @Test
    public void parsesDataTestCase()
    {
        Matcher m = checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        assertThat(m.group()).isEqualTo("TEST(TestSuite, testCase) - 84 ms");
        assertThat(m.group(2)).isEqualTo("TestSuite");
        assertThat(m.group(3)).isEqualTo("testCase");
        assertThat(m.group(5)).isEqualTo("84");
    }

    @Test
    public void parsesDataTestCaseWhichFailed()
    {
        Matcher m = checkedMatch(handler, "TEST(TestSuite, testThatFailed)");
        assertThat(m.group(2)).isEqualTo("TestSuite");
        assertThat(m.group(3)).isEqualTo("testThatFailed");
        assertThat(m.group(4)).isNull();
    }

    @Test
    public void rejectsMalformedTestCase()
    {
        assertThat(handler.matches("TEST(TestSuite, testCase) - 1")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) - a")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) - abc ms")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) - ms")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) -  ms")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) - 11 ms ")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, )")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase, wrong) - 5 ms")).isFalse();
        assertThat(handler.matches("TEST(TestSuite, testCase) - 5 ms - 7 ms")).isFalse();
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

        long time = 0L;

        for( String line : input )
        {
            Matcher m = checkedMatch(handler, line);
            time += Long.valueOf(m.group(5));
        }

        assertThat(time).isEqualTo(17005L + 8L + 25L);
    }

    @Test
    public void matchesTestCaseWithOutputNoTime()
    {
        assertThat(handler.matches("TEST(TestSuite, testCase)")).isTrue();
    }

    @Test
    public void updateUIStartsTestIfFirstTest()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(manager).testStarted(session);
    }

    @Test
    public void updateUIStartsTestBeforeSuite()
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
        verify(session).addSuite(argThat(isSuiteOfFramework("TestSuite", FRAMEWORK)));
        verify(manager).displaySuiteRunning(eq(session), argThat(isSuiteOfFramework("TestSuite", FRAMEWORK)));
    }

    @Test
    public void updateUIAddsTestCase()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(isTest("TestSuite", "testCase")));
    }

    @Test
    public void updateUISetsTestCaseInformation()
    {
        checkedMatch(handler, "TEST(TestSuite, testCase) - 84 ms");
        handler.updateUI(manager, session);
        final ArgumentCaptor<Testcase> captor = ArgumentCaptor.forClass(Testcase.class);
        verify(session).addTestCase(captor.capture());
        assertThat(captor.getValue()).isTestCase("TestSuite", "testCase");
        assertThat(captor.getValue()).isFramework(FRAMEWORK);
        assertThat(captor.getValue()).isSession(session);
        assertThat(captor.getValue()).isTime(84L);
        assertThat(captor.getValue()).hasNoError();
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
        verify(session).addTestCase(argThat(isTest("TestSuite", "testCase")));
    }
}
