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

public class GoogleTestSessionFinishedHandlerTest
{
    private GoogleTestSessionFinishedHandler handler;
    
    
    @Before
    public void setUp()
    {
        handler = new GoogleTestSessionFinishedHandler();
    }
    
    @Test
    public void matchesTestSessionResult()
    {
        assertTrue(handler.matches("[==========] 1200 tests from 307 test cases ran. (1234 ms total)"));
    }
    
    @Test
    public void parseDataTestSession()
    {
        Matcher m = handler.match("[==========] 1200 tests from 307 test cases ran. (1234 ms total)");
        assertTrue(m.find());
        assertEquals("1234", m.group(1));
    }
    
    @Test
    public void matchesSingleTests()
    {
        assertTrue(handler.matches("[==========] 1 test from 1 test case ran. (3 ms total)"));
    }
    
    @Test
    public void parseDataSingleTests()
    {
        Matcher m = handler.match("[==========] 1 test from 1 test case ran. (3 ms total)");
        assertTrue(m.find());
        assertEquals("3", m.group(1));
    }
}
