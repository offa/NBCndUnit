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
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import java.util.regex.Matcher;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code CppUTestErrorHandler} handles test errors and their
 * information.
 * 
 * @author offa
 */
class CppUTestErrorHandler extends CndTestHandler
{
    public CppUTestErrorHandler(TestSessionInformation info)
    {
        super("^(.+?)\\:([0-9]+?)\\: error\\: Failure in "
                + "TEST\\(([^, ]+?), ([^, ]+?)\\)$", true, true);
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
        CndTestCase testCase = (CndTestCase) session.getCurrentTestCase();
        final Matcher m = getMatcher();

        if( isSameTestCase(testCase, m.group(4), m.group(3)) == true )
        {
            final String location = m.group(1) + ":" + m.group(2);
            testCase.setLocation(location);
            testCase.setError(new String[] { location });
        }
    }

}
