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

package bv.offa.netbeans.cnd.unittest.api;

import org.netbeans.modules.gsf.testrunner.api.Report;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

public class ManagerAdapterImpl implements ManagerAdapter
{
    private final Manager manager;

    protected ManagerAdapterImpl(Manager manager)
    {
        this.manager = manager;
    }
    

    /**
     * Displays the result report for the test session.
     * 
     * @param session       Test session
     * @param report        Report
     */
    @Override
    public void displayReport(TestSession session, Report report)
    {
        manager.displayReport(session, report);
    }

    
    /**
     * Finishes the test session.
     * 
     * @param session   Test session
     */
    @Override
    public void sessionFinished(TestSession session)
    {
        manager.sessionFinished(session);
    }

    
    /**
     * Starts the test session.
     * 
     * @param session       Test session
     */
    @Override
    public void testStarted(TestSession session)
    {
        manager.testStarted(session);
    }

    
    /**
     * Displays the test suite as running within the session.
     * 
     * @param session       Test session
     * @param suite         Test suite
     */
    @Override
    public void displaySuiteRunning(TestSession session, TestSuite suite)
    {
        manager.displaySuiteRunning(session, suite);
    }
    
    
    /**
     * Adapts the {@code Manager}.
     * 
     * @param manager   Manager
     * @return          Adapter for {@code manager}
     */
    public static ManagerAdapter adapt(Manager manager)
    {
        return new ManagerAdapterImpl(manager);
    }
    
}
