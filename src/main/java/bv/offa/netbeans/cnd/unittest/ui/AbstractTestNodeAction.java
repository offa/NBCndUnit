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

package bv.offa.netbeans.cnd.unittest.ui;

import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.ui.api.TestNodeAction;

/**
 * The class {@code AbstractTestNodeAction} is the base for test node actions.
 * 
 * @author offa
 */
public abstract class AbstractTestNodeAction extends TestNodeAction
{
    private static final long serialVersionUID = 1L;
    protected final String actionName;
    protected final Project project;

    public AbstractTestNodeAction(String actionName, Project project)
    {
        this.actionName = actionName;
        this.project = project;
        super.putValue(Action.NAME, actionName);
    }
    
}
