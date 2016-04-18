/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2016  offa
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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.BeforeClass;
import static org.mockito.Mockito.*;

public class NewTestSuiteVisualPanel1Test
{
    private static ChangeListener listenerMock;
    private final NewTestSuiteVisualPanel1 panel = new NewTestSuiteVisualPanel1();
    
    
    @BeforeClass
    public static void setUpClass()
    {
        listenerMock = mock(ChangeListener.class);
    }
    
    @Before
    public void setUp()
    {
        reset(listenerMock);
        panel.removeChangeListener(listenerMock);
    }
    
    
    @Test
    public void getTestCaseNamesIsEmptyIfNothingSet()
    {
        panel.setTestCaseNames(Arrays.asList(""));
        assertTrue(panel.getTestCaseNames().isEmpty());
    }
    
    @Test
    public void getTestCaseNamesContainsValue()
    {
        panel.setTestCaseNames(Arrays.asList("abc"));
        final List<String> names = panel.getTestCaseNames();
        assertEquals(1, names.size());
        assertEquals("abc", names.get(0));
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
