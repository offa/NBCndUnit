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

package bv.offa.netbeans.cnd.unittest.googletest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code GoogleTestTestFinishedHandler} handles the finish of a
 * test case.
 *
 * @author offa
 */
public class GoogleTestErrorHandler extends CndTestHandler
{
    private static final int GROUP_FILE = 1;
    private static final int GROUP_LINE = 2;

    public GoogleTestErrorHandler()
    {
        super(TestFramework.GOOGLETEST, "^(.+?):([0-9]+?): Failure$");
    }

    @Override
    public void updateUI(ManagerAdapter manager, TestSession session)
    {
        final CndTestCase testCase = currentTestCase(session);
        testCase.setError(getMatchGroup(GROUP_FILE), Integer.parseInt(getMatchGroup(GROUP_LINE)));
    }

}
