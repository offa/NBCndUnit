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
public class GoogleTestTestFinishedHandler extends CndTestHandler
{
    private static final int GROUP_RESULT = 1;
    private static final int GROUP_SUITE = 2;
    private static final int GROUP_CASE = 3;
    private static final int GROUP_TIME = 4;
    private static final String MSG_FAILED = "FAILED ";


    public GoogleTestTestFinishedHandler()
    {
        super(TestFramework.GOOGLETEST, "^.*?\\[  (     OK|FAILED ) \\].*? (.+?)\\.(.+?)(?:/.+)??"
                                        + " \\(([0-9]+?) ms\\)$");
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
        final CndTestCase testCase = currentTestCase(session);
        final String caseName = getMatchGroup(GROUP_CASE);
        final String suiteName = getMatchGroup(GROUP_SUITE);

        if( isSameTestCase(testCase, caseName, suiteName) == true )
        {
            final String location = suiteName + ":" + caseName;
            testCase.setLocation(location);
            updateTime(testCase);
            updateResult(testCase);
        }
        else
        {
            throw new IllegalStateException("No test found for: "
                                            + suiteName + ":" + caseName);
        }
    }


    /**
     * Updates the test time.
     *
     * @param testCase  Test Case
     */
    private void updateTime(CndTestCase testCase)
    {
        final String timeValue = getMatchGroup(GROUP_TIME);
        long time = Long.parseLong(timeValue);
        testCase.setTimeMillis(time);
    }


    /**
     * Updates the test result.
     *
     * @param testCase      Test Case
     */
    private void updateResult(CndTestCase testCase)
    {
        final String result = getMatchGroup(GROUP_RESULT);

        if( result.equals(MSG_FAILED) == true )
        {
            testCase.setError();
        }
    }

}
