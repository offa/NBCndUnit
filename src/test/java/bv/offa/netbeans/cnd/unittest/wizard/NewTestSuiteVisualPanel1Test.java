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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;

@Tag("ui")
public class NewTestSuiteVisualPanel1Test
{
    private static ChangeListener listenerMock;
    private final NewTestSuiteVisualPanel1 panel = new NewTestSuiteVisualPanel1();


    @BeforeAll
    public static void setUpClass()
    {
        listenerMock = mock(ChangeListener.class);
    }

    @BeforeEach
    public void setUp()
    {
        reset(listenerMock);
        panel.removeChangeListener(listenerMock);
    }

    @Test
    public void defaultSettings()
    {
        assertThat(panel.getTestSuiteName().isEmpty()).isFalse();
        assertThat(panel.getGenerateTestCases()).isTrue();
        assertThat(panel.getTestCaseNames().isEmpty()).isFalse();
        assertThat(panel.getGenerateSetup()).isTrue();
        assertThat(panel.getGenerateTeardown()).isTrue();
        assertThat(panel.getEnableModernCpp()).isTrue();
        assertThat(panel.getConfigureCustomProject()).isFalse();
    }

    @Test
    public void getTestCaseNamesIsEmptyIfNothingSet()
    {
        panel.setTestCaseNames(Arrays.asList(""));
        assertThat(panel.getTestCaseNames().isEmpty()).isTrue();
    }

    @Test
    public void getTestCaseNamesContainsValue()
    {
        panel.setTestCaseNames(Arrays.asList("abc"));
        final List<String> names = panel.getTestCaseNames();
        assertThat(names.size()).isEqualTo(1);
        assertThat(names.get(0)).isEqualTo("abc");
    }

    @Test
    public void changeListenerUpdatedOnSuiteNameChange()
    {
        panel.addChangeListener(listenerMock);
        panel.setTestSuiteName("abc");
        verify(listenerMock, atLeastOnce()).stateChanged(any(ChangeEvent.class));
    }

    @Test
    public void changeListenerUpdatedOnTestCaseNamesChange()
    {
        panel.addChangeListener(listenerMock);
        panel.setTestCaseNames(Arrays.asList("abc"));
        verify(listenerMock, atLeastOnce()).stateChanged(any(ChangeEvent.class));
    }
}
