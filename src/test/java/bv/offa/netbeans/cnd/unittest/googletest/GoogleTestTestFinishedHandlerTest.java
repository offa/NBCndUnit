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
    private TestSession session;
    
    
    @Before
    public void setUp()
    {
        handler = new GoogleTestTestFinishedHandler();
        session = mock(TestSession.class);
    }
    
    @Test
    public void matchesSuccessfulTestCase()
    {
        assertTrue(handler.matches("[       OK ] TestSuite.testCase (0 ms)"));
    }
    
    @Test
    public void matchesFailedTestCase()
    {
        assertTrue(handler.matches("[  FAILED  ] TestSuite.testCase (45 ms)"));
    }
    
    @Test
    public void rejectsUnknownTestCaseResult()
    {
        assertFalse(handler.matches("[      UKN ] TestSuite.testCase (0 ms)"));
    }
    
    @Test
    public void parseDataSuccessfulTestCase()
    {
        Matcher m = handler.match("[       OK ] TestSuite.testCase (0 ms)");
        assertTrue(m.find());
        assertEquals("     OK", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }
    
    @Test
    public void parseDataSuccessfulTestCaseParameterized()
    {
        Matcher m = handler.match("[       OK ] withParameterImpl/TestSuite"
                                    + ".testCase/0 (0 ms)");
        assertTrue(m.find());
        assertEquals("     OK", m.group(1));
        assertEquals("withParameterImpl/TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }
    
    @Test
    public void parseDataFailTestCaseParameterized()
    {
        Matcher m = handler.match("[  FAILED  ] withParameterImpl/TestSuite"
                                    + ".testCase/3, where GetParam() = 0 (0 ms)");
        assertTrue(m.find());
        assertEquals("FAILED ", m.group(1));
        assertEquals("withParameterImpl/TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("0", m.group(4));
    }
    
    @Test
    public void parseDataFailedTestCase()
    {
        Matcher m = handler.match("[  FAILED  ] TestSuite.testCase (45 ms)");
        assertTrue(m.find());
        assertEquals("FAILED ", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("45", m.group(4));
    }
    
    @Test
    public void updateUIThrowsIfNoTest()
    {
        Matcher m = handler.match("[       OK ] TestSuite.testCase (0 ms)");
        assertTrue(m.find());
        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void updateUIThrownsIfNotMatchingTestCase()
    {
        Matcher m = handler.match("[       OK ] withParameterImpl/"
                                    + "TestSuite.withParameter/0 (0 ms)");
        assertTrue(m.find());
        createTestCase("TestSuite", "testCaseWrong", session);
        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void updateUIIgnoresNotMatchingTestSuite()
    {
        Matcher m = handler.match("[       OK ] TestSuite.testCase (0 ms)");
        assertTrue(m.find());
        createTestCase("TestSuiteWrong", "testCase", session);
        exception.expect(IllegalStateException.class);
        handler.updateUI(null, session);
    }
    
    @Test
    public void updateUIDoesNothingIfSuccessful()
    {
        Matcher m = handler.match("[       OK ] TestSuite.testCase (0 ms)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        handler.updateUI(null, session);
        assertNull(testCase.getTrouble());
    }
    
    @Test
    public void updateUISetsTroubleIfFailed()
    {
        Matcher m = handler.match("[  FAILED  ] TestSuite.testCase (45 ms)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        handler.updateUI(null, session);
        Trouble t = testCase.getTrouble();
        assertNotNull(t);
        assertTrue(t.isError());
        assertEquals("TestSuite:testCase", t.getStackTrace()[0]);
    }
    
    @Test
    public void updateUIUpdatesTroubleIfFailed()
    {
        Matcher m = handler.match("[  FAILED  ] TestSuite.testCase (45 ms)");
        assertTrue(m.find());
        Testcase testCase = createTestCase("TestSuite", "testCase", session);
        testCase.setTrouble(new Trouble(false));
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
        when(session.getCurrentTestCase()).thenReturn(testCase);
        
        return testCase;
    }
}
