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

import javax.swing.SwingUtilities;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import org.openide.WizardDescriptor;
import org.openide.util.ChangeSupport;
import org.openide.util.NbBundle;

/**
 * The class {@code AbstractWizardPanel} is an abstract base class for
 * wizard panels.
 *
 * @author offa
 */
public abstract class AbstractWizardPanel implements WizardDescriptor.Panel<WizardDescriptor>, ChangeListener
{
    protected static final String PROP_CONFIGURE_CUSTOM_PROJECT = "PROP_CONFIGURE_CUSTOM_PROJECT";
    private final ChangeSupport changeSupport;


    public AbstractWizardPanel()
    {
        this.changeSupport = new ChangeSupport(this);
    }



    /**
     * Adds the {@link ChangeListener ChangeListener}.
     *
     * @param l     Listener
     */
    @Override
    public void addChangeListener(ChangeListener l)
    {
        this.changeSupport.addChangeListener(l);
    }


    /**
     * Removes the {@link ChangeListener ChangeListener}.
     *
     * @param l     Listener
     */
    @Override
    public void removeChangeListener(ChangeListener l)
    {
        this.changeSupport.removeChangeListener(l);
    }


    /**
     * Invoked when the target of the listener has changed its state.
     *
     * @param e     Event
     */
    @Override
    public void stateChanged(ChangeEvent e)
    {
        if( SwingUtilities.isEventDispatchThread() == true )
        {
            changeSupport.fireChange();
        }
        else
        {
            SwingUtilities.invokeLater(changeSupport::fireChange);
        }
    }


    /**
     * Returns the localized message with the given key using this class.
     *
     * @param key   Key
     * @return      Message
     */
    protected String getMessageFromBundle(String key)
    {
        return NbBundle.getMessage(AbstractWizardPanel.class , key);
    }


    /**
     * Returns whether there are {@code ChangeSupport}-listener.
     *
     * @return  Returns {@code true} if there is at least one listener or
     *          {@code none} if there is none
     */
    protected boolean hasChangeSupportListeners()
    {
        return changeSupport.hasListeners();
    }


    /**
     * Tests if <code>wiz</code> is a next option with valid data.
     *
     * @param wizValue  Wizard value
     * @return          Returns <tt>true</tt> if valid next or <tt>false</tt>
     *                  otherwise
     */
    protected boolean isNextOption(Object wizValue)
    {
        if( WizardDescriptor.PREVIOUS_OPTION.equals(wizValue) == true )
        {
            return false;
        }

        return ( WizardDescriptor.CANCEL_OPTION.equals(wizValue) == false )
                && ( isValid() == true );
    }
}
