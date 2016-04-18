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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

public class CppUTestErrorHandlerTest
{
    private TestSession session;
    private TestSessionInformation info;
    private CppUTestErrorHandler handler;
    
    
    @Before
    public void setUp()
    {
        session = mock(TestSession.class);
        info = new TestSessionInformation();
        handler = new CppUTestErrorHandler(info);
    }
    
    @Test
    public void matchesErrorLocationLine()
    {
        assertTrue(handler.matches("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)"));
    }
    
    @Test
    public void parsesDataErrorLocationLine()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        assertTrue(m.find());
        assertEquals(4, m.groupCount());
        assertEquals("test/TestSuite.cpp", m.group(1));
        assertEquals("37", m.group(2));
        assertEquals("TestSuite", m.group(3));
        assertEquals("testCase", m.group(4));
    }
    
    @Test
    public void updateUIDoesNothingIfNoTestCase()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        assertTrue(m.find());
        Testcase testCase = null;
        when(session.getCurrentTestCase()).thenReturn(testCase);
        handler.updateUI(null, session);
    }
    
    @Test
    public void updateUIIgnoresNotMatchingTestCase()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "wrongTestCase", session);
        handler.updateUI(null, session);
        assertNull(testCase.getTrouble());
        assertNull(testCase.getLocation());
    }
    
    @Test
    public void updateUIIgnoresNotMatchingTestSuite()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure in "
                                    + "TEST(TestSuite, testName)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("WrongTestSuite", "testCase", session);
        handler.updateUI(null, session);
        assertNull(testCase.getTrouble());
    }
    
    @Test
    public void updateUISetsTrouble()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        handler.updateUI(null, session);
        Trouble t = testCase.getTrouble();
        assertNotNull(t);
        assertTrue(t.isError());
        assertEquals("test/TestSuite.cpp:37", t.getStackTrace()[0]);
    }
    
    @Test
    public void updateUIUpdatesTrouble()
    {
        Matcher m = handler.match("test/TestSuite.cpp:37: error: Failure "
                                    + "in TEST(TestSuite, testCase)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        testCase.setTrouble(new Trouble(false));
        handler.updateUI(null, session);
        Trouble t = testCase.getTrouble();
        assertNotNull(t);
        assertTrue(t.isError());
        assertEquals("test/TestSuite.cpp:37", t.getStackTrace()[0]);
    }
    
    
    private static CndTestCase createTestCase(String suiteName, String caseName, TestSession session)
    {
        CndTestCase testCase = new CndTestCase(caseName, TestFramework.CPPUTEST, session);
        testCase.setClassName(suiteName);
        when(session.getCurrentTestCase()).thenReturn(testCase);
        
        return testCase;
    }
    
}
