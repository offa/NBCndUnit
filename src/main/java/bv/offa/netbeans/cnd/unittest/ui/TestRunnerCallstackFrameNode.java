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

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.gsf.testrunner.api.CallstackFrameNode;
import org.openide.util.NbBundle;

public class TestRunnerCallstackFrameNode extends CallstackFrameNode
{
    private final String actionName = NbBundle.getMessage(TestRunnerTestMethodNode.class, "LBL_Action_GoToSource"); 
    
    public TestRunnerCallstackFrameNode(String frameInfo, String displayName)
    {
        super(frameInfo, displayName);
    }

    
    @Override
    public Action[] getActions(boolean context)
    {
        if( context == true )
        {
            return new Action[0];
        }
        
        List<Action> actions = new ArrayList<Action>(1);
        actions.add(getPreferredAction());
        
        return actions.toArray(new Action[actions.size()]);
    }

    @Override
    public Action getPreferredAction()
    {
        Action action = new GoToCallstackNodeAction(actionName, frameInfo);
        action.setEnabled(false);
        
        return action;
    }
    
}
