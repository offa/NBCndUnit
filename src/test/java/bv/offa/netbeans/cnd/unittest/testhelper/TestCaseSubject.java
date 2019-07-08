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
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import com.google.common.truth.FailureMetadata;
import com.google.common.truth.Subject;
import static com.google.common.truth.Truth.assertAbout;
import javax.annotation.Nullable;
import org.checkerframework.checker.nullness.compatqual.NullableDecl;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

public final class TestCaseSubject extends Subject
{

    private static final Subject.Factory<TestCaseSubject, CndTestCase> TESTCASES_SUBJECT_FACTORY = TestCaseSubject::new;
    private final CndTestCase actual;

    private TestCaseSubject(FailureMetadata metadata, @NullableDecl CndTestCase actual)
    {
        super(metadata, actual);
        this.actual = actual;
    }

    public void hasError()
    {
        final Trouble trouble = actual.getTrouble();
        check("hasError()").that(trouble).isNotNull();
        check("hasError()").that(trouble.isError()).isTrue();
    }

    public void hasNoError()
    {
        check("hasNoError()").that(actual.getTrouble()).isNull();
    }

    public void isTime(long timeMs)
    {
        check("timeIs").that(actual.getTimeMillis()).isEqualTo(timeMs);
    }

    public void isFramework(TestFramework expected)
    {
        check("isFramework").that(actual.getFramework()).isEqualTo(expected);
    }

    public void isSession(TestSession expected)
    {
        check("isSession").that(actual.getSession()).isEqualTo(expected);
    }

    public void isTestCase(String testSuite, String testCase)
    {
        check("isTestCase").that(actual.getClassName()).isEqualTo(testSuite);
        check("isTestCase").that(actual.getName()).isEqualTo(testCase);
    }

    public void isLocation(String expected)
    {
        check("isLocation").that(actual.getLocation()).isEqualTo(expected);
    }

    public static TestCaseSubject assertThat(@Nullable CndTestCase actual)
    {
        return assertAbout(testcases()).that(actual);
    }

    public static TestCaseSubject assertThat(@Nullable Testcase actual)
    {
        return assertAbout(testcases()).that((CndTestCase) actual);
    }

    public static Subject.Factory<TestCaseSubject, CndTestCase> testcases()
    {
        return TESTCASES_SUBJECT_FACTORY;
    }
}
