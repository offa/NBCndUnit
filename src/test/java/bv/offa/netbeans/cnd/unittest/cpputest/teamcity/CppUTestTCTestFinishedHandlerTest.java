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

package bv.offa.netbeans.cnd.unittest.cpputest.teamcity;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import bv.offa.netbeans.cnd.unittest.cpputest.TestSessionInformation;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestSuite;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.frameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasNoError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.sessionIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.timeIs;
import java.util.regex.Matcher;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

public class CppUTestTCTestFinishedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST_TC;
    private TestSessionInformation info;
    private CppUTestTCTestFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;


    @Before
    public void setUp()
    {
        info = new TestSessionInformation();
        handler = new CppUTestTCTestFinishedHandler(info);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataSuccessfulTestCase()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testFinished name='testCase' duration='123']");
        assertEquals("testCase", m.group(1));
        assertEquals("123", m.group(2));
    }

    @Test
    public void updateUISetsTestCaseInformation()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "##teamcity[testFinished name='testCase' duration='123']");
        createCurrentTestSuite("TestSuite", FRAMEWORK, session);
        handler.updateUI(manager, session);
        assertThat(testCase, allOf(matchesTestCase("testCase", "TestSuite"),
                                    frameworkIs(FRAMEWORK),
                                    timeIs(123l),
                                    sessionIs(session),
                                    hasNoError()));
        assertEquals("TestSuite:testCase", testCase.getLocation());
        assertEquals(123l, info.getTimeTotal());
    }
}
