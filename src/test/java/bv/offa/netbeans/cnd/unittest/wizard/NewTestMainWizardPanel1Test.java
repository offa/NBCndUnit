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
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

@Tag("ui")
public class NewTestMainWizardPanel1Test
{
    private static WizardDescriptor wizardMock;
    private final NewTestMainWizardPanel1 panel = new NewTestMainWizardPanel1();

    @BeforeAll
    public static void setUpClass()
    {
        wizardMock = mock(WizardDescriptor.class);
    }

    @BeforeEach
    public void setUp()
    {
        reset(wizardMock);
    }

    @Test
    public void validPerDefault()
    {
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
        NewTestMainWizardPanel1 spyPanel = spy(panel);
        spyPanel.setComponent(null);
        when(spyPanel.isValid()).thenReturn(true);
        when(wizardMock.getValue()).thenReturn(WizardDescriptor.FINISH_OPTION);
        spyPanel.storeSettings(wizardMock);
        verify(wizardMock).putProperty(eq(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_VERBOSE), any());
        verify(wizardMock).putProperty(eq(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_COLOR), any());
        verify(wizardMock).putProperty(eq(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_MODERNCPP), any());
    }

    @Test
    public void storeSettingsWithNoCancelOptionAndInvalid()
    {
        NewTestMainWizardPanel1 spyPanel = spy(panel);
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
        assertThat(panel.getHelp()).isEqualTo(HelpCtx.DEFAULT_HELP);
    }

}
