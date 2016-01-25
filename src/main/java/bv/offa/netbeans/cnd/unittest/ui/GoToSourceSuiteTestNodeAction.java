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

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import java.awt.event.ActionEvent;
import org.netbeans.api.project.Project;

/**
 * The class {@code GoToSourceSuiteTestNodeAction} implements an
 * {@link AbstractTestNodeAction action} for testsuite nodes.
 * 
 * @author offa
 */
public class GoToSourceSuiteTestNodeAction extends AbstractTestNodeAction
{
    private static final long serialVersionUID = 1L;
    private final CndTestSuite testSuite;

    public GoToSourceSuiteTestNodeAction(String actionName, CndTestSuite testSuite, Project project)
    {
        super(actionName, project);
        this.testSuite = testSuite;
    }
    
    
    /**
     * Performs the action.
     * 
     * @param ae    Action event
     */
    @Override
    protected void doActionPerformed(ActionEvent ae)
    {
        TestSupportUtils.goToSourceOfTestSuite(project, testSuite);
    }
    
}
