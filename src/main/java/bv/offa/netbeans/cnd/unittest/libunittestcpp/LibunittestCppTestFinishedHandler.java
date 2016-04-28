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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;

/**
 * The class {@code LibunittestCppTestFinishedHandler} handles the test
 * output.
 * 
 * @author offa
 */
class LibunittestCppTestFinishedHandler extends CndTestHandler
{
    private static final TestFramework TESTFRAMEWORK = TestFramework.LIBUNITTESTCPP;
    private static final String MSG_OK = "ok";
    private static final String MSG_FAILED = "FAIL";
    private static final String MSG_SKIP = "SKIP";
    private static boolean firstSuite;


    public LibunittestCppTestFinishedHandler()
    {
        super("^(.+?)::(.+?) \\.{3} \\[([0-9].*?)s\\] (ok|FAIL|SKIP).*?$", true, true);
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
        final Matcher m = getMatcher();
        final String suiteName = normalise(m.group(1));
        CndTestSuite currentSuite = (CndTestSuite) session.getCurrentSuite();

        if( isSameTestSuite(currentSuite, suiteName) == false )
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

            currentSuite = new CndTestSuite(suiteName, TESTFRAMEWORK);
            session.addSuite(currentSuite);
            manager.displaySuiteRunning(session, currentSuite);
        }
        
        final String testName = normalise(m.group(2));
        CndTestCase testCase = new CndTestCase(testName, TESTFRAMEWORK, session);
        testCase.setClassName(suiteName);
        testCase.setTimeMillis(TestSupportUtils.parseTimeSecToMillis(m.group(3)));
        
        final String result = m.group(4);
        
        if( result.equals(MSG_FAILED) == true )
        {
            testCase.setError();
        }
        else if( result.equals(MSG_SKIP) == true )
        {
            testCase.setSkipped();
        }
        else
        {
            /* Empty */
        }
        
        session.addTestCase(testCase);
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
    
}
    
