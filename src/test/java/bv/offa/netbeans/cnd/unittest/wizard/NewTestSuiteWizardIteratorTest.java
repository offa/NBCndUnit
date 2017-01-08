/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2017  offa
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

package bv.offa.netbeans.cnd.unittest.wizard;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import static org.hamcrest.CoreMatchers.hasItems;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThat;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.openide.WizardDescriptor;

public class NewTestSuiteWizardIteratorTest
{
    private static WizardDescriptor wizardMock;
    private final NewTestSuiteWizardIterator wizardIterator = new NewTestSuiteWizardIterator();
    
    
    @BeforeClass
    public static void setUpClass()
    {
        wizardMock = createMock(true, false, true, 
                Arrays.asList("testCase1", "testCase2", "testCase3"), "TestSuite");
    }
    
    @Test
    @SuppressWarnings("unchecked")
    public void getTemplateParameters()
    {
        wizardIterator.wizard = wizardMock;
        final Map<String, Object> params = wizardIterator.getTemplateParameters();
        
        assertEquals(Boolean.TRUE, params.get("generateSetup"));
        assertEquals(Boolean.FALSE, params.get("generateTeardown"));
        assertEquals(Boolean.TRUE, params.get("generateTestCases"));
        assertThat((List<String>) params.get("testCases"), 
                    hasItems("testCase1", "testCase2", "testCase3"));
        assertEquals("TestSuite", params.get("suiteName"));
    }
    
    
    private static  WizardDescriptor createMock(boolean genSetup, boolean genTeardown, 
                                                boolean genTestCases, List<String> testCases, 
                                                String testSuite)
    {
        WizardDescriptor wiz = mock(WizardDescriptor.class);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_SETUP)).thenReturn(genSetup);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TEARDOWN)).thenReturn(genTeardown);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TESTCASES)).thenReturn(genTestCases);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTCASE_NAMES)).thenReturn(testCases);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTSUITE_NAME)).thenReturn(testSuite);
        
        return wiz;
    }

}
