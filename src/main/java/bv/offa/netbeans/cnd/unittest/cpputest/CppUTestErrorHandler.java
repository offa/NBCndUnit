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
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

/**
 * The class {@code CppUTestErrorHandler} handles test errors and their
 * information.
 * 
 * @author offa
 */
class CppUTestErrorHandler extends TestRecognizerHandler
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
        TestSupportUtils.assertNodeFactory(ts);
        
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
