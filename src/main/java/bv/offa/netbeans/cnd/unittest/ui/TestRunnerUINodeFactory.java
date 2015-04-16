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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import org.netbeans.api.project.Project;
import org.netbeans.modules.gsf.testrunner.api.TestRunnerNodeFactory;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.TestsuiteNode;
import org.openide.nodes.Node;

public class TestRunnerUINodeFactory extends TestRunnerNodeFactory
{
    @Override
    public Node createTestMethodNode(Testcase testcase, Project project)
    {
        return new TestRunnerTestMethodNode((CndTestCase) testcase, project);
    }

    @Override
    public Node createCallstackFrameNode(String frameInfo, String displayName)
    {
        return new TestRunnerCallstackFrameNode(frameInfo, displayName);
    }

    @Override
    public TestsuiteNode createTestSuiteNode(String suiteName, boolean filtered)
    {
        return new TestRunnerTestSuiteNode(suiteName, filtered);
    }
    
}
