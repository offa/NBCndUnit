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

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

/**
 * The class {@code CndTestHandler} is the base class for
 * {@code TestRecognizerHandler} with adapted {@code Manager} API.
 * 
 * @author offa
 */
public abstract class CndTestHandler extends TestRecognizerHandler
{
    public CndTestHandler(String regex, boolean wrapRegex, boolean performOutput)
    {
        super(regex, true, true);
    }

    
    /**
     * Updates the UI. This method should not be used directly - use 
     * {@link #updateUI(ManagerAdapter, TestSession)} instead.
     * 
     * @param manager       Manager
     * @param session       Test session
     */
    @Override
    public void updateUI(Manager manager, TestSession session)
    {
        updateUI(ManagerAdapterImpl.adapt(manager), session);
    }
    
    
    /**
     * Updates the UI.
     * 
     * @param manager       Manager Adapter
     * @param session       Test session
     */
    public abstract void updateUI(ManagerAdapter manager, TestSession session);
        
    
    /**
     * Returns whether {@code testCase} and the Test Case described by the
     * given name and suite are equal.
     * 
     * @param testCase      Test Case
     * @param otherName     Name of the other Test Case
     * @param otherSuite    Suite of the other Test Case
     * @return              Returns {@code true} if both match or {@code false}
     *                      otherwise
     */
    protected boolean isSameTestCase(CndTestCase testCase, String otherName, String otherSuite)
    {
        return ( testCase != null ) 
                && ( testCase.getName().equals(otherName) == true ) 
                && ( testCase.getClassName().equals(otherSuite) == true );
    }
    
    
    /**
     * Returns whether {@code testSuite} and the Test Suite described by the
     * given name are equal.
     * 
     * @param testSuite         Test Suite
     * @param otherName         Name of the other Test Suite
     * @return                  Returns {@code true} if both match or
     *                          {@code false} otherwise
     */
    protected boolean isSameTestSuite(CndTestSuite testSuite, String otherName)
    {
        return ( testSuite != null ) && ( testSuite.getName().equals(otherName) == true );
    }
    
    
    /**
     * Returns the match result captured by the {@code group}.
     * 
     * @param group     Group
     * @return          Matched String
     */
    protected String getMatchGroup(int group)
    {
        return getMatcher().group(group);
    }
}
