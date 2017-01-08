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

import javax.swing.JPanel;
import javax.swing.event.ChangeListener;
import org.openide.util.ChangeSupport;

/**
 * The class {@code AbstractVisualPanel} is an abstract base class for wizard
 * visual panels.
 * 
 * @author offa
 */
public abstract class AbstractVisualPanel extends JPanel
{
    private static final long serialVersionUID = 1L;
    private final ChangeSupport changeSupport;

    public AbstractVisualPanel(String compName)
    {
        this.changeSupport = new ChangeSupport(this);
        setup(compName);
    }
    
    
    
    /**
     * Adds the {@link ChangeListener ChangeListener}.
     * 
     * @param l     Listener
     */
    public void addChangeListener(ChangeListener l)
    {
        changeSupport.addChangeListener(l);
    }
    
    
    /**
     * Removes the {@link ChangeListener ChangeListener}.
     * 
     * @param l     Listener
     */
    public void removeChangeListener(ChangeListener l)
    {
        changeSupport.removeChangeListener(l);
    }
    
    
    /**
     * Updates the listeners about a change.
     */
    protected void updateChangeListener()
    {
        changeSupport.fireChange();
    }
    
    
    /**
     * Setups the component.
     * 
     * @param compName  Name of the component
     */
    private void setup(String compName)
    {
        setName(compName);
    }
}
