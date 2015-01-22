/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015  offa
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

import bv.offa.netbeans.cnd.unittest.googletest.GoogleTestTestHandlerFactory.GoogleTestSuiteStartedHandler;
import bv.offa.netbeans.cnd.unittest.googletest.GoogleTestTestHandlerFactory.GoogleTestSuiteFinishedHandler;
import bv.offa.netbeans.cnd.unittest.googletest.GoogleTestTestHandlerFactory.GoogleTestTestStartedHandler;
import bv.offa.netbeans.cnd.unittest.googletest.GoogleTestTestHandlerFactory.GoogleTestTestFinishedHandler;
import bv.offa.netbeans.cnd.unittest.googletest.GoogleTestTestHandlerFactory.GoogleTestSessionFinishedHandler;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class GoogleTestTestHandlerFactoryTest
{
    @Test
    public void testTestSuiteStartedHandler()
    {
        final GoogleTestSuiteStartedHandler handler = new GoogleTestSuiteStartedHandler();
        
        final String input[] = new String[]
        {
            "[----------] 5 tests from TestSuite",
            "[----------] 3 tests from TestSuite2"
        };
        
        final String inputWrong[] = new String[]
        {
            "[----------] 5 tests from TestSuite (0 ms total)",
            "[----------] 3 tests from TestSuite2 (259 ms total)"
        };
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));

        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("TestSuite2", m.group(1));
    }
    
    @Test
    public void testTestSuiteFinishedHandler()
    {
        final GoogleTestSuiteFinishedHandler handler = new GoogleTestSuiteFinishedHandler();
        
        final String input[] = new String[]
        {
            "[----------] 5 tests from TestSuite (0 ms total)",
            "[----------] 3 tests from TestSuite2 (259 ms total)"
        };
        
        final String inputWrong[] = new String[]
        {
            "[----------] 5 tests from TestSuite",
            "[----------] 3 tests from TestSuite2"
        };
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));

        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals(0L, Long.parseLong(m.group(2)));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("TestSuite2", m.group(1));
        assertEquals(259L, Long.parseLong(m.group(2)));
    }
    
    @Test
    public void testTestStartedHandler()
    {
        final GoogleTestTestStartedHandler handler = new GoogleTestTestStartedHandler();
        
        final String input[] = new String[]
        {
            "[ RUN      ] TestSuite.testCase1",
            "[ RUN      ] TestSuite.testCase2"
        };
        
        final String inputWrong[] = new String[]
        {
            "[       OK ] TestSuite.testCase1 (0 ms)",
            "[  FAILED  ] TestSuite.testCase2 (45 ms)"
        };
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));

        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testCase1", m.group(2));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testCase2", m.group(2));
    }
    
    @Test
    public void testTestFinishedHandler()
    {
        final GoogleTestTestFinishedHandler handler = new GoogleTestTestFinishedHandler();
        
        final String input[] = new String[]
        {
            "[       OK ] TestSuite.testCase1 (0 ms)",
            "[  FAILED  ] TestSuite.testCase2 (45 ms)"
        };
        
        final String inputWrong[] = new String[]
        {
            "[ RUN      ] TestSuite.testCase1",
            "[ RUN      ] TestSuite.testCase2"
        };

        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));

        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("     OK", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase1", m.group(3));
        assertEquals(0L, Long.parseLong(m.group(4)));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("FAILED ", m.group(1));
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase2", m.group(3));
        assertEquals(45L, Long.parseLong(m.group(4)));
    }
    
    @Test
    public void testSessionFinishedHandler()
    {
        final GoogleTestSessionFinishedHandler handler = new GoogleTestSessionFinishedHandler();
        
        final String input[] = new String[]
        {
            "[==========] 1200 tests from 307 test cases ran. (123456 ms total)",
            "[==========] 1 tests from 2 test cases ran. (0 ms total)",
            "[==========] 4 tests from 1 test case ran. (25 ms total)"
        };
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertTrue(handler.matches(input[2]));
        
        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals(123456L, Long.parseLong(m.group(1)));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals(0L, Long.parseLong(m.group(1)));
        
        m = handler.match(input[2]);
        assertTrue(m.find());
        assertEquals(25L, Long.parseLong(m.group(1)));
    }
}
