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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code CppUTestErrorHandler} handles test errors and their
 * information.
 *
 * @author offa
 */
public class CppUTestErrorHandler extends CndTestHandler
{
    private static final int GROUP_FILE = 1;
    private static final int GROUP_LINE = 2;
    private static final int GROUP_SUITE = 3;
    private static final int GROUP_CASE = 4;

    public CppUTestErrorHandler()
    {
        super(TestFramework.CPPUTEST, "^(.+?)\\:([0-9]+?)\\: error\\: Failure in "
                                    + "TEST\\(([^, ]+?), ([^, ]+?)\\)$");
    }



    /**
     * Updates the UI.
     *
     * @param manager       Manager Adapter
     * @param session       Test session
     */
    @Override
    public void updateUI(ManagerAdapter manager, TestSession session)
    {
        CndTestCase testCase = currentTestCase(session);
        final String suiteName = getMatchGroup(GROUP_SUITE);
        final String caseName = getMatchGroup(GROUP_CASE);

        if( isSameTestCase(testCase, caseName, suiteName) == true )
        {
            final String file = getMatchGroup(GROUP_FILE);
            final String lineNumber = getMatchGroup(GROUP_LINE);
            testCase.setError(file, Integer.parseInt(lineNumber));
        }
    }

}
