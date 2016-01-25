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
    public void testMatchesTestCase()
    {
        final String input = "TEST(TestSuite, testCase) - 8 ms";
        assertTrue(handler.matches(input));
    }
    
    @Test
    public void testMatchesTestCaseIgnored()
    {
        final String input = "IGNORE_TEST(TestSuite, testCase) - 7 ms";
        assertTrue(handler.matches(input));
    }
    
    @Test
    public void testMatchesTestCaseAndDetectsNotIgnored()
    {
        final String input = "TEST(TestSuite, testCase) - 8 ms";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertNull(m.group(1));
    }
    
    @Test
    public void testMatchesTestCaseAndDetectsIgnored()
    {
        final String input = "IGNORE_TEST(TestSuite, testCase) - 7 ms";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertNotNull(m.group(1));
    }
    
    @Test
    public void testParsesDataTestCase()
    {
        final String input = "TEST(TestSuite, testCase) - 84 ms";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("TEST(TestSuite, testCase) - 84 ms", m.group());
        assertEquals("TestSuite", m.group(2));
        assertEquals("testCase", m.group(3));
        assertEquals("84", m.group(5));
    }
    
    
    @Test
    public void testMatchesDataTestCaseWhichFailed()
    {
        final String input = "TEST(TestSuite, testThatFailed)";
        assertTrue(handler.matches(input));
    }
    
    @Test
    public void testParsesDataTestCaseWhichFailed()
    {
        final String input = "TEST(TestSuite, testThatFailed)";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(2));
        assertEquals("testThatFailed", m.group(3));
        assertNull(m.group(4));
    }
    
    @Test
    public void testRejectsMalformedTestCase()
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
    public void testSuiteTimeParsing()
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
    public void testMatchesTestCaseWithOutputNoTime()
    {
        final String input = "TEST(TestSuite, testCase)";
        assertTrue(handler.matches(input));
    }
}
