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
import java.util.Collections;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

@Tag("ui")
public class NewTestSuiteWizardPanel1Test
{
    private static NewTestSuiteVisualPanel1 compMock;
    private static WizardDescriptor wizardMock;
    private final NewTestSuiteWizardPanel1 panel = new NewTestSuiteWizardPanel1();


    @BeforeAll
    public static void setUpClass()
    {
        compMock = mock(NewTestSuiteVisualPanel1.class);
        wizardMock = mock(WizardDescriptor.class);
    }

    @BeforeEach
    public void setUp()
    {
        reset(wizardMock);
        reset(compMock);
        when(compMock.getTestSuiteName()).thenReturn("SuiteName");
        panel.setComponent(compMock);
    }


    @Test
    public void validPerDefault()
    {
        when(compMock.getTestSuiteName()).thenReturn("abc");
        assertTrue(panel.isValid());
    }

    @Test
    public void invalidIfNoSuiteName()
    {
        when(compMock.getTestSuiteName()).thenReturn("");
        assertFalse(panel.isValid());
    }

    @Test
    public void validIfSuiteName()
    {
        when(compMock.getTestSuiteName()).thenReturn("abc");
        assertTrue(panel.isValid());
    }

    @Test
    public void invalidIfTestCasesSelectedButNoValue()
    {
        final List<String> l = Collections.emptyList();
        when(compMock.getTestCaseNames()).thenReturn(l);
        when(compMock.getGenerateTestCases()).thenReturn(true);
        assertFalse(panel.isValid());
    }

    @Test
    public void validIfTestCasesSelectedAndValue()
    {
        when(compMock.getTestCaseNames()).thenReturn(Arrays.asList("Case"));
        when(compMock.getTestSuiteName()).thenReturn("Suite");
        assertTrue(panel.isValid());
    }

    @Test
    public void validIfTestCasesSelectedAndNoValue()
    {
        when(compMock.getTestCaseNames()).thenReturn(Arrays.asList(""));
        when(compMock.getGenerateTestCases()).thenReturn(false);
        when(compMock.getTestSuiteName()).thenReturn("Suite");
        assertTrue(panel.isValid());
    }

    @Test
    public void invalidIfTestTaseSelectedAndInvalidTestCaseName()
    {
        when(compMock.getTestCaseNames()).thenReturn(Arrays.asList(" $invalid"));
        when(compMock.getGenerateTestCases()).thenReturn(true);
        when(compMock.getTestSuiteName()).thenReturn("Suite");
        assertFalse(panel.isValid());
    }

    @Test
    public void validIfTestCasesNotSelectedAndInvalidName()
    {
        when(compMock.getTestCaseNames()).thenReturn(Arrays.asList(" $invalid"));
        when(compMock.getGenerateTestCases()).thenReturn(false);
        assertTrue(panel.isValid());
    }

    @Test
    public void invalidIfTestSuiteNameIsNotValid()
    {
        when(compMock.getGenerateTestCases()).thenReturn(false);
        when(compMock.getTestSuiteName()).thenReturn(" $Suite");
        assertFalse(panel.isValid());
    }

    @Test
    public void validIfTestSuiteNameIsValid()
    {
        when(compMock.getGenerateTestCases()).thenReturn(false);
        when(compMock.getTestSuiteName()).thenReturn("Suite");
        assertTrue(panel.isValid());
    }

    @Test
    public void storeSettingsWithPreviousOption()
    {
        when(wizardMock.getValue()).thenReturn(WizardDescriptor.PREVIOUS_OPTION);
        panel.storeSettings(wizardMock);
        verify(wizardMock, never()).putProperty(anyString(), any());
    }

    @Test
    public void storeSettingsWithNoCancelOptionAndValid()
    {
        NewTestSuiteWizardPanel1 spyPanel = spy(panel);
        spyPanel.setComponent(null);
        when(spyPanel.isValid()).thenReturn(true);
        when(wizardMock.getValue()).thenReturn(WizardDescriptor.FINISH_OPTION);
        spyPanel.storeSettings(wizardMock);
        verify(wizardMock).putProperty(eq(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_SETUP), any());
        verify(wizardMock).putProperty(eq(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TEARDOWN), any());
        verify(wizardMock).putProperty(eq(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TESTCASES), any());
        verify(wizardMock).putProperty(eq(NewTestSuiteWizardPanel1.PROP_TEST_TESTCASE_NAMES), any());
        verify(wizardMock).putProperty(eq(NewTestSuiteWizardPanel1.PROP_TEST_TESTSUITE_NAME), any());
    }

    @Test
    public void storeSettingsWithNoCancelOptionAndInvalid()
    {
        NewTestSuiteWizardPanel1 spyPanel = spy(panel);
        spyPanel.setComponent(null);
        when(spyPanel.isValid()).thenReturn(false);
        when(wizardMock.getValue()).thenReturn(WizardDescriptor.FINISH_OPTION);
        spyPanel.storeSettings(wizardMock);
        verify(wizardMock, never()).putProperty(anyString(), any());
    }

    @Test
    public void storeSettingsWithCancelOption()
    {
        when(wizardMock.getValue()).thenReturn(WizardDescriptor.CANCEL_OPTION);
        panel.storeSettings(wizardMock);
        verify(wizardMock, atLeastOnce()).getValue();
        verifyNoMoreInteractions(wizardMock);
    }

    @Test
    public void getHelpReturnsDefaultHelp()
    {
        assertEquals(HelpCtx.DEFAULT_HELP, panel.getHelp());
    }
}
