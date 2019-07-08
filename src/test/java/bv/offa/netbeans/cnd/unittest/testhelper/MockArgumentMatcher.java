/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2019  offa
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
import java.util.Objects;
import org.mockito.ArgumentMatcher;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

public final class MockArgumentMatcher
{

    private MockArgumentMatcher()
    {
    }

    public static ArgumentMatcher<Testcase> hasStatus(Status status)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getStatus() == status;
            }

            @Override
            public String toString()
            {
                return "hasStatus(): " + status.toString();
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTime(long ms)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getTimeMillis() == ms;
            }

            @Override
            public String toString()
            {
                return "isTime(): " + ms + " ms";
            }
        };
    }

    public static ArgumentMatcher<Testcase> isSession(TestSession session)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getSession().equals(session);
            }

            @Override
            public String toString()
            {
                return "isSession(): " + session; // TODO
            }

        };
    }

    public static ArgumentMatcher<Testcase> hasError()
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return Objects.requireNonNull(t.getTrouble()).isError();
            }

            @Override
            public String toString()
            {
                return "hasError()";
            }
        };
    }

    public static ArgumentMatcher<Testcase> hasNoError()
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getTrouble() == null;
            }

            @Override
            public String toString()
            {
                return "hasNoError()";
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTest(String suite, String name)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                final CndTestCase testCase = (CndTestCase) t;
                return (testCase.getName().equals(name) == true)
                        && (testCase.getClassName().equals(suite) == true);
            }

            @Override
            public String toString()
            {
                return "isTest(): " + suite + "::" + name;
            }
        };
    }

    public static ArgumentMatcher<Testcase> isFramework(TestFramework expected)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return ((CndTestCase) t).getFramework() == expected;
            }

            @Override
            public String toString()
            {
                return "isFramework(): " + expected;
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuiteFramework(TestFramework expected)
    {
        return new ArgumentMatcher<TestSuite>()
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return ((CndTestSuite) t).getFramework() == expected;
            }

            @Override
            public String toString()
            {
                return "isSuiteFramework(): " + expected;
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuite(String expected)
    {
        return new ArgumentMatcher<TestSuite>()
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return t.getName().equals(expected);
            }

            @Override
            public String toString()
            {
                return "isSuite(): " + expected;
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuiteOfFramework(String expected, TestFramework framework)
    {
        return new ArgumentMatcher<TestSuite>()
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return t.getName().equals(expected) && ((CndTestSuite) t).getFramework() == framework;
            }

            @Override
            public String toString()
            {
                return "isSuiteOfFramework(): " + expected + " - " + framework;
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTestOfFramework(String suite, String test, TestFramework framework)
    {
        return new ArgumentMatcher<Testcase>()
        {
            @Override
            public boolean matches(Testcase t)
            {
                return isTest(suite, test).matches(t) && isFramework(framework).matches(t);
            }

            @Override
            public String toString()
            {
                return "isTestOfFramework(): " + suite + "::" + test + " - " + framework;
            }
        };
    }
}
