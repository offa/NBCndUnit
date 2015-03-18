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

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class LibunittestCppTestSessionFinishedHandlerTest
{
    private LibunittestCppTestSessionFinishedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new LibunittestCppTestSessionFinishedHandler();
    }
    
    @Test
    public void testMatchesSuccessfulTest()
    {
        assertTrue(handler.matches("Ran 60 tests in 0.100471s"));
        assertTrue(handler.matches("Ran 5 tests in 127.000288703s"));
    }
    
    @Test
    public void testParseDataSuccessfulTest()
    {
        final String input = "Ran 5 tests in 127.000288703s";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("127.000288703", m.group(1));
    }
    
    @Test
    public void testRejectsMalformedTests()
    {
        assertFalse(handler.matches("Ran 2 tests in"));
        assertFalse(handler.matches("Ran 2 tests in "));
    }
    
    @Test
    public void testParseTimeToMillis()
    {
        assertEquals(12345000L, parseMs("12345.0"));
        assertEquals(67890000L, parseMs("67890"));
        assertEquals(3000L, parseMs("3"));
        assertEquals(100L, parseMs("0.100471"));
        assertEquals(477100L, parseMs("477.100486"));
        assertEquals(101L, parseMs("0.1005"));
        assertEquals(0L, parseMs("0.0"));
        assertEquals(0L, parseMs("2.646e-05"));
        assertEquals(4L, parseMs("427.4272e-05"));
        assertEquals(74L, parseMs("7.4272e-02"));
        assertEquals(320000L, parseMs("3.2e02"));
        assertEquals(0L, parseMs("0.000144571"));
        assertEquals(1L, parseMs("0.00144571"));
        assertEquals(234L, parseMs("0.234108"));
    }
    
    @Test
    public void testParseTimeToMillisWithInvalidInput()
    {
        assertEquals(0L, parseMs("-7.4272e-02"));
        assertEquals(0L, parseMs("-0.100471"));
        assertEquals(0L, parseMs("-477.100486"));
        assertEquals(0L, parseMs("7.4272b-02"));
        assertEquals(0L, parseMs("7.4272e"));
        assertEquals(0L, parseMs(""));
    }
    
    private long parseMs(String timeSec)
    {
        return LibunittestCppTestHandlerFactory.parseSecTimeToMillis(timeSec);
    }
}
