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
package bv.offa.netbeans.cnd.unittest.testhelper;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

public final class TestMatcher
{
    private TestMatcher()
    {
        /* Empty */
    }

    
    public static Matcher<Trouble> isError()
    {
        return new BaseMatcher<Trouble>()
        {
            @Override
            public boolean matches(Object item)
            {
                final Trouble trouble = ((Trouble) item);
                return ( trouble != null ) && ( trouble.isError() == true );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Error");
            }
        };
    }
    
    
    public static Matcher<Trouble> isNoError()
    {
        return new BaseMatcher<Trouble>()
        {
            @Override
            public boolean matches(Object item)
            {
                return ( ((Trouble) item) == null );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("No Error (trouble == null)");
            }
        };
    }
    
    
    public static Matcher<CndTestCase> matchesTestCase(final String name, final String suite)
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return ( testCase.getName().equals(name) == true )
                        && ( testCase.getClassName().equals(suite) == true );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case Name: ")
                            .appendValue(name)
                            .appendText(", Suite: ")
                            .appendValue(suite);
            }
        };
    }
    
    
    public static Matcher<CndTestCase> frameworkIs(final TestFramework framework)
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return ( testCase.getFramework() == framework );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case Framework: ")
                            .appendValue(framework.toString());
            }
        };
    }
    
    
    public static Matcher<CndTestCase> timeIs(final long timeMs)
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return ( testCase.getTimeMillis() == timeMs );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case Time: ")
                            .appendValue(timeMs)
                            .appendText(" ms");
            }
        };
    }
        
    
    public static Matcher<CndTestCase> sessionIs(final TestSession session)
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return ( testCase.getSession().equals(session) == true ) ;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case Session: ").appendValue(session);
            }
        };
    }
    
    
    public static Matcher<CndTestCase> hasError()
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return isError().matches(testCase.getTrouble());
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case has Error");
            }
        };
    }
    
    
    public static Matcher<CndTestCase> hasNoError()
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return isNoError().matches(testCase.getTrouble());
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case no Error");
            }
        };
    }

    
    public static Matcher<CndTestCase> hasStatus(final Status status)
    {
        return new BaseMatcher<CndTestCase>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestCase testCase = (CndTestCase) item;
                return testCase.getStatus() == status;
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Case Status: ").appendValue(status);
            }
        };
    }
    
    
    public static Matcher<CndTestSuite> matchesTestSuite(final String name)
    {
        return new BaseMatcher<CndTestSuite>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestSuite testSuite = (CndTestSuite) item;
                return ( testSuite.getName().equals(name) == true );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Suite Name: ").appendValue(name);
            }
        };
    }
    
    
    public static Matcher<CndTestSuite> suiteFrameworkIs(final TestFramework framework)
    {
        return new BaseMatcher<CndTestSuite>()
        {
            @Override
            public boolean matches(Object item)
            {
                final CndTestSuite testSuite = (CndTestSuite) item;
                return ( testSuite.getFramework() == framework );
            }

            @Override
            public void describeTo(Description description)
            {
                description.appendText("Test Suite Framework: ").appendValue(framework);
            }
        };
    }
    
}
