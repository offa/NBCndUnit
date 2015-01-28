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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.libunittestcpp.LibunittestCppTestHandlerFactory.LibunittestCppTestFinishedHandler;
import bv.offa.netbeans.cnd.unittest.libunittestcpp.LibunittestCppTestHandlerFactory.LibunittestCppTestSessionFinishedHandler;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Rule;
import org.junit.rules.ExpectedException;

public class LibunittestCppTestHandlerFactoryTest
{
    @Rule
    public ExpectedException expectedException = ExpectedException.none();
    
    
    @Test
    public void testLibunittestCppTestFinishedHandler()
    {
        final String input[] = new String[]
        {
            "test_name::test ... [1.551e-05s] ok",
            "test_name::test ... [0.000108249s] FAIL",
            "test_name::test ... [0.100113s] ok",
            "test_name::test ... [477.100486s] ok"
        };
        
        final String inputWrong[] = new String[]
        {
            "test_name::test ... [0.000108249s] ",
            "test_name::test ... [0.000108249s] wrong",
            "test_name::test ... [abc] ok",
            "test_name::test ... [ ] ok",
            "FAIL: test_name::test [0.000108249s]"
        };
        
        LibunittestCppTestFinishedHandler handler = new LibunittestCppTestFinishedHandler();
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertTrue(handler.matches(input[2]));
        assertTrue(handler.matches(input[3]));
        
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));
        assertFalse(handler.matches(inputWrong[2]));
        assertFalse(handler.matches(inputWrong[3]));
        assertFalse(handler.matches(inputWrong[4]));
        
        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("1.551e-05", m.group(2));
        assertEquals("ok", m.group(3));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("0.000108249", m.group(2));
        assertEquals("FAIL", m.group(3));
        
        m = handler.match(input[2]);
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("0.100113", m.group(2));
        assertEquals("ok", m.group(3));
        
        m = handler.match(input[3]);
        assertTrue(m.find());
        assertEquals("test_name", m.group(1));
        assertEquals("477.100486", m.group(2));
        assertEquals("ok", m.group(3));
    }
    
    
    @Test
    public void testLibunittestCppTestSessionFinishedHandler()
    {
        final String input[] = new String[]
        {
            "Ran 60 tests in 0.100471s",
            "Ran 1 tests in 134.100471s",
            "Ran 5 tests in 0.000288703s"
        };
        
        final String inputWrong[] = new String[]
        {
            "Ran 2 tests in",
            "Ran 2 tests in "
        };
        
        LibunittestCppTestSessionFinishedHandler handler = new LibunittestCppTestSessionFinishedHandler();
        
        assertTrue(handler.matches(input[0]));
        assertTrue(handler.matches(input[1]));
        assertTrue(handler.matches(input[2]));
        
        assertFalse(handler.matches(inputWrong[0]));
        assertFalse(handler.matches(inputWrong[1]));
        
        Matcher m = handler.match(input[0]);
        assertTrue(m.find());
        assertEquals("0.100471", m.group(1));
        
        m = handler.match(input[1]);
        assertTrue(m.find());
        assertEquals("134.100471", m.group(1));
        
        m = handler.match(input[2]);
        assertTrue(m.find());
        assertEquals("0.000288703", m.group(1));
    }
    
    
    @Test
    public void testParseTimeMillis()
    {
        assertEquals(12345000L, parseTimeStoMs("12345.0"));
        assertEquals(67890000L, parseTimeStoMs("67890"));
        assertEquals(3000L, parseTimeStoMs("3"));
        assertEquals(100L, parseTimeStoMs("0.100471"));
        assertEquals(477100L, parseTimeStoMs("477.100486"));
        assertEquals(101L, parseTimeStoMs("0.1005"));
        assertEquals(0L, parseTimeStoMs("0.0"));
        assertEquals(0L, parseTimeStoMs("2.646e-05"));
        assertEquals(4L, parseTimeStoMs("427.4272e-05"));
        assertEquals(74L, parseTimeStoMs("7.4272e-02"));
        assertEquals(320000L, parseTimeStoMs("3.2e02"));
        assertEquals(0L, parseTimeStoMs("0.000144571"));
        assertEquals(1L, parseTimeStoMs("0.00144571"));
        assertEquals(234L, parseTimeStoMs("0.234108"));
        
        assertEquals(0L, parseTimeStoMs("-7.4272e-02"));
        assertEquals(0L, parseTimeStoMs("-0.100471"));
        assertEquals(0L, parseTimeStoMs("-477.100486"));
        assertEquals(0L, parseTimeStoMs("7.4272b-02"));
        assertEquals(0L, parseTimeStoMs("7.4272e"));
        assertEquals(0L, parseTimeStoMs(""));
        
        expectedException.expect(NullPointerException.class);
        parseTimeStoMs(null);
    }
    
    
    private long parseTimeStoMs(String timeSec)
    {
        return LibunittestCppTestHandlerFactory.parseSecTimeToMillis(timeSec);
    }
}
