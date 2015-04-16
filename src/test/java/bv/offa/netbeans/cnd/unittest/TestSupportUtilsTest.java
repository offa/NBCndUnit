/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015  offa
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
import bv.offa.netbeans.cnd.unittest.ui.TestRunnerUINodeFactory;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.BeforeClass;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.TestRunnerNodeFactory;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSession.SessionType;
import org.openide.filesystems.FileUtil;
import org.openide.util.Lookup;

public class TestSupportUtilsTest
{
    private static Project projectMock;
    
    @BeforeClass
    public static void setUpClass()
    {
        projectMock = mock(Project.class);
        when(projectMock.getProjectDirectory()).thenReturn(FileUtil.createMemoryFileSystem().getRoot());
        when(projectMock.getLookup()).thenReturn(Lookup.EMPTY);
    }
    
    @Test
    public void testEnableNodeFactoryReplacesDefaultOne()
    {
        final TestSession session = new TestSession("TestSession", projectMock, SessionType.TEST);
        TestSupportUtils.enableNodeFactory(session);
        assertTrue(session.getNodeFactory() instanceof TestRunnerUINodeFactory);
    }
    
    @Test
    public void testEnableNodeFactoryKeepsCorrectOne()
    {
        final TestRunnerNodeFactory nodeFactory = new TestRunnerUINodeFactory();
        final TestSession session = new TestSession("TestSession", projectMock, SessionType.TEST, nodeFactory);
        TestSupportUtils.enableNodeFactory(session);
        assertSame(session.getNodeFactory(), nodeFactory);
    }
    
    @Test
    public void testGetUniqueDeclaratonNameTestCaseCppUTest()
    {
        final TestSession ts = mock(TestSession.class);
        final String testCaseName = "testCase";
        final String testSuiteName = "testSuite";
        CndTestCase testCase = new CndTestCase(testCaseName, TestFramework.CPPUTEST, ts);
        testCase.setClassName(testSuiteName);
        
        final String expected = "C:TEST_" + testSuiteName + "_" + testCaseName + "_Test";
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testCase));
    }
    
    
    @Test
    public void testGetUniqueDeclaratonNameTestSuiteeCppUTest()
    {
        final String testSuiteName = "testSuite";
        CndTestSuite testSuite = new CndTestSuite(testSuiteName, TestFramework.CPPUTEST);
        
        final String expected = "S:TEST_GROUP_CppUTestGroup" + testSuiteName;
        assertEquals(expected, TestSupportUtils.getUniqueDeclaratonName(testSuite));
    }
}
