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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestCaseSubject.assertThat;
import static com.google.common.truth.Truth.assertThat;
import java.util.regex.Matcher;
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
        assertThat(handler.matches("[      UKN ] TestSuite.testCase (0 ms)")).isFalse();
    }

    @Test
    public void parseDataSuccessfulTestCase()
    {
        Matcher m = checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        assertThat(m.group(1)).isEqualTo("     OK");
        assertThat(m.group(2)).isEqualTo("TestSuite");
        assertThat(m.group(3)).isEqualTo("testCase");
        assertThat(m.group(4)).isEqualTo("0");
    }

    @Test
    public void parseDataSuccessfulTestCaseParameterized()
    {
        Matcher m = checkedMatch(handler, "[       OK ] withParameterImpl/TestSuite"
                                        + ".testCase/0 (0 ms)");
        assertThat(m.group(1)).isEqualTo("     OK");
        assertThat(m.group(2)).isEqualTo("withParameterImpl/TestSuite");
        assertThat(m.group(3)).isEqualTo("testCase");
        assertThat(m.group(4)).isEqualTo("0");
    }

    @Test
    public void parseDataFailTestCaseParameterized()
    {
        Matcher m = checkedMatch(handler, "[  FAILED  ] withParameterImpl/TestSuite"
                                        + ".testCase/3, where GetParam() = 0 (0 ms)");
        assertThat(m.group(1)).isEqualTo("FAILED ");
        assertThat(m.group(2)).isEqualTo("withParameterImpl/TestSuite");
        assertThat(m.group(3)).isEqualTo("testCase");
        assertThat(m.group(4)).isEqualTo("0");
    }

    @Test
    public void parseDataFailedTestCase()
    {
        Matcher m = checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        assertThat(m.group(1)).isEqualTo("FAILED ");
        assertThat(m.group(2)).isEqualTo("TestSuite");
        assertThat(m.group(3)).isEqualTo("testCase");
        assertThat(m.group(4)).isEqualTo("45");
    }

    @Test
    public void updateUISetsTroubleIfFailed()
    {
        checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        handler.updateUI((Manager) null, session);
        assertThat(testCase).hasError();
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
        assertThat(testCase).hasError();
        assertThat(testCase.getTrouble().getStackTrace()[0]).isEqualTo("abc");
    }

    @Test
    public void updateUIThrowsIfNoTestCase()
    {
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertThat(ex).isNotNull();
    }

    @Test
    public void updateUIThrowsIfNoMatchingTestCaseIsFoundByWrongSuite()
    {
        createCurrentTestCase("WrongTestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertThat(ex).isNotNull();
    }

    @Test
    public void updateUIThrowsIfNoMatchingTestCaseIsFoundByWrongCase()
    {
        createCurrentTestCase("TestSuite", "wrongTestCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (0 ms)");
        IllegalStateException ex = assertThrows(IllegalStateException.class,
                                                () -> handler.updateUI(manager, session));
        assertThat(ex).isNotNull();
    }

    @Test
    public void updateUISetsTestCaseInformation()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[       OK ] TestSuite.testCase (13 ms)");
        handler.updateUI(manager, session);

        assertThat(testCase).isTestCase("TestSuite", "testCase");
        assertThat(testCase).isFramework(FRAMEWORK);
        assertThat(testCase).isTime(13L);
        assertThat(testCase).isSession(session);
        assertThat(testCase).hasNoError();
        assertThat(testCase).isLocation("TestSuite:testCase");
    }

    @Test
    public void updateUISetsErrorOnTestFailure()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "[  FAILED  ] TestSuite.testCase (45 ms)");
        handler.updateUI(manager, session);
        assertThat(testCase).hasError();
    }
}
