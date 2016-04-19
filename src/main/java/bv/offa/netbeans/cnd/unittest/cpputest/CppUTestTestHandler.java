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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

/**
 * The class {@code CppUTestHandler} handles the test output.
 *
 * @author offa
 */
class CppUTestTestHandler extends CndTestHandler
{

    private static final TestFramework TESTFRAMEWORK = TestFramework.CPPUTEST;
    private static boolean firstSuite;
    private final TestSessionInformation info;

    public CppUTestTestHandler(TestSessionInformation info)
    {
        super("^(IGNORE_)??TEST\\(([^, ]+?), ([^, ]+?)\\)"
                + "( \\- ([0-9]+?) ms)?$", true, true);
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
        final Matcher m = getMatcher();
        final String suiteName = m.group(2);
        TestSuite currentSuite = session.getCurrentSuite();

        if( currentSuite == null || currentSuite.getName().equals(suiteName) == false )
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
        
        Testcase testcase = new CndTestCase(m.group(3), TESTFRAMEWORK, session);
        testcase.setClassName(suiteName);
        
        if( m.group(1) != null )
        {
            testcase.setStatus(Status.SKIPPED);
        }
        else if( m.group(4) != null )
        {
            long testTime = Long.valueOf(m.group(5));
            testcase.setTimeMillis(testTime);
            info.addTime(testTime);
        }
        else
        {
            // Test time is separated, eg. failed or test with additional output
        }
        
        session.addTestCase(testcase);
    }
    
    /**
     * Indicates the current suite has finished.
     */
    static void suiteFinished()
    {
        CppUTestTestHandler.firstSuite = true;
    }

}
