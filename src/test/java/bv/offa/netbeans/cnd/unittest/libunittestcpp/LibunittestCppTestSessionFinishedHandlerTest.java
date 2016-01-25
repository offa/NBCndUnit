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
}
