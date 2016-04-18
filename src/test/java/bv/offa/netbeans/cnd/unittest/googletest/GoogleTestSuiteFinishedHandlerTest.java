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

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GoogleTestSuiteFinishedHandlerTest
{
    private GoogleTestSuiteFinishedHandler handler;
    

    @Before
    public void setUp()
    {
        handler = new GoogleTestSuiteFinishedHandler();
    }
    
    @Test
    public void matchesSuccessfulTestSuite()
    {
        assertTrue(handler.matches("[----------] 3 tests from TestSuite (259 ms total)"));
    }
    
    @Test
    public void parseDataSuccessfulTestSuite()
    {
        Matcher m = handler.match("[----------] 3 tests from TestSuite (259 ms total)");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("259", m.group(2));
    }
    
    @Test
    public void matchesSingleTestSuite()
    {
        assertTrue(handler.matches("[----------] 1 test from TestSuite (123 ms total)"));
    }
    
    @Test
    public void parseDataSingleTestSuite()
    {
        Matcher m = handler.match("[----------] 1 test from TestSuite (123 ms total)");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("123", m.group(2));
    }
    
}
