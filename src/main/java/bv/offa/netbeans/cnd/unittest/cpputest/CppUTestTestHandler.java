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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code CppUTestHandler} handles the test output.
 *
 * @author offa
 */
public class CppUTestTestHandler extends CndTestHandler
{
    private static final int GROUP_IGNORED = 1;
    private static final int GROUP_SUITE = 2;
    private static final int GROUP_CASE = 3;
    private static final int GROUP_TIME = 4;
    private static final int GROUP_TIME_VALUE = 5;
    private static boolean firstSuite;
    private final TestSessionInformation info;

    public CppUTestTestHandler(TestSessionInformation info)
    {
        super(TestFramework.CPPUTEST, "^(IGNORE_)??TEST\\(([^, ]+?), ([^, ]+?)\\)"
                                    + "( \\- ([0-9]+?) ms)?$");
        this.info = info;
        suiteFinished();
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
        final String suiteName = getMatchGroup(GROUP_SUITE);

        if( isSameTestSuite(currentSuite(session), suiteName) == false )
        {
            updateSessionState(manager, session);
            startNewTestSuite(suiteName, session, manager);
        }

        final String caseName = getMatchGroup(GROUP_CASE);
        CndTestCase testCase = startNewTestCase(caseName, suiteName, session);
        updateStatus(testCase);
        updateTime(testCase);
    }


    /**
     * Indicates the current suite has finished.
     */
    static void suiteFinished()
    {
        CppUTestTestHandler.firstSuite = true;
    }


    /**
     * Updates the session state.
     *
     * @param manager   Manager
     * @param session   Session
     */
    private void updateSessionState(ManagerAdapter manager, TestSession session)
    {
        if( firstSuite == true )
        {
            manager.testStarted(session);
            firstSuite = false;
        }
        else
        {
            manager.displayReport(session, session.getReport(0));
        }
    }


    /**
     * Updates the test time.
     *
     * @param testCase  Test Case
     */
    private void updateTime(CndTestCase testCase)
    {
        if( getMatchGroup(GROUP_TIME) != null )
        {
            final String timeValue = getMatchGroup(GROUP_TIME_VALUE);
            long testTime = Long.valueOf(timeValue);
            testCase.setTimeMillis(testTime);
            info.addTime(testTime);
        }
    }


    /**
     * Updates the test status.
     *
     * @param testCase  Test Case
     */
    private void updateStatus(CndTestCase testCase)
    {
        final String ignored = getMatchGroup(GROUP_IGNORED);

        if( ignored != null )
        {
            testCase.setSkipped();
        }
    }
}
