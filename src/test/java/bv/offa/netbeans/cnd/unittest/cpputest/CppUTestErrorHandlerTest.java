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

package bv.offa.netbeans.cnd.unittest.cpputest;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class CppUTestErrorHandlerTest
{
    private static final TestSessionInformation DONT_CARE_INFO = new TestSessionInformation();
    private CppUTestErrorHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new CppUTestErrorHandler(DONT_CARE_INFO);
    }
    
    @Test
    public void testMatchesErrorLocationLine()
    {
        final String line = "test/SuiteName.cpp:37: error: Failure in TEST(SuiteName, testName)";
        assertTrue(handler.matches(line));
    }
    
    @Test
    public void testParsesDataErrorLocationLine()
    {
        final String line = "test/SuiteName.cpp:37: error: Failure in TEST(SuiteName, testName)";
        Matcher m = handler.match(line);
        
        assertTrue(m.find());
        assertEquals(4, m.groupCount());
        assertEquals("test/SuiteName.cpp", m.group(1));
        assertEquals("37", m.group(2));
        assertEquals("SuiteName", m.group(3));
        assertEquals("testName", m.group(4));
    }
}
