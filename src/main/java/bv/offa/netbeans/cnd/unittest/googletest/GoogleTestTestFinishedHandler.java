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

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;


/**
 * The class {@code GoogleTestTestFinishedHandler} handles the finish of a
 * test case.
 * 
 * @author offa
 */
class GoogleTestTestFinishedHandler extends TestRecognizerHandler
{
    private static final String MSG_OK = "     OK"; //NOI18N
    private static final String MSG_FAILED = "FAILED "; //NOI18N


    public GoogleTestTestFinishedHandler()
    {
        super("^.*?\\[  (     OK|FAILED ) \\].*? (.+?)\\.(.+?) \\(([0-9]+?) ms\\)$", true); //NOI18N
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
        TestSupportUtils.assertNodeFactory(mngr);
        
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
                throw new IllegalStateException("Unknown result: <" + result + ">"); //NOI18N
            }
        }
        else
        {
            throw new IllegalStateException("No test found for: " 
                    + matcher.group(2) + ":" + matcher.group(3));
        }
    }

}
