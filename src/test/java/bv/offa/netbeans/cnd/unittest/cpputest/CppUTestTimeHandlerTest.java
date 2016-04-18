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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

public class CppUTestTimeHandlerTest
{
    private static final TestSessionInformation DONT_CARE_INFO = new TestSessionInformation();
    private CppUTestTimeHandler handler;
    private TestSession session;
    private TestSessionInformation info;
    private CppUTestTimeHandler timeHandler;
    
    
    @Before
    public void setUp()
    {
        handler = new CppUTestTimeHandler(DONT_CARE_INFO);
        session = mock(TestSession.class);
        info = new TestSessionInformation();
        timeHandler = new CppUTestTimeHandler(info);
    }
    
    @Test
    public void matchesTime()
    {
        assertTrue(handler.matches(" - 0 ms"));
        assertTrue(handler.matches(" - 123 ms"));
    }
    
    @Test
    public void parsesDataTime()
    {
        Matcher m = handler.match(" - 0 ms");
        assertTrue(m.matches());
        assertEquals("0", m.group(1));
        m = handler.match(" - 123 ms");
        assertTrue(m.matches());
        assertEquals("123", m.group(1));
    }
    
    @Test
    public void updateUIUpdatesTime()
    {
        Matcher m = timeHandler.match(" - 123 ms");
        assertTrue(m.find());
        Testcase testCase = new CndTestCase("testCase", TestFramework.CPPUTEST, session);
        when(session.getCurrentTestCase()).thenReturn(testCase);
        timeHandler.updateUI(null, session);
        assertEquals(123L, info.getTimeTotal());
        assertEquals(123L, testCase.getTimeMillis());
    }
    
}
