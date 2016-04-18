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

package bv.offa.netbeans.cnd.unittest.ui;

import java.awt.event.ActionEvent;

/**
 * The class {@code GoToCallstackNodeAction} implements an
 * {@link AbstractTestNodeAction action} for classtack nodes.
 * 
 * @author offa
 */
public class GoToCallstackNodeAction extends AbstractTestNodeAction
{
    private static final long serialVersionUID = 1L;
    
    public GoToCallstackNodeAction(String actionName, String frameInfo)
    {
        super(actionName, null);
    }

    
    /**
     * Performs the action.
     * 
     * @param ae    Action event
     */
    @Override
    protected void doActionPerformed(ActionEvent ae)
    {
        /* Not implemented. */
    }
    
}
