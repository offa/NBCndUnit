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

package bv.offa.netbeans.cnd.unittest.googletest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Rule;
import org.junit.rules.ExpectedException;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

public class GoogleTestTestFinishedHandlerTest
{
    @Rule
    public ExpectedException exception = ExpectedException.none();
    private GoogleTestTestFinishedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new GoogleTestTestFinishedHandler();
    }
    
    @Test
    public void testMatchesSuccessfulTestCase()
    {
        assertTrue(handler.matches("[       OK ] TestSuite.testCase (0 ms)"));
    }
    
    @Test
    public void testMatchesFailedTestCase()
    {
        assertTrue(handler.matches("[  FAILED  ] TestSuite.testCase (45 ms)"));
    }
    
    @Test
    public void testRejectsUnknownTestCaseResult()
    {
        assertFalse(handler.matches("[      UKN ] TestSuite.testCase (0 ms)"));
    }
    
    @Test
    public void testParseDataSuccessfulTestCase()
    {
        final String input = "[       OK ] TestSuite.testCase (0 ms)";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("     OK", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }
    
    @Test
    public void testParseDataFailedTestCase()
    {
        final String input = "[  FAILED  ] TestSuite.testCase (45 ms)";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("FAILED ", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("45", m.group(4));
    }
    
    @Test
    public void testUpdateUIThrowsIfNoTest()
    {
        final String input = "[       OK ] TestSuite.testCase (0 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);

        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void testUpdateUIThrownsIfNotMatchingTestCase()
    {
        final String input = "[       OK ] TestSuite.testCase (0 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);
        Testcase testCase = createTestCase("TestSuite", "testCaseWrong", session);
        when(session.getCurrentTestCase()).thenReturn(testCase);

        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void testUpdateUIIgnoresNotMatchingTestSuite()
    {
        final String input = "[       OK ] TestSuite.testCase (0 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);
        Testcase testCase = createTestCase("TestSuiteWrong", "testCase", session);
        when(session.getCurrentTestCase()).thenReturn(testCase);

        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void testUpdateUIDoesNothingIfSuccessful()
    {
        final String input = "[       OK ] TestSuite.testCase (0 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        when(session.getCurrentTestCase()).thenReturn(testCase);

        handler.updateUI(null, session);
        
        assertNull(testCase.getTrouble());
    }
    
    @Test
    public void testUpdateUISetsTroubleIfFailed()
    {
        final String input = "[  FAILED  ] TestSuite.testCase (45 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        when(session.getCurrentTestCase()).thenReturn(testCase);

        handler.updateUI(null, session);
        Trouble t = testCase.getTrouble();
        
        assertNotNull(t);
        assertTrue(t.isError());
        assertEquals("TestSuite:testCase", t.getStackTrace()[0]);
    }
    
    @Test
    public void testUpdateUIUpdatesTroubleIfFailed()
    {
        final String input = "[  FAILED  ] TestSuite.testCase (45 ms)";
        Matcher m = handler.match(input);
        assertTrue(m.find());

        TestSession session = mock(TestSession.class);
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        testCase.setTrouble(new Trouble(false));
        when(session.getCurrentTestCase()).thenReturn(testCase);

        handler.updateUI(null, session);
        Trouble t = testCase.getTrouble();
        
        assertNotNull(t);
        assertTrue(t.isError());
        assertEquals("TestSuite:testCase", t.getStackTrace()[0]);
    }
    
    private static CndTestCase createTestCase(String suiteName, String caseName, TestSession session)
    {
        CndTestCase testCase = new CndTestCase(caseName, TestFramework.CPPUTEST, session);
        testCase.setClassName(suiteName);
        
        return testCase;
    }
}
