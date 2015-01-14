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

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;
import org.netbeans.modules.gsf.testrunner.api.Manager;

/**
 * The class {@code CppUTestTestHandlerFactory} implements a factory for
 * test handler.
 * 
 * @author  offa
 */
public class CppUTestTestHandlerFactory implements TestHandlerFactory
{
    private static final String CPPUTEST = "CppUTest"; //NOI18N

    
    /**
     * Creates heandlers for the unit test output.
     * 
     * @return  Test output handler
     */
    @Override
    public List<TestRecognizerHandler> createHandlers()
    {
        TestSessionInformation info = new TestSessionInformation();

        List<TestRecognizerHandler> testHandler = new ArrayList<TestRecognizerHandler>();
        testHandler.add(new CppUTestHandler(info));
        testHandler.add(new CppUTestSuiteFinishedHandler(info));
        testHandler.add(new CppUTestErrorHandler(info));

        return testHandler;
    }

    
    /**
     * Returns whether a summary is printed.
     * 
     * @return  Always {@code true}
     */
    @Override
    public boolean printSummary()
    {
        return true;
    }
    
    
    
    /**
     * The class {@code CppUTestHandler} handles test output.
     */
    static class CppUTestHandler extends TestRecognizerHandler
    {
        private final TestSessionInformation info;

        
        public CppUTestHandler(TestSessionInformation info)
        {
            super("^TEST\\(([^, ]+?), ([^, ]+?)\\)( \\- ([0-9]+?) ms)?$"); //NOI18N
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
            final String suiteName = matcher.group(1);
            final TestSuite currentSuite = ts.getCurrentSuite();

            if( currentSuite == null )
            {
                mngr.testStarted(ts);
                ts.addSuite(new TestSuite(suiteName));
                mngr.displaySuiteRunning(ts, currentSuite);
            }
            else if( currentSuite.getName().equals(suiteName) == false )
            {
                mngr.displayReport(ts, ts.getReport(info.getTimeTotal()));
                info.setTimeTotal(0L);

                TestSuite suite = new TestSuite(suiteName);
                ts.addSuite(suite);
                mngr.displaySuiteRunning(ts, suite);
            }
            else
            {
                /* Empty */
            }

            Testcase testcase = new Testcase(matcher.group(2), CPPUTEST, ts);
            testcase.setClassName(suiteName);

            if( matcher.group(3) == null )
            {
                testcase.setTrouble(new Trouble(false));
            }
            else
            {
                long testTime = Long.valueOf(matcher.group(4));
                testcase.setTimeMillis(testTime);
                info.addTime(testTime);
            }

            ts.addTestCase(testcase);
        }
    }

    
    
    /**
     * The class {@code CppUTestSuiteFinishedHandler} handles the test end.
     */
    static class CppUTestSuiteFinishedHandler extends TestRecognizerHandler
    {
        private final TestSessionInformation info;

        
        public CppUTestSuiteFinishedHandler(TestSessionInformation info)
        {
            super("(\u001B\\[[;\\d]*m)?(Errors|OK) \\([0-9]+?.+?\\)(\u001B\\[[;\\d]*m)?$", true); //NOI18N
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
            mngr.displayReport(ts, ts.getReport(info.getTimeTotal()));
            mngr.sessionFinished(ts);
            info.setTimeTotal(0L);
        }

    }

    
    
    /**
     * The class {@code CppUTestErrorHandler} handles test errors and their
     * information.
     */
    static class CppUTestErrorHandler extends TestRecognizerHandler
    {
        private final TestSessionInformation info;

        
        public CppUTestErrorHandler(TestSessionInformation info)
        {
            super("^(.+?)\\:([0-9]+?)\\: error\\: Failure in TEST\\(([^, ]+?), ([^, ]+?)\\)$", true); //NOI18N
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
            Testcase tc = ts.getCurrentTestCase();

            if( tc != null && tc.getClassName().equals(matcher.group(3))
                    && tc.getName().equals(matcher.group(4)) )
            {
                final String location = matcher.group(1) + ":" + matcher.group(2);
                tc.setLocation(location);

                Trouble t = tc.getTrouble();

                if( t == null )
                {
                    t = new Trouble(true);
                }

                t.setError(true);
                t.setStackTrace(new String[] { location });
                tc.setTrouble(t);
            }
        }

    }

    
    
    /**
     * The class {@code TestSessionInformation} holds data for sharing between
     * handler instances.
     * 
     * <p>
     * <i>This class is not intended for usage outside the test handlers.</i>
     * </p>
     */
    static class TestSessionInformation
    {
        private long timeTotal;

        public TestSessionInformation()
        {
            this.timeTotal = 0L;
        }

        
        
        /**
         * Returns the total time.
         *
         * @return Total time
         */
        public long getTimeTotal()
        {
            return timeTotal;
        }

        
        /**
         * Sets the total time.
         *
         * @param timeTotal New totoal time
         */
        void setTimeTotal(long timeTotal)
        {
            this.timeTotal = timeTotal;
        }

        
        /**
         * Adds the {@code time} to the total time.
         *
         * @param time Time
         */
        public void addTime(long time)
        {
            this.timeTotal += time;
        }
        
    }
}
