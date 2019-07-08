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
package bv.offa.netbeans.cnd.unittest.googletest;

import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestSuite;
import static bv.offa.netbeans.cnd.unittest.testhelper.MockArgumentMatcher.isSuite;
import static com.google.common.truth.Truth.assertThat;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InOrder;
import static org.mockito.Mockito.inOrder;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

@Tag("Test-Framework")
@Tag("GoogleTest")
public class GoogleTestSuiteStartedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.GOOGLETEST;
    private static Project project;
    private static Report report;
    private GoogleTestSuiteStartedHandler handler;
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
        handler = new GoogleTestSuiteStartedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestSuite()
    {
        Matcher m = checkedMatch(handler, "[----------] 5 tests from TestSuite");
        assertThat(m.group(1)).isEqualTo("TestSuite");
    }

    @Test
    public void parseDataTestSuiteParameterized()
    {
        Matcher m = checkedMatch(handler, "[----------] 5 tests from withParameterImpl/TestSuite");
        assertThat(m.group(1)).isEqualTo("withParameterImpl/TestSuite");
    }

    @Test
    public void parseDataSingleTestSuite()
    {
        Matcher m = checkedMatch(handler, "[----------] 1 test from TestSuite");
        assertThat(m.group(1)).isEqualTo("TestSuite");
    }

    @Test
    public void updateUIStartsTestIfFirstTest()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        handler.updateUI(manager, session);
        verify(manager).testStarted(session);
    }

    @Test
    public void updateUIStartsTestBeforeSuite()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        handler.updateUI(manager, session);
        InOrder inOrder = inOrder(manager);
        inOrder.verify(manager).testStarted(any(TestSession.class));
        inOrder.verify(manager).displaySuiteRunning(any(TestSession.class), any(CndTestSuite.class));
    }

    @Test
    public void updateUIDisplaysReportIfNotFirstTest()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        when(session.getReport(anyLong())).thenReturn(report);
        handler.updateUI(manager, session);
        handler.updateUI(manager, session);
        verify(manager).displayReport(session, report);
    }

    @Test
    public void updateUIStartsNewSuiteIfFirstSuite()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(isSuite("TestSuite")));
        verify(manager).displaySuiteRunning(eq(session), argThat(isSuite("TestSuite")));
    }

    @Test
    public void updateUIStartsNewSuiteIfNewSuiteStarted()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        CndTestSuite suite = new CndTestSuite("TestSuiteFirst", TestFramework.GOOGLETEST);
        when(session.getCurrentSuite()).thenReturn(suite);
        handler.updateUI(manager, session);
        verify(session).addSuite(argThat(isSuite("TestSuite")));
        verify(manager).displaySuiteRunning(eq(session), argThat(isSuite("TestSuite")));
    }

    @Test
    public void updateUIDoesNothingIfSameSuite()
    {
        checkedMatch(handler, "[----------] 1 test from TestSuite");
        CndTestSuite suite = createCurrentTestSuite("TestSuite", FRAMEWORK, session);
        when(session.getCurrentSuite()).thenReturn(suite);
        handler.updateUI(manager, session);
        verify(session, never()).addSuite(any(CndTestSuite.class));
        verify(manager, never()).displaySuiteRunning(any(TestSession.class), any(TestSuite.class));
    }
}
