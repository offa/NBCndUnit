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

import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

/**
 * The class {@code CppUTestTimeHandler} handles test time if it's separated
 * from the main test output.
 * 
 * @author offa
 */
class CppUTestTimeHandler extends CndTestHandler
{
    private static final int GROUP_TIME = 1;
    private final TestSessionInformation info;
    
    public CppUTestTimeHandler(TestSessionInformation info)
    {
        super(TestFramework.CPPUTEST, "^ \\- ([0-9]+?) ms$");
        this.info = info;
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
        Testcase testCase = session.getCurrentTestCase();
        
        if( testCase != null )
        {
            final String time = getMatchGroup(GROUP_TIME);
            long testTime = Long.valueOf(time);
            testCase.setTimeMillis(testTime);
            info.addTime(testTime);
        }
    }
    
}
