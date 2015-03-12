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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

/**
 * The class {@code LibunittestCppTestHandlerFactory} implements a factory for
 * <i>libunittestc++</i> test handler.
 * 
 * @author offa
 */
public class LibunittestCppTestHandlerFactory implements TestHandlerFactory
{
    private static final String LIBUNITTESTCPP = "LibunittestC++"; //NOI18N
    
    
    /**
     * Creates handlers for the unit test output.
     * 
     * @return  Test output handler
     */
    @Override
    public List<TestRecognizerHandler> createHandlers()
    {
        List<TestRecognizerHandler> testHandler = new ArrayList<TestRecognizerHandler>();
        testHandler.add(new LibunittestCppTestFinishedHandler());
        testHandler.add(new LibunittestCppTestSessionFinishedHandler());
        
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
     * Parses the input string - containing a seconds time - to milliseconds.
     * The value is rounded.
     * 
     * @param str   Input string (sec)
     * @return      Time in ms or {@code 0L} if an invalid or negative time is
     *              passed
     */
    static long parseSecTimeToMillis(String str)
    {
        long result = 0L;
        
        try
        {
            final double value = Double.parseDouble(str) * 1000.0;
            
            if( Math.signum(value) > 0 )
            {
                result = Math.round(value);
            }
            
        }
        catch( NumberFormatException ex )
        {
            result = 0L;
        }
        
        return result;
    }
    
    
    
    /**
     * The class {@code LibunittestCppTestFinishedHandler} handles the test
     * output.
     */
    static class LibunittestCppTestFinishedHandler extends TestRecognizerHandler
    {
        private static final String MSG_OK = "ok"; //NOI18N
        private static final String MSG_FAILED = "FAIL"; //NOI18N
        private static final String MSG_SKIP = "SKIP"; //NOI18N
        
        
        public LibunittestCppTestFinishedHandler()
        {
            super("^(.+?)::(.+?) \\.{3} \\[([0-9].*?)s\\] (ok|FAIL|SKIP).*?$", true); //NOI18N
        }
        
        
        
        /**
         * Updates the ui and test states.
         * 
         * @param mngr  Manager
         * @param ts    Test session
         * @exception IllegalStateException If the handler gets into an
         *                                  illegal state or parses unknown
         *                                  output values
         */
        @Override
        public void updateUI(Manager mngr, TestSession ts)
        {
            final String suiteName = normalise(matcher.group(1));
            TestSuite currentSuite = ts.getCurrentSuite();
            
            if( currentSuite == null )
            {
                mngr.testStarted(ts);
                currentSuite = new TestSuite(suiteName);
                ts.addSuite(currentSuite);
                mngr.displaySuiteRunning(ts, currentSuite);
            }
            else if( currentSuite.getName().equals(suiteName) == false )
            {
                mngr.displayReport(ts, ts.getReport(0L));

                TestSuite suite = new TestSuite(suiteName);
                ts.addSuite(suite);
                mngr.displaySuiteRunning(ts, suite);
            }
            else
            {
                /* Empty */
            }
            
            final String testName = normalise(matcher.group(2));
            Testcase testCase = new Testcase(testName, LIBUNITTESTCPP, ts);
            testCase.setClassName(suiteName);
            testCase.setTimeMillis(parseSecTimeToMillis(matcher.group(3)));
            
            final String result = matcher.group(4);
            
            if( result.equals(MSG_OK) == true )
            {
                // Testcase ok
            }
            else if( result.equals(MSG_FAILED) == true )
            {
                Trouble trouble = new Trouble(true);
                testCase.setTrouble(trouble);
            }
            else if( result.equals(MSG_SKIP) == true )
            {
                testCase.setStatus(Status.SKIPPED);
            }
            else
            {
                throw new IllegalStateException("Unknown result: <" + result + ">"); //NOI18N
            }
            
            ts.addTestCase(testCase);
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
    
    
    
    /**
     * The class {@code LibunittestCppTestSessionFinishedHandler} handles the 
     * test session end.
     */
    static class LibunittestCppTestSessionFinishedHandler extends TestRecognizerHandler
    {

        public LibunittestCppTestSessionFinishedHandler()
        {
            super("^Ran [0-9]+? tests in ([0-9].+?)s$", true); //NOI18N
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
            final long time = parseSecTimeToMillis(matcher.group(1));
            mngr.displayReport(ts, ts.getReport(time));
            mngr.sessionFinished(ts);
        }
        
    }
    
}
