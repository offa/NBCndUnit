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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.mockito.ArgumentMatcher;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

public class GoogleTestTestStartedHandlerTest
{
    private GoogleTestTestStartedHandler handler;
    

    @Before
    public void setUp()
    {
        handler = new GoogleTestTestStartedHandler();
    }
    
    @Test
    public void matchesTestCase()
    {
        assertTrue(handler.matches("[ RUN      ] TestSuite.testCase"));
    }
    
    @Test
    public void parseDataTestCase()
    {
        Matcher m = handler.match("[ RUN      ] TestSuite.testCase");
        assertTrue(m.find());
        assertEquals("TestSuite", m.group(1));
        assertEquals("testCase", m.group(2));
    }
    
    @Test
    public void parseDataTestCaseParameterized()
    {
        Matcher m = handler.match("[ RUN      ] withParameterImpl/TestSuite.testCase/0");
        assertTrue(m.find());
        assertEquals("withParameterImpl/TestSuite", m.group(1));
        assertEquals("testCase", m.group(2));
    }
    
    @Test
    public void updateUIAddsTestCase()
    {
        Matcher m = handler.match("[ RUN      ] TestSuite.testCase");
        assertTrue(m.find());
        TestSession session = mock(TestSession.class);
        handler.updateUI(null, session);
        verify(session).addTestCase(argThat(
                new TestCaseArgumentMatcher("testCase", TestFramework.GOOGLETEST, session)));
    }
    
    
    private static class TestCaseArgumentMatcher implements ArgumentMatcher<CndTestCase>
    {
        private final String name;
        private final TestFramework framework;
        private final TestSession session;

        public TestCaseArgumentMatcher(String name, TestFramework framework, TestSession session)
        {
            this.name = name;
            this.framework = framework;
            this.session = session;
        }
        
        
        @Override
        public boolean matches(Object argument)
        {
            final CndTestCase tc = (CndTestCase) argument;
            
            return tc.getName().equals(name) 
                    && tc.getFramework() == framework 
                    && tc.getSession().equals(session);
        }
    }
}
