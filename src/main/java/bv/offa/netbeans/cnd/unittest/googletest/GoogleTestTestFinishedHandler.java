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

package bv.offa.netbeans.cnd.unittest.googletest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import java.util.regex.Matcher;
import org.netbeans.modules.gsf.testrunner.api.TestSession;


/**
 * The class {@code GoogleTestTestFinishedHandler} handles the finish of a
 * test case.
 * 
 * @author offa
 */
class GoogleTestTestFinishedHandler extends CndTestHandler
{
    private static final String MSG_OK = "     OK";
    private static final String MSG_FAILED = "FAILED ";


    public GoogleTestTestFinishedHandler()
    {
        super("^.*?\\[  (     OK|FAILED ) \\].*? (.+?)\\.(.+?)(?:/.+)??"
            + " \\(([0-9]+?) ms\\)$", true, true);
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
        final CndTestCase testCase = (CndTestCase) session.getCurrentTestCase();
        
        if( isSameTestCase(testCase, m.group(3), m.group(2)) == true )
        {
            long time = Long.valueOf(m.group(4));
            testCase.setTimeMillis(time);
            
            final String location = m.group(2) + ":" + m.group(3);
            testCase.setLocation(location);
            
            final String result = m.group(1);

            if( result.equals(MSG_FAILED) == true )
            {
                testCase.setError(new String[] { location });
            }
        }
        else
        {
            throw new IllegalStateException("No test found for: " 
                                            + m.group(2) + ":" + m.group(3));
        }
    }

}
