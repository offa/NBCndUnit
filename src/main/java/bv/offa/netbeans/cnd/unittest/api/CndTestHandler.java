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
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

/**
 * The class {@code CndTestHandler} is the base class for
 * {@code TestRecognizerHandler} with adapted {@code Manager} API.
 *
 * @author offa
 */
public abstract class CndTestHandler extends TestRecognizerHandler
{
    protected final TestFramework framework;

    public CndTestHandler(TestFramework framework, String regex)
    {
        super(regex, true, true);
        this.framework = framework;
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
     * Returns the Testframework that is handled.
     *
     * @return      Testframework
     */
    public TestFramework getTestFramework()
    {
        return framework;
    }


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


    /**
     * Returns the current Test Suite of the session.
     *
     * @param session       Session
     * @return              Test Suite
     */
    protected CndTestSuite currentSuite(TestSession session)
    {
        return (CndTestSuite) session.getCurrentSuite();
    }


    /**
     * Returns the current Test Case of the session.
     *
     * @param session       Session
     * @return              Test Case
     */
    protected CndTestCase currentTestCase(TestSession session)
    {
        return (CndTestCase) session.getCurrentTestCase();
    }


    /**
     * Starts a new Test Suite for the session.
     *
     * @param suiteName     Test Suite
     * @param session       Session
     * @param manager       Manager
     * @return              Test Suite
     */
    protected CndTestSuite startNewTestSuite(String suiteName, TestSession session, ManagerAdapter manager)
    {
        CndTestSuite testSuite = new CndTestSuite(suiteName, framework);
        session.addSuite(testSuite);
        manager.displaySuiteRunning(session, testSuite);

        return testSuite;
    }


    /**
     * Starts a new Test Casefor the session.
     *
     * @param caseName      Test Case
     * @param suiteName     Test Suite
     * @param session       Session
     * @return              Test Case
     */
    protected CndTestCase startNewTestCase(String caseName, String suiteName, TestSession session)
    {
        CndTestCase testcase = new CndTestCase(caseName, framework, session);
        testcase.setClassName(suiteName);
        session.addTestCase(testcase);

        return testcase;
    }

}
