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

import javax.swing.event.ChangeListener;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import org.junit.Before;
import org.junit.Test;
import static org.mockito.Mockito.*;

public class AbstractWizardPanelTest
{
    private AbstractWizardPanel panelMock;
    private ChangeListener listenerMock;
    
    
    @Before
    public void setUp()
    {
        panelMock = mock(AbstractWizardPanel.class, withSettings()
                                                    .useConstructor()
                                                    .defaultAnswer(CALLS_REAL_METHODS));
        listenerMock = mock(ChangeListener.class);
    }
    
    @Test
    public void testListenerAdded()
    {
        panelMock.addChangeListener(listenerMock);
        assertTrue(panelMock.hasChangeSupportListeners());
    }
    
    @Test
    public void testListenerRemoved()
    {
        panelMock.addChangeListener(listenerMock);
        panelMock.removeChangeListener(listenerMock);
        assertFalse(panelMock.hasChangeSupportListeners());
    }
    
}
