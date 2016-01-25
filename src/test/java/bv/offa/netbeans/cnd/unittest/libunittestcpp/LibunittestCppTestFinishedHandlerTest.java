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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class LibunittestCppTestFinishedHandlerTest
{
    private LibunittestCppTestFinishedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new LibunittestCppTestFinishedHandler();
    }
    
    @Test
    public void testMatchesSuccessfulTest()
    {
        assertTrue(handler.matches("test_name::testA ... [1.551e-05s] ok"));
        assertTrue(handler.matches("test_name::testB ... [0.000108249s] ok"));
        assertTrue(handler.matches("test_name::testC ... [477.100486s] ok"));
        assertTrue(handler.matches("test_suite::test_name::test_case ... [9.217e-06s] ok"));
    }
    
    @Test
    public void testParseDataSuccessfulTest()
    {
        final String input = "test_name::testA ... [1.551e-05s] ok";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("testA", m.group(2));
        assertEquals("1.551e-05", m.group(3));
        assertEquals("ok", m.group(4));
    }
    
    @Test
    public void testParseDataFailedTest()
    {
        final String input = "test_name::testB ... [0.000108249s] FAIL";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("testB", m.group(2));
        assertEquals("0.000108249", m.group(3));
        assertEquals("FAIL", m.group(4));
    }
    
    @Test
    public void testMatchesIgnoredTestCase()
    {
        assertTrue(handler.matches("TestSuite::testExample::test ... [0s] SKIP A message"));
    }
    
    @Test
    public void testParseDataIgnoredTestCase()
    {
        final String input = "TestSuite::testExample::test ... [0s] SKIP A message";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testExample::test", m.group(2));
        assertEquals("0", m.group(3));
        assertEquals("SKIP", m.group(4));
    }
}
