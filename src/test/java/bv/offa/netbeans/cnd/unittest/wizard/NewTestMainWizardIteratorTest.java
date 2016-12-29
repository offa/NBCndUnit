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

import java.util.Map;
import static org.junit.Assert.assertEquals;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import static org.mockito.Mockito.*;
import org.openide.WizardDescriptor;

public class NewTestMainWizardIteratorTest
{
    private static WizardDescriptor wizardMock;
    private final NewTestMainWizardIterator wizardIterator = new NewTestMainWizardIterator();
    
    
    @BeforeClass
    public static void setUpClass()
    {
        wizardMock = mock(WizardDescriptor.class);
        when(wizardMock.getProperty(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_VERBOSE)).thenReturn(true);
        when(wizardMock.getProperty(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_COLOR)).thenReturn(false);
    }
    
    @Before
    public void setUp()
    {
        wizardIterator.wizard = wizardMock;
    }
    
    @Test
    public void getTemplateParameters()
    {
        final Map<String, Object> params = wizardIterator.getTemplateParameters();
        assertEquals(Boolean.TRUE, params.get("enableVerbose"));
        assertEquals(Boolean.FALSE, params.get("enableColor"));
    }
}
