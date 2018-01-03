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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.frameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasNoError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.sessionIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.timeIs;
import java.util.regex.Matcher;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Trouble;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

@Tag("Test-Framework")
@Tag("GoogleTest")
public class GoogleTestTestFinishedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.GOOGLETEST;
    private GoogleTestTestFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;


    @BeforeEach
    public void setUp()
    {
        handler = new GoogleTestTestFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void rejectsUnknownTestCaseResult()
    {
        assertFalse(handler.matches("[      UKN ] TestSuite.testCase (0 ms)"));
    }

    @Test
    public void parseDataSuccessfulTestCase()
    {
        Matcher m = checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        assertEquals("     OK", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }

    @Test
    public void parseDataSuccessfulTestCaseParameterized()
    {
        Matcher m = checkedMatch(handler, "[       OK ] withParameterImpl/TestSuite"
                                        + ".testCase/0 (0 ms)");
        assertEquals("     OK", m.group(1));
        assertEquals("withParameterImpl/TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }

    @Test
    public void parseDataFailTestCaseParameterized()
    {
        Matcher m = checkedMatch(handler, "[  FAILED  ] withParameterImpl/TestSuite"
                                        + ".testCase/3, where GetParam() = 0 (0 ms)");
        assertEquals("FAILED ", m.group(1));
        assertEquals("withParameterImpl/TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }

    @Test
    public void parseDataFailedTestCase()
    {
        Matcher m = checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        assertEquals("FAILED ", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("45", m.group(4));
    }

    @Test
    public void updateUISetsTroubleIfFailed()
    {
        checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        handler.updateUI((Manager) null, session);
        assertThat(testCase, hasError());
    }

    @Test
    public void updateUIUpdatesTroubleIfFailed()
    {
        checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        Trouble trouble = new Trouble(false);
        trouble.setStackTrace(new String[] { "abc" });
        testCase.setTrouble(trouble);
        handler.updateUI((Manager) null, session);
        assertThat(testCase, hasError());
        assertEquals("abc", testCase.getTrouble().getStackTrace()[0]);
    }

    @Test
    public void updateUIThrowsIfNoTestCase()
    {
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertNotNull(ex);
    }

    @Test
    public void updateUIThrowsIfNoMatchingTestCaseIsFoundByWrongSuite()
    {
        createCurrentTestCase("WrongTestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertNotNull(ex);
    }

    @Test
    public void updateUIThrowsIfNoMatchingTestCaseIsFoundByWrongCase()
    {
        createCurrentTestCase("TestSuite", "wrongTestCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertNotNull(ex);
    }

    @Test
    public void updateUISetsTestCaseInformation()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (13 ms)");
        handler.updateUI(manager, session);
        assertThat(testCase, allOf(matchesTestCase("testCase", "TestSuite"),
                                    frameworkIs(FRAMEWORK),
                                    timeIs(13l),
                                    sessionIs(session),
                                    hasNoError()));
        assertEquals("TestSuite:testCase", testCase.getLocation());
    }

    @Test
    public void updateUISetsErrorOnTestFailure()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
    }
}
