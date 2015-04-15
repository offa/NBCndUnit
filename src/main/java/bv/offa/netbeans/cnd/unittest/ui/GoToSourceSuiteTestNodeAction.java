/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015  offa
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
import java.awt.event.ActionEvent;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Testcase;


public class GoToSourceSuiteTestNodeAction extends AbstractTestNodeAction
{
    private static final long serialVersionUID = 1L;

    public GoToSourceSuiteTestNodeAction(String actionName, Testcase testcase, Project project)
    {
        super(actionName, testcase, project);
    }
    
    

    @Override
    protected void doActionPerformed(ActionEvent ae)
    {
        // Jump to the first testcase of the testcaselist
        TestSupportUtils.goToSourceOfTestCase(project, testcase);
    }
    
}
