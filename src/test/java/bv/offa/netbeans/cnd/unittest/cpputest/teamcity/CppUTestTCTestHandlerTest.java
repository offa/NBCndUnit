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

import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.frameworkIs;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.matchesTestCase;
import java.util.regex.Matcher;
import static org.hamcrest.CoreMatchers.allOf;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.hamcrest.MockitoHamcrest.argThat;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

public class CppUTestTCTestHandlerTest
{

    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST_TC;
    private CppUTestTCTestStartedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @Before
    public void setUp()
    {
        handler = new CppUTestTCTestStartedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestCase()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testStarted name='testCase']");
        assertEquals("testCase", m.group(1));
    }

    @Test
    public void updateUIAddsTestCase()
    {
        checkedMatch(handler, "##teamcity[testStarted name='testCase']");
        CndTestSuite suite = new CndTestSuite("TestSuite", FRAMEWORK);
        when(session.getCurrentSuite()).thenReturn(suite);
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(allOf(matchesTestCase("testCase", "TestSuite"),
                                                    frameworkIs(FRAMEWORK))));
    }

}
