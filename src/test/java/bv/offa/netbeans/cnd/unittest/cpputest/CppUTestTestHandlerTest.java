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

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CppUTestTestHandlerTest
{
    private static final TestSessionInformation DONT_CARE_INFO = new TestSessionInformation();
    private CppUTestTestHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new CppUTestTestHandler(DONT_CARE_INFO);
    }
    
    @Test
    public void matchesTestCase()
    {
        assertTrue(handler.matches("TEST(TestSuite, testCase) - 8 ms"));
    }
    
    @Test
    public void matchesTestCaseIgnored()
    {
        assertTrue(handler.matches("IGNORE_TEST(TestSuite, testCase) - 7 ms"));
    }
    
    @Test
    public void matchesTestCaseAndDetectsNotIgnored()
    {
        Matcher m = handler.match("TEST(TestSuite, testCase) - 8 ms");
        assertTrue(m.find());
        assertNull(m.group(1));
    }
    
    @Test
    public void matchesTestCaseAndDetectsIgnored()
    {
        Matcher m = handler.match("IGNORE_TEST(TestSuite, testCase) - 7 ms");
        assertTrue(m.find());
        assertNotNull(m.group(1));
    }
    
    @Test
    public void parsesDataTestCase()
    {
        Matcher m = handler.match("TEST(TestSuite, testCase) - 84 ms");
        assertTrue(m.find());
        assertEquals("TEST(TestSuite, testCase) - 84 ms", m.group());
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("84", m.group(5));
    }
    
    
    @Test
    public void matchesDataTestCaseWhichFailed()
    {
        assertTrue(handler.matches("TEST(TestSuite, testThatFailed)"));
    }
    
    @Test
    public void parsesDataTestCaseWhichFailed()
    {
        Matcher m = handler.match("TEST(TestSuite, testThatFailed)");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(2));
        assertEquals("testThatFailed", m.group(3));
        assertNull(m.group(4));
    }
    
    @Test
    public void rejectsMalformedTestCase()
    {
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 1"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - a"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - abc ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) -  ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 11 ms "));
        assertFalse(handler.matches("TEST(TestSuite, )"));
        assertFalse(handler.matches("TEST(TestSuite, testCase, wrong) - 5 ms"));
        assertFalse(handler.matches("TEST(TestSuite, testCase) - 5 ms - 7 ms"));
    }
    
    @Test
    public void suiteTimeParsing()
    {
        final String input[] = new String[]
        {
            "TEST(TestSuite1, testCase1) - 17005 ms",
            "TEST(TestSuite2, testCase1) - 8 ms",
            "TEST(TestSuite2, testCase2) - 25 ms",
            "TEST(TestSuite3, testCase1) - 0 ms",
        };
        
        final long expected = 17005L + 8L + 25L;
        long time = 0L;
        
        for( String line : input )
        {
            Matcher m = handler.match(line);
            assertTrue(m.find());
            time += Long.valueOf(m.group(5));
        }
        
        assertEquals(expected, time);
    }
    
    @Test
    public void matchesTestCaseWithOutputNoTime()
    {
        assertTrue(handler.matches("TEST(TestSuite, testCase)"));
    }
}
