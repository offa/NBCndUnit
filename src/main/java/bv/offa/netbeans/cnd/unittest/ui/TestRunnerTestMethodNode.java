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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.ui.api.DiffViewAction;
import org.netbeans.modules.gsf.testrunner.ui.api.Locator;
import org.netbeans.modules.gsf.testrunner.ui.api.TestMethodNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;
import org.openide.util.lookup.Lookups;

/**
 * The class {@code TestRunnerTestMethodNode} implements a frame node for
 * a testmethod.
 * 
 * @author offa
 */
public class TestRunnerTestMethodNode extends TestMethodNode
{
    private final String actionName = NbBundle.getMessage(TestRunnerTestMethodNode.class, "LBL_Action_GoToSource");
    
    public TestRunnerTestMethodNode(CndTestCase testcase, Project project)
    {
        super(testcase, project, Lookups.singleton(new Locator()
        {
            @Override
            public void jumpToSource(Node node)
            {
                node.getPreferredAction().actionPerformed(null);
            }
        }));
    }


    
    /**
     * Returns the preferred action.
     * 
     * @return      Action
     */
    @Override
    public Action getPreferredAction()
    {
        return new GoToSourceTestMethodNodeAction(actionName, (CndTestCase) testcase, getProject());
    }
    
    
    /**
     * Returns actions that are associated with this node. These are used to
     * construct the context menu for the node.
     * 
     * @param context   Whether to find actions for context meaning or for the
     *                  node itself
     * @return          Returns an empty array if {@code context} is
     *                  {@code true}, otherwise the actions for this node
     *                  are returned
     */
    @Override
    public Action[] getActions(boolean context)
    {
        if( context == true )
        {
            return new Action[0];
        }
        
        List<Action> actions = new ArrayList<>(2);
        actions.add(getPreferredAction());
        actions.add(new DiffViewAction(testcase));
        
        return actions.toArray(new Action[actions.size()]);
    }

}
