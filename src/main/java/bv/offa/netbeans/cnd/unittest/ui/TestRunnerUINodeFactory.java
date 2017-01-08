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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.ui.api.TestRunnerNodeFactory;
import org.netbeans.modules.gsf.testrunner.ui.api.TestsuiteNode;
import org.openide.nodes.Node;

/**
 * The class {@code TestRunnerUINodeFactory} implements a testrunner
 * node factory.
 * 
 * @author offa
 */
public class TestRunnerUINodeFactory extends TestRunnerNodeFactory
{
    /**
     * Creates a new testmethod node.
     * 
     * @param testcase  Testcase
     * @param project   Project
     * @return          Node
     */
    @Override
    public Node createTestMethodNode(Testcase testcase, Project project)
    {
        return new TestRunnerTestMethodNode((CndTestCase) testcase, project);
    }

    
    /**
     * Creates a new callstack node.
     * 
     * @param frameInfo     Callstack frameinfo
     * @param displayName   Display name
     * @return              Node
     */
    @Override
    public Node createCallstackFrameNode(String frameInfo, String displayName)
    {
        return new TestRunnerCallstackFrameNode(frameInfo, displayName);
    }

    
    /**
     * Creates a new testsuite node.
     * 
     * @param suiteName Testsuite name
     * @param filtered  Filtered
     * @return          Node
     */
    @Override
    public TestsuiteNode createTestSuiteNode(String suiteName, boolean filtered)
    {
        return new TestRunnerTestSuiteNode(suiteName, filtered);
    }
    
}
