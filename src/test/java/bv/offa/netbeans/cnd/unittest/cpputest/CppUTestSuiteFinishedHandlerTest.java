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

import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CppUTestSuiteFinishedHandlerTest
{
    private static final TestSessionInformation dontCareInfo = new TestSessionInformation();
    private CppUTestSuiteFinishedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new CppUTestSuiteFinishedHandler(dontCareInfo);
    }
    
    @Test
    public void testMatchesSuccessfulTest()
    {
        final String inputOk = "OK (8 tests, 8 ran, 7 checks, 0 ignored, 0 filtered out, 123 ms)";
        assertTrue(handler.matches(inputOk));
    }
    
    @Test
    public void testRejectsMalformedResult()
    {
        assertFalse(handler.matches("OK"));
        assertFalse(handler.matches("OK ("));
        assertFalse(handler.matches("OK ( )"));
    }
    
    @Test
    public void testMatchesErrorWithoutEscapeColor()
    {
        final String errorMsg = "Errors (1 failures, 9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 123 ms)";
        assertTrue(handler.matches(errorMsg));
    }
    
    @Test
    public void testMatchesErrorWithEscapeColor()
    {
        final String errorMsg = "\u001B[31;1m" 
                + "Errors (1 failures, 9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 123 ms)"  
                + "\u001B[m";
        assertTrue(handler.matches(errorMsg));
    }
    
    @Test
    public void testMatchesSuccessfulWithoutEscapeColor()
    {
        final String okMsg = "OK (9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 124 ms)";
        assertTrue(handler.matches(okMsg));
    }
    
    @Test
    public void testMatchesSuccessfulWithEscapeColor()
    {
        final String okMsg = "\u001B[32;1m"
                + "OK (9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 124 ms)"
                + "\u001B[m";
        assertTrue(handler.matches(okMsg));
    }
    
}
