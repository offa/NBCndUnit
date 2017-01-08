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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.FailureInfo;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasError;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasNoError;
import java.util.regex.Matcher;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

public class CppUTestErrorHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST;
    private TestSessionInformation info;
    private CppUTestErrorHandler handler;
    private TestSession session;
    private ManagerAdapter manager;
    
    @Before
    public void setUp()
    {
        info = new TestSessionInformation();
        handler = new CppUTestErrorHandler(info);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
        
    }
    
    @Test
    public void parsesDataErrorLocationLine()
    {
        Matcher m = checkedMatch(handler, "test/TestSuite.cpp:37: error: "
                                        + "Failure in TEST(TestSuite, testCase)");
        assertEquals(4, m.groupCount());
        assertEquals("test/TestSuite.cpp", m.group(1));
        assertEquals("37", m.group(2));
        assertEquals("TestSuite", m.group(3));
        assertEquals("testCase", m.group(4));
    }
    
    @Test
    public void updateUIDoesNothingIfNoTestCase()
    {
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        when(session.getCurrentTestCase()).thenReturn(null);
        handler.updateUI(manager, session);
    }
    
    @Test
    public void updateUIIgnoresNotMatchingTestCase()
    {
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "wrongTestCase", FRAMEWORK, session);
        handler.updateUI(manager, session);
        assertThat(testCase, hasNoError());
        assertNull(testCase.getLocation());
    }
    
    @Test
    public void updateUIIgnoresNotMatchingTestSuite()
    {
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure in "
                                    + "TEST(TestSuite, testName)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        handler.updateUI(manager, session);
        assertThat(testCase, hasNoError());
    }
    
    @Test
    public void updateUISetsTrouble()
    {
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
    }
    
    @Test
    public void updateUIUpdatesTrouble()
    {
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        testCase.setTrouble(new Trouble(false));
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
    }
    
    @Test
    public void updateUISetsFailureInfo()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
        FailureInfo failure = testCase.getFailureInfo();
        assertEquals("test/TestSuite.cpp", failure.getFile());
        assertEquals(37, failure.getLine());
        assertEquals("test/TestSuite.cpp:37", testCase.getTrouble().getStackTrace()[0]);
    }

}
