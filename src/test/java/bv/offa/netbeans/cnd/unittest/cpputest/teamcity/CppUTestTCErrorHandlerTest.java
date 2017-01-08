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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.FailureInfo;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasError;
import java.util.regex.Matcher;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

public class CppUTestTCErrorHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST_TC;
    private CppUTestTCErrorHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @Before
    public void setUp()
    {
        handler = new CppUTestTCErrorHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataFailure()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testFailed name='testCase' "
                                            + "message='test/TestSuite.cpp:25' "
                                            + "details='Expected failure message']");
        assertEquals("testCase", m.group(1));
        assertEquals("test/TestSuite.cpp", m.group(2));
        assertEquals("25", m.group(3));
        assertEquals("Expected failure message", m.group(4));
    }

    @Test
    public void updateUISetsFailureInfo()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "##teamcity[testFailed name='testCase' "
                                + "message='test/TestSuite.cpp:25' "
                                + "details='Expected failure message']");
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
        FailureInfo failure = testCase.getFailureInfo();
        assertEquals("test/TestSuite.cpp", failure.getFile());
        assertEquals(25, failure.getLine());
        assertEquals("test/TestSuite.cpp:25", testCase.getTrouble().getStackTrace()[0]);
    }

}
