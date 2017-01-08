/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2017  offa
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
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

    
/**
 * The class {@code GoogleTestSuiteStartedHandler} handles the start of a
 * test suite.
 * 
 * @author offa
 */
public class GoogleTestSuiteStartedHandler extends CndTestHandler
{
    private static final int GROUP_SUITE = 1;
    private static boolean firstSuite;
    
    public GoogleTestSuiteStartedHandler()
    {
        super(TestFramework.GOOGLETEST, "^.*?\\[[-]{10}\\].*? [0-9]+? tests?? from (.+?)$");
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
        final String suiteName = getMatchGroup(GROUP_SUITE);
        
        if( isSameTestSuite(currentSuite(session), suiteName) == false )
        {
            updateSessionState(manager, session);
            startNewTestSuite(suiteName, session, manager);
        }
    }

    
    /**
     * Updates the session state.
     * 
     * @param manager   Manager
     * @param session   Session
     */
    private void updateSessionState(ManagerAdapter manager, TestSession session)
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
    }
    

    /**
     * Indicates the current suite has finished.
     */
    static void suiteFinished()
    {
        GoogleTestSuiteStartedHandler.firstSuite = true;
    }
    
}
    
    
