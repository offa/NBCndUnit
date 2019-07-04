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

package bv.offa.netbeans.cnd.unittest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static com.google.common.truth.Truth.assertThat;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

@Tag("util")
public class TestSupportUtilsTest
{
    private static final String CASE_NAME = "testCase";
    private static final String SUITE_NAME = "TestSuite";
    private static TestSession testSessionMock;
    private static Project projectMock;

    @BeforeAll
    public static void setUpClass()
    {
        testSessionMock = mock(TestSession.class);
        projectMock = mock(Project.class);
        when(projectMock.getProjectDirectory())
                .thenReturn(FileUtil.createMemoryFileSystem().getRoot());
        when(projectMock.getLookup()).thenReturn(Lookup.EMPTY);
    }


    @Test
    public void parseTimeToMillis()
    {
        assertThat(parseMs("12345.0")).isEqualTo(12345000L);
        assertThat(parseMs("67890")).isEqualTo(67890000L);
        assertThat(parseMs("3")).isEqualTo(3000L);
        assertThat(parseMs("0.100471")).isEqualTo(100L);
        assertThat(parseMs("477.100486")).isEqualTo(477100L);
        assertThat(parseMs("0.1005")).isEqualTo(101L);
        assertThat(parseMs("12.00")).isEqualTo(12000L);
        assertThat(parseMs("0.01")).isEqualTo(10L);
        assertThat(parseMs("0.0")).isEqualTo(0L);
        assertThat(parseMs("2.646e-05")).isEqualTo(0L);
        assertThat(parseMs("427.4272e-05")).isEqualTo(4L);
        assertThat(parseMs("7.4272e-02")).isEqualTo(74L);
        assertThat(parseMs("3.2e02")).isEqualTo(320000L);
        assertThat(parseMs("0.000144571")).isEqualTo(0L);
        assertThat(parseMs("0.00144571")).isEqualTo(1L);
        assertThat(parseMs("0.234108")).isEqualTo(234L);
    }

    @Test
    public void parseTimeToMillisWithInvalidInput()
    {
        assertThat(parseMs("-7.4272e-02")).isEqualTo(0L);
        assertThat(parseMs("-0.100471")).isEqualTo(0L);
        assertThat(parseMs("-477.100486")).isEqualTo(0L);
        assertThat(parseMs("7.4272b-02")).isEqualTo(0L);
        assertThat(parseMs("7.4272e")).isEqualTo(0L);
        assertThat(parseMs("")).isEqualTo(0L);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseCppUTest()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.CPPUTEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "C:TEST_" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseCppUTestIgnored()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.CPPUTEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);
        testCase.setStatus(Status.SKIPPED);

        final String expected = "C:IGNORE" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestSuiteCppUTest()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.CPPUTEST);

        final String expected = "S:TEST_GROUP_CppUTestGroup" + SUITE_NAME;
        assertThat(TestSupportUtils.getUniqueDeclarationName(testSuite)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseGoogleTest()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.GOOGLETEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "C:" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseGoogleTestWithParameter()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.GOOGLETEST, testSessionMock);
        testCase.setClassName("withParam/" + SUITE_NAME);

        final String expected = "C:" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestSuiteGoogleTest()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.GOOGLETEST);

        final String expected = "C:" + SUITE_NAME;
        assertThat(TestSupportUtils.getUniqueDeclarationName(testSuite)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestSuiteGoogleTestWithParameter()
    {
        CndTestSuite testSuite = new CndTestSuite("withParam/" + SUITE_NAME, TestFramework.GOOGLETEST);

        final String expected = "C:" + SUITE_NAME;
        assertThat(TestSupportUtils.getUniqueDeclarationName(testSuite)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseLibunittestCpp()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.LIBUNITTESTCPP, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "S:" + SUITE_NAME + "::" + CASE_NAME;
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestCaseLibunittestCppWithToken()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME + "::test", TestFramework.LIBUNITTESTCPP, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "S:" + SUITE_NAME + "::" + CASE_NAME;
        assertThat(TestSupportUtils.getUniqueDeclarationName(testCase)).isEqualTo(expected);
    }

    @Test
    public void getUniqueDeclarationNameTestSuiteLibunittestCpp()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.LIBUNITTESTCPP);

        final String expected = "S:" + SUITE_NAME + "::__testcollection_child__";
        assertThat(TestSupportUtils.getUniqueDeclarationName(testSuite)).isEqualTo(expected);
    }

    private long parseMs(String timeSec)
    {
        return TestSupportUtils.parseTimeSecToMillis(timeSec);
    }
}
