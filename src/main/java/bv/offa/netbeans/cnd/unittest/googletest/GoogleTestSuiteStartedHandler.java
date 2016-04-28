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

import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import java.util.regex.Matcher;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

    
/**
 * The class {@code GoogleTestSuiteStartedHandler} handles the start of a
 * test suite.
 * 
 * @author offa
 */
class GoogleTestSuiteStartedHandler extends CndTestHandler
{
    private static final TestFramework TESTFRAMEWORK = TestFramework.GOOGLETEST;
    private static boolean firstSuite;
    
    public GoogleTestSuiteStartedHandler()
    {
        super("^.*?\\[[-]{10}\\].*? [0-9]+? tests?? from (.+?)$", true, true);
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
        if( firstSuite == true )
        {
            manager.testStarted(session);
            firstSuite = false;
        }
        else
        {
            manager.displayReport(session, session.getReport(0));
        }
        
        final Matcher m = getMatcher();
        final String suiteName = m.group(1);
        CndTestSuite currentSuite = (CndTestSuite) session.getCurrentSuite();
        
        if( isSameTestSuite(currentSuite, suiteName) == false )
        {
            currentSuite = new CndTestSuite(suiteName, TESTFRAMEWORK);
            session.addSuite(currentSuite);
            manager.displaySuiteRunning(session, currentSuite);
        }
    }
    

    /**
     * Indicates the current suite has finished.
     */
    static void suiteFinished()
    {
        GoogleTestSuiteStartedHandler.firstSuite = true;
    }
}
    
    
