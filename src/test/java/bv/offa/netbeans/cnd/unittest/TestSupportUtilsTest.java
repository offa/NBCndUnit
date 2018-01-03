/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2018  offa
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
import static org.junit.jupiter.api.Assertions.*;
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
        assertEquals(12345000L, parseMs("12345.0"));
        assertEquals(67890000L, parseMs("67890"));
        assertEquals(3000L, parseMs("3"));
        assertEquals(100L, parseMs("0.100471"));
        assertEquals(477100L, parseMs("477.100486"));
        assertEquals(101L, parseMs("0.1005"));
        assertEquals(12000L, parseMs("12.00"));
        assertEquals(10L, parseMs("0.01"));
        assertEquals(0L, parseMs("0.0"));
        assertEquals(0L, parseMs("2.646e-05"));
        assertEquals(4L, parseMs("427.4272e-05"));
        assertEquals(74L, parseMs("7.4272e-02"));
        assertEquals(320000L, parseMs("3.2e02"));
        assertEquals(0L, parseMs("0.000144571"));
        assertEquals(1L, parseMs("0.00144571"));
        assertEquals(234L, parseMs("0.234108"));
    }

    @Test
    public void parseTimeToMillisWithInvalidInput()
    {
        assertEquals(0L, parseMs("-7.4272e-02"));
        assertEquals(0L, parseMs("-0.100471"));
        assertEquals(0L, parseMs("-477.100486"));
        assertEquals(0L, parseMs("7.4272b-02"));
        assertEquals(0L, parseMs("7.4272e"));
        assertEquals(0L, parseMs(""));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseCppUTest()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.CPPUTEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "C:TEST_" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseCppUTestIgnored()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.CPPUTEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);
        testCase.setStatus(Status.SKIPPED);

        final String expected = "C:IGNORE" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestSuiteCppUTest()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.CPPUTEST);

        final String expected = "S:TEST_GROUP_CppUTestGroup" + SUITE_NAME;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testSuite));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseGoogleTest()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.GOOGLETEST, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "C:" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseGoogleTestWithParameter()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.GOOGLETEST, testSessionMock);
        testCase.setClassName("withParam/" + SUITE_NAME);

        final String expected = "C:" + SUITE_NAME + "_" + CASE_NAME + "_Test";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestSuiteGoogleTest()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.GOOGLETEST);

        final String expected = "C:" + SUITE_NAME;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testSuite));
    }

    @Test
    public void getUniqueDeclaratonNameTestSuiteGoogleTestWithParameter()
    {
        CndTestSuite testSuite = new CndTestSuite("withParam/" + SUITE_NAME, TestFramework.GOOGLETEST);

        final String expected = "C:" + SUITE_NAME;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testSuite));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseLibunittestCpp()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME, TestFramework.LIBUNITTESTCPP, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "S:" + SUITE_NAME + "::" + CASE_NAME;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestCaseLibunittestCppWithToken()
    {
        CndTestCase testCase = new CndTestCase(CASE_NAME + "::test", TestFramework.LIBUNITTESTCPP, testSessionMock);
        testCase.setClassName(SUITE_NAME);

        final String expected = "S:" + SUITE_NAME + "::" + CASE_NAME;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }

    @Test
    public void getUniqueDeclaratonNameTestSuiteLibunittestCpp()
    {
        CndTestSuite testSuite = new CndTestSuite(SUITE_NAME, TestFramework.LIBUNITTESTCPP);

        final String expected = "S:" + SUITE_NAME + "::__testcollection_child__";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testSuite));
    }

    private long parseMs(String timeSec)
    {
        return TestSupportUtils.parseTimeSecToMillis(timeSec);
    }
}
