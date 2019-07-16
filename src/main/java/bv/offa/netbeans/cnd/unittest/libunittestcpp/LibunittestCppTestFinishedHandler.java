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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code LibunittestCppTestFinishedHandler} handles the test
 * output.
 *
 * @author offa
 */
public class LibunittestCppTestFinishedHandler extends CndTestHandler
{
    private static final int GROUP_SUITE = 1;
    private static final int GROUP_CASE = 2;
    private static final int GROUP_TIME = 3;
    private static final int GROUP_RESULT = 4;
    private static final String MSG_FAILED = "FAIL";
    private static final String MSG_SKIP = "SKIP";
    private static boolean firstSuite;


    public LibunittestCppTestFinishedHandler()
    {
        super(TestFramework.LIBUNITTESTCPP, "^(.+?)::(.+?) \\.{3} \\[([0-9].*?)s\\] (ok|FAIL|SKIP).*?$");
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
        final String suiteName = normalise(getMatchGroup(GROUP_SUITE));

        if( isSameTestSuite(currentSuite(session), suiteName) == false )
        {
            updateSessionState(manager, session);
            startNewTestSuite(suiteName, session, manager);
        }

        final String testName = normalise(getMatchGroup(GROUP_CASE));
        CndTestCase testCase = startNewTestCase(testName, suiteName, session);
        updateTime(testCase);
        updateResult(testCase);
    }


    /**
     * Indicates the current suite has finished.
     */
    static void suiteFinished()
    {
        LibunittestCppTestFinishedHandler.firstSuite = true;
    }


    /**
     * Normalises the input. This will replace all prohibited characters.
     *
     * @param input     Input string
     * @return          Normalised output
     */
    private String normalise(String input)
    {
        return input.replace('<', '(').replace('>', ')');
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
     * Updates the test result.
     *
     * @param testCase      Test Case
     * @param location      Test location
     */
    private void updateResult(CndTestCase testCase)
    {
        final String result = getMatchGroup(GROUP_RESULT);

        switch(result)
        {
            case MSG_FAILED:
                testCase.setError();
                break;
            case MSG_SKIP:
                testCase.setSkipped();
                break;
            default:
                break;

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
        testCase.setTimeMillis(TestSupportUtils.parseTimeSecToMillis(timeValue));
    }

}

