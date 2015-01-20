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

package bv.offa.netbeans.cnd.unittest.googletest;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

/**
 * The class {@code GoogleTestTestHandlerFactory} implements a factory for
 * test handler.
 * 
 * @author  offa
 */
public class GoogleTestTestHandlerFactory implements TestHandlerFactory
{
    private static final String GOOGLETEST = "GoogleTest";


    /**
     * Creates heandlers for the unit test output.
     * 
     * @return  Test output handler
     */
    @Override
    public List<TestRecognizerHandler> createHandlers()
    {
        List<TestRecognizerHandler> testHandler = new ArrayList<TestRecognizerHandler>();
        testHandler.add(new GoogleTestSuiteStartedHandler());
        testHandler.add(new GoogleTestSuiteFinishedHandler());
        testHandler.add(new GoogleTestTestStartedHandler());
        testHandler.add(new GoogleTestTestFinishedHandler());
        testHandler.add(new GoogleTestSessionFinishedHandler());
        
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
     * The class {@code GoogleTestSuiteStartedHandler} handles the start of a
     * test suite.
     */
    static class GoogleTestSuiteStartedHandler extends TestRecognizerHandler
    {
        
        public GoogleTestSuiteStartedHandler()
        {
            super("^.*?\\[[-]{10}\\].*? [0-9]+? tests from ([^ ]+?)$");
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
                mngr.displayReport(ts, ts.getReport(0));

                TestSuite suite = new TestSuite(suiteName);
                ts.addSuite(suite);
                mngr.displaySuiteRunning(ts, suite);
            }
            else
            {
                /* Empty */
            }
        }
    }
    
    
    
    /**
     * The class {@code GoogleTestSuiteFinishedHandler} handles the finish of a
     * test suite.
     */
    static class GoogleTestSuiteFinishedHandler extends TestRecognizerHandler
    {
        public GoogleTestSuiteFinishedHandler()
        {
            super("^.*?\\[[-]{10}\\].*? [0-9]+? tests from ([^ ]+?) \\(([0-9]+?) ms total\\)$");
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
            /*
             * Disabled since this causes an assert failure if a trouble is set.
             */
            
//            long time = Long.parseLong(matcher.group(2));
//            mngr.displayReport(ts, ts.getReport(time));
        }
    }
    
    
    
    /**
     * The class {@code GoogleTestSessionFinishedHandler} handles the finish of
     * a test session.
     */
    static class GoogleTestSessionFinishedHandler extends TestRecognizerHandler
    {
        
        public GoogleTestSessionFinishedHandler()
        {
            super("^.*?\\[[=]{10}\\].*? [0-9]+? tests from [0-9]+? test cases ran\\. \\(([0-9]+?) ms total\\)$");
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
            final long time = Long.parseLong(matcher.group(1));
            mngr.displayReport(ts, ts.getReport(time));
            mngr.sessionFinished(ts);
        }
    }
    
    
    
    /**
     * The class {@code GoogleTestTestStartedHandler} handles the start of
     * a test case.
     */
    static class GoogleTestTestStartedHandler extends TestRecognizerHandler
    {

        public GoogleTestTestStartedHandler()
        {
            super("^.*?\\[ RUN      \\].*? (.+?)\\.(.+?)$");
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
            final TestSuite currentTestSuite = ts.getCurrentSuite();
            
            if( currentTestSuite == null )
            {
                throw new IllegalStateException("No suite found for test "
                        + matcher.group(1) + "." + matcher.group(2));
            }
            
            Testcase testcase = new Testcase(matcher.group(2), GOOGLETEST, ts);
            testcase.setClassName(matcher.group(1));
            ts.addTestCase(testcase);
        }
        
    }
    
    
    
    /**
     * The class {@code GoogleTestTestFinishedHandler} handles the finish of a
     * test case.
     */
    static class GoogleTestTestFinishedHandler extends TestRecognizerHandler
    {
        private static final String MSG_OK = "     OK";
        private static final String MSG_FAILED = "FAILED ";

        
        public GoogleTestTestFinishedHandler()
        {
            super("^.*?\\[  (     OK|FAILED ) \\].*? (.+?)\\.(.+?) \\(([0-9]+?) ms\\)$");
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
            final Testcase testCase = ts.getCurrentTestCase();
            
            if( testCase != null && testCase.getClassName().equals(matcher.group(2)) 
                    && testCase.getName().equals(matcher.group(3)) )
            {
                long time = Long.valueOf(matcher.group(4));
                testCase.setTimeMillis(time);
                
                final String location = matcher.group(2) + ":" + matcher.group(3);
                testCase.setLocation(location);
                
                final String result = matcher.group(1);
                
                if( result.equals(MSG_OK) == true )
                {
                    // Testcase ok
                }
                else if( result.equals(MSG_FAILED) == true )
                {
                    Trouble trouble = testCase.getTrouble();
                    
                    if( trouble == null )
                    {
                        trouble = new Trouble(true);
                    }
                    
                    trouble.setError(true);
                    trouble.setStackTrace(new String[] { location });
                    testCase.setTrouble(trouble);
                }
                else
                {
                    throw new IllegalStateException("Unknown result: <" + result + ">");
                }
            }
            else
            {
                throw new IllegalStateException("No test found for: " 
                        + matcher.group(2) + "." + matcher.group(3));
            }
        }
        
    }

}
