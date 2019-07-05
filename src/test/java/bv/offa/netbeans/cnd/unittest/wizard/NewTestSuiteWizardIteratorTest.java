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

package bv.offa.netbeans.cnd.unittest.wizard;

import static com.google.common.truth.Truth.assertThat;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.openide.WizardDescriptor;

@Tag("ui")
public class NewTestSuiteWizardIteratorTest
{
    private static WizardDescriptor wizardMock;
    private final NewTestSuiteWizardIterator wizardIterator = new NewTestSuiteWizardIterator();


    @BeforeAll
    public static void setUpClass()
    {
        wizardMock = createMock(true, false, true, true,
                Arrays.asList("testCase1", "testCase2", "testCase3"), "TestSuite");
    }

    @Test
    @SuppressWarnings("unchecked")
    public void getTemplateParameters()
    {
        wizardIterator.wizard = wizardMock;
        final Map<String, Object> params = wizardIterator.getTemplateParameters();

        assertThat(params.get("generateSetup")).isEqualTo(Boolean.TRUE);
        assertThat(params.get("generateTeardown")).isEqualTo(Boolean.FALSE);
        assertThat(params.get("generateTestCases")).isEqualTo(Boolean.TRUE);
        assertThat(params.get("enableModernCpp")).isEqualTo(Boolean.TRUE);
        assertThat((List<String>) params.get("testCases")).containsExactly("testCase1", "testCase2", "testCase3");
        assertThat(params.get("suiteName")).isEqualTo("TestSuite");
    }


    private static  WizardDescriptor createMock(boolean genSetup, boolean genTeardown,
                                                boolean genTestCases, boolean enableModernCpp,
                                                List<String> testCases, String testSuite)
    {
        WizardDescriptor wiz = mock(WizardDescriptor.class);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_SETUP)).thenReturn(genSetup);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TEARDOWN)).thenReturn(genTeardown);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TESTCASES)).thenReturn(genTestCases);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTCASE_NAMES)).thenReturn(testCases);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTSUITE_NAME)).thenReturn(testSuite);
        when(wiz.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_ENABLE_MODERNCPP)).thenReturn(enableModernCpp);

        return wiz;
    }

}
