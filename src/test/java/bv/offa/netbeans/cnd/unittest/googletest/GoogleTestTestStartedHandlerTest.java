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

package bv.offa.netbeans.cnd.unittest.googletest;

import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;

public class GoogleTestTestStartedHandlerTest
{
    private GoogleTestTestStartedHandler handler;
    

    @Before
    public void setUp()
    {
        handler = new GoogleTestTestStartedHandler();
    }
    
    @Test
    public void testMatchesTestCase()
    {
        assertTrue(handler.matches("[ RUN      ] TestSuite.testCase"));
    }
    
    @Test
    public void testParseDataTestCase()
    {
        final String input = "[ RUN      ] TestSuite.testCase";
        Matcher m = handler.match(input);
        
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testCase", m.group(2));
    }
}
