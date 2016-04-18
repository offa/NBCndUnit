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

public class GoogleTestSuiteStartedHandlerTest
{
    private GoogleTestSuiteStartedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new GoogleTestSuiteStartedHandler();
    }
    
    @Test
    public void matchesTestSuite()
    {
        assertTrue(handler.matches("[----------] 5 tests from TestSuite"));
    }
    
    @Test
    public void parseDataTestSuite()
    {
        Matcher m = handler.match("[----------] 5 tests from TestSuite");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
    }
    
        
    @Test
    public void parseDataTestSuiteParameterized()
    {
        Matcher m = handler.match("[----------] 5 tests from withParameterImpl/TestSuite");
        assertTrue(m.find());
        assertEquals("withParameterImpl/TestSuite", m.group(1));
    }
    
    @Test
    public void matchesSingleTestSuiteParameterized()
    {
        assertTrue(handler.matches("[----------] 1 test from TestSuite"));
    }
    
    @Test
    public void parseDataSingleTestSuite()
    {
        Matcher m = handler.match("[----------] 1 test from TestSuite");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
    }
    
}
