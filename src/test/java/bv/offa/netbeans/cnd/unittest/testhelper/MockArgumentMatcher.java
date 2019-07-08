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
        return new ArgumentMatcherImpl<Testcase>("hasStatus()", status.toString())
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getStatus() == status;
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTime(long ms)
    {
        return new ArgumentMatcherImpl<Testcase>("isTime()", ms + " ms")
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getTimeMillis() == ms;
            }
        };
    }

    public static ArgumentMatcher<Testcase> isSession(TestSession session)
    {
        return new ArgumentMatcherImpl<Testcase>("isSession()", session)
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getSession().equals(session);
            }
        };
    }

    public static ArgumentMatcher<Testcase> hasError()
    {
        return new ArgumentMatcherImpl<Testcase>("hasError()")
        {
            @Override
            public boolean matches(Testcase t)
            {
                return Objects.requireNonNull(t.getTrouble()).isError();
            }
        };
    }

    public static ArgumentMatcher<Testcase> hasNoError()
    {
        return new ArgumentMatcherImpl<Testcase>("hasNoError()")
        {
            @Override
            public boolean matches(Testcase t)
            {
                return t.getTrouble() == null;
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTest(String suite, String name)
    {
        return new ArgumentMatcherImpl<Testcase>("isTest()", suite + "::" + name)
        {
            @Override
            public boolean matches(Testcase t)
            {
                final CndTestCase testCase = (CndTestCase) t;
                return (testCase.getName().equals(name) == true)
                        && (testCase.getClassName().equals(suite) == true);
            }
        };
    }

    public static ArgumentMatcher<Testcase> isFramework(TestFramework framework)
    {
        return new ArgumentMatcherImpl<Testcase>("isFramework()", framework)
        {
            @Override
            public boolean matches(Testcase t)
            {
                return ((CndTestCase) t).getFramework() == framework;
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuiteFramework(TestFramework framework)
    {
        return new ArgumentMatcherImpl<TestSuite>("isSuiteFramework()", framework)
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return ((CndTestSuite) t).getFramework() == framework;
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuite(String suite)
    {
        return new ArgumentMatcherImpl<TestSuite>("isSuite()", suite)
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return t.getName().equals(suite);
            }
        };
    }

    public static ArgumentMatcher<TestSuite> isSuiteOfFramework(String suite, TestFramework framework)
    {
        return new ArgumentMatcherImpl<TestSuite>("isSuiteOfFramework()", suite + " - " + framework)
        {
            @Override
            public boolean matches(TestSuite t)
            {
                return isSuite(suite).matches(t) && isSuiteFramework(framework).matches(t);
            }
        };
    }

    public static ArgumentMatcher<Testcase> isTestOfFramework(String suite, String test, TestFramework framework)
    {
        return new ArgumentMatcherImpl<Testcase>("isTestframework()", suite + "::" + test + " - " + framework)
        {
            @Override
            public boolean matches(Testcase t)
            {
                return isTest(suite, test).matches(t) && isFramework(framework).matches(t);
            }
        };
    }

    private static abstract class ArgumentMatcherImpl<T> implements ArgumentMatcher<T>
    {

        private final String name;
        private final String detail;

        public ArgumentMatcherImpl(String name, Object detail)
        {
            this.name = name;
            this.detail = Objects.toString(detail, "");
        }

        public ArgumentMatcherImpl(String name)
        {
            this(name, null);
        }

        @Override
        public abstract boolean matches(T t);

        @Override
        public String toString()
        {
            return name + (detail.isEmpty() ? "" : ": " + detail);
        }

    }
}
