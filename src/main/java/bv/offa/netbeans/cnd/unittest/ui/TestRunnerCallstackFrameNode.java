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

package bv.offa.netbeans.cnd.unittest.ui;

import java.util.ArrayList;
import java.util.List;
import javax.swing.Action;
import org.netbeans.modules.gsf.testrunner.ui.api.CallstackFrameNode;
import org.netbeans.modules.gsf.testrunner.ui.api.TestMethodNode;
import org.openide.nodes.Node;
import org.openide.util.NbBundle;

/**
 * The class {@code TestRunnerCallstackFrameNode} implements a frame node for
 * a callstack.
 * 
 * @author offa
 */
public class TestRunnerCallstackFrameNode extends CallstackFrameNode
{
    private final String actionName = NbBundle.getMessage(TestRunnerCallstackFrameNode.class, "LBL_Action_GoToSource"); 
    
    public TestRunnerCallstackFrameNode(String frameInfo, String displayName)
    {
        super(frameInfo, displayName);
    }

    
    
    /**
     * Returns the preferred action.
     * 
     * @return      Action
     */
    @Override
    public Action getPreferredAction()
    {
        Action action = findActionOfParentNode(this);

        if( action instanceof GoToSourceTestMethodNodeAction )
        {
            final GoToSourceTestMethodNodeAction parent = (GoToSourceTestMethodNodeAction) action;
            return new GoToCallstackNodeAction(actionName, parent.getTestCase(), parent.getProject());
        }
        else
        {
            throw new IllegalStateException("Invalid action instance: " + action.getClass().getName());
        }
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
        
        List<Action> actions = new ArrayList<>(1);
        actions.add(getPreferredAction());
        
        return actions.toArray(new Action[actions.size()]);
    }
    
    
    /**
     * Returns the preferred action of the parent {@code TestMethodNode}. If
     * there's no such parent, {@code null} is returned instead.
     * 
     * @param node  Node
     * @return      Action of the parent {@code TestMethodNode} or {@code null}
     *              if there's no parent of this type
     */
    static Action findActionOfParentNode(Node node)
    {
        Node n = node;
        
        while( n != null )
        {
            if( n instanceof TestMethodNode )
            {
                return n.getPreferredAction();
            }
            
            n = n.getParentNode();
        }
        
        return null;
    }

}
