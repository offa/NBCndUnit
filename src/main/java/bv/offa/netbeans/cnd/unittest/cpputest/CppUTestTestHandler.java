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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

/**
 * The class {@code CppUTestHandler} handles the test output.
 * 
 * @author offa
 */
class CppUTestTestHandler extends TestRecognizerHandler
{
    private static final TestFramework testFramework = TestFramework.CPPUTEST;
    private final TestSessionInformation info;


    public CppUTestTestHandler(TestSessionInformation info)
    {
        super("^(IGNORE_)??TEST\\(([^, ]+?), ([^, ]+?)\\)( \\- ([0-9]+?) ms)?$", true); //NOI18N
        this.info = info;
    }



    /**
     * Updates the ui and test states.
     * 
     * @param mngr  Manager
     * @param ts    Test session
     */
    @Override
    public void updateUI(Manager mngr, TestSession ts)
    {
        TestSupportUtils.enableNodeFactory(ts);
        TestSupportUtils.assertNodeFactory(ts);
        
        final String suiteName = matcher.group(2);
        TestSuite currentSuite = ts.getCurrentSuite();

        if( currentSuite == null )
        {
            mngr.testStarted(ts);
            currentSuite = new CndTestSuite(suiteName, testFramework);
            ts.addSuite(currentSuite);
            mngr.displaySuiteRunning(ts, currentSuite);
        }
        else if( currentSuite.getName().equals(suiteName) == false )
        {
            mngr.displayReport(ts, ts.getReport(info.getTimeTotal()));
            info.setTimeTotal(0L);

            TestSuite suite = new CndTestSuite(suiteName, testFramework);
            ts.addSuite(suite);
            mngr.displaySuiteRunning(ts, suite);
        }
        else
        {
            /* Empty */
        }

        Testcase testcase = new CndTestCase(matcher.group(3), testFramework, ts);
        testcase.setClassName(suiteName);

        if( matcher.group(1) != null )
        {
            testcase.setStatus(Status.SKIPPED);
        }
        else if( matcher.group(4) == null )
        {
            // Test time is separated, eg. failed or test with additional output
        }
        else
        {
            long testTime = Long.valueOf(matcher.group(5));
            testcase.setTimeMillis(testTime);
            info.addTime(testTime);
        }

        ts.addTestCase(testcase);
    }
}
