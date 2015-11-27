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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.TestSupportUtils;
import java.util.regex.Matcher;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code LibunittestCppTestSessionFinishedHandler} handles the 
 * test session end.
 * 
 * @author offa
 */
class LibunittestCppTestSessionFinishedHandler extends TestRecognizerHandler
{

    public LibunittestCppTestSessionFinishedHandler()
    {
        super("^Ran [0-9]+? tests in ([0-9].+?)s$", true, true);
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
        final Matcher m = getMatcher();
        final long time = TestSupportUtils.parseTimeSecToMillis(m.group(1));
        mngr.displayReport(ts, ts.getReport(time));
        mngr.sessionFinished(ts);
        
        LibunittestCppTestFinishedHandler.suiteFinished();
    }

}
