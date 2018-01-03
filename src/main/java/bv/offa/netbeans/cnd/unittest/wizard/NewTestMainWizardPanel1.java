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

package bv.offa.netbeans.cnd.unittest.wizard;

import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 * The class {@code NewTestMainWizardPanel1} implements a wizard panel for
 * test mains.
 * 
 * @author offa
 */
public class NewTestMainWizardPanel1 extends AbstractWizardPanel
{
    protected static final String PROP_MAIN_ENABLE_VERBOSE = "PROP_MAIN_ENABLE_VERBOSE";
    protected static final String PROP_MAIN_ENABLE_COLOR = "PROP_MAIN_ENABLE_COLOR";
    private NewTestMainVisualPanel1 component;

    
    /**
     * Gets the visual component.
     * 
     * @return  Component
     */
    @Override
    public NewTestMainVisualPanel1 getComponent()
    {
        if( component == null )
        {
            component = new NewTestMainVisualPanel1();
            component.addChangeListener(this);
        }
        
        return component;
    }

    
    
    /**
     * Help for this panel. When the panel is active, this is used as the help
     * for the wizard dialog.
     * 
     * @return  The help or {@code null} if no help is supplied
     */
    @Override
    public HelpCtx getHelp()
    {
        return HelpCtx.DEFAULT_HELP;
    }


    /**
     * Tests whether the panel meets all conditions to be valid. If so, the
     * panel is finished and it is safe to proceed to the next one.
     * 
     * <p><b>Note:</b> The "Next" (or "Finish") button will only be enabled if
     * this method returns {@code true}.</p>
     * 
     * @return  Returns {@code true} if valid, or {@code false} otherwise
     */
    @Override
    public boolean isValid()
    {
        return true;
    }


    /**
     * Reads the settings from the wizard descriptor.
     * 
     * @param wiz   Wizard descriptor
     */
    @Override
    public void readSettings(WizardDescriptor wiz)
    {
        /* Empty */
    }


    /**
     * Stores the settings to the wizard descriptor.
     * 
     * @param wiz   Wizard descriptor
     */
    @Override
    public void storeSettings(WizardDescriptor wiz)
    {
        if( isNextOption(wiz.getValue()) == true )
        {
            wiz.putProperty(PROP_MAIN_ENABLE_VERBOSE, getEnableVerboseOutputFromVisualPanel());
            wiz.putProperty(PROP_MAIN_ENABLE_COLOR, getEnableColorOutputFromVisualPanel());
            wiz.putProperty(PROP_CONFIGURE_CUSTOM_PROJECT, getConfigureCustomProjectFromVisualPanel());
        }
    }
    
    
    /**
     * Sets the visual component.
     * 
     * @param comp  Component
     */
    void setComponent(NewTestMainVisualPanel1 comp)
    {
        this.component = comp;
    }
    
    
    /**
     * Returns the verbose mode setting from the visual component.
     * 
     * @return  Setting
     */
    private boolean getEnableVerboseOutputFromVisualPanel()
    {
        return getComponent().getEnableVerbose();
    }
    
    
    /**
     * Returns the color mode setting from the visual component.
     * 
     * @return  Setting
     */
    private boolean getEnableColorOutputFromVisualPanel()
    {
        return getComponent().getEnableColor();
    }
    
    
    /**
     * Returns whether to configure a custom project from the visual component.
     * 
     * @return  Configure Setting
     */
    private boolean getConfigureCustomProjectFromVisualPanel()
    {
        return getComponent().getConfigureCustomProject();
    }

}
