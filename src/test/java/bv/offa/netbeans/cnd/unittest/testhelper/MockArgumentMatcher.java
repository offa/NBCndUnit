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
        return (Testcase t) -> t.getStatus() == status;
    }

    public static ArgumentMatcher<Testcase> isTime(long ms)
    {
        return (Testcase t) -> t.getTimeMillis() == ms;
    }

    public static ArgumentMatcher<Testcase> isSession(TestSession session)
    {
        return (Testcase t) -> t.getSession().equals(session);
    }

    public static ArgumentMatcher<Testcase> hasError()
    {
        return (Testcase t) -> Objects.requireNonNull(t.getTrouble()).isError();
    }

    public static ArgumentMatcher<Testcase> hasNoError()
    {
        return (Testcase t) -> t.getTrouble() == null;
    }

    public static ArgumentMatcher<Testcase> isTest(String suite, String name)
    {
        return (Testcase t) ->
        {
            final CndTestCase testCase = (CndTestCase) t;
            return (testCase.getName().equals(name) == true)
                    && (testCase.getClassName().equals(suite) == true);
        };
    }

    public static ArgumentMatcher<Testcase> isFramework(TestFramework expected)
    {
        return (Testcase t) -> ((CndTestCase) t).getFramework() == expected;
    }

    public static ArgumentMatcher<TestSuite> isSuiteFramework(TestFramework expected)
    {
        return (TestSuite t) -> ((CndTestSuite) t).getFramework() == expected;
    }

    public static ArgumentMatcher<TestSuite> isSuite(String expected)
    {
        return (TestSuite t) -> t.getName().equals(expected);
    }

    public static ArgumentMatcher<TestSuite> isSuiteOfFramework(String expected, TestFramework framework)
    {
        return (TestSuite t) -> t.getName().equals(expected) && ((CndTestSuite) t).getFramework() == framework;
    }

    public static ArgumentMatcher<Testcase> isTestOfFramework(String suite, String test, TestFramework framework)
    {
        return (Testcase t) ->
        {
            return isTest(suite, test).matches(t) && isFramework(framework).matches(t);
        };
    }
}
