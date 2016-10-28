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

package bv.offa.netbeans.cnd.unittest.cpputest.teamcity;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import bv.offa.netbeans.cnd.unittest.cpputest.TestSessionInformation;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code CppUTestTCTestFinishedHandler} handles the finish of a
 * test case.
 *
 * @author offa
 */
public class CppUTestTCTestFinishedHandler extends CndTestHandler
{
    private static final int GROUP_CASE = 1;
    private static final int GROUP_TIME = 2;
    private final TestSessionInformation info;

    public CppUTestTCTestFinishedHandler(TestSessionInformation info)
    {
        super(TestFramework.CPPUTEST_TC, "^##teamcity\\[testFinished name='(.+?)' "
                                            + "duration='([0-9]+?)'\\]$");
        this.info = info;
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
        // TODO: Check matching suite / case in all these handlers
        final CndTestCase testCase = currentTestCase(session);
        final String caseName = getMatchGroup(GROUP_CASE);
        final String suiteName = currentSuite(session).getName();

        final String location = suiteName + ":" + caseName;
        testCase.setLocation(location);
        updateTime(testCase);
    }


    /**
     * Updates the test time.
     *
     * @param testCase  Test Case
     */
    private void updateTime(CndTestCase testCase)
    {
        final String timeValue = getMatchGroup(GROUP_TIME);
        final long time = Long.valueOf(timeValue);
        testCase.setTimeMillis(time);
        info.addTime(time);
    }
}
