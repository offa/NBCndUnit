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

package bv.offa.netbeans.cnd.unittest.cpputest.teamcity;

import bv.offa.netbeans.cnd.unittest.cpputest.teamcity.CppUTestTCSuiteFinishedHandler;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Before;
import static org.mockito.Mockito.mock;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

public class CppUTestTcSuiteFinishedHandlerTest
{
    private CppUTestTCSuiteFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @Before
    public void setUp()
    {
        handler = new CppUTestTCSuiteFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestSuite()
    {
        Matcher m = checkedMatch(handler, "##teamcity[testSuiteFinished name='TestSuite']");
        assertEquals("TestSuite", m.group(1));
    }

    @Test
    public void updateUIHasNoInteraction()
    {
        handler.updateUI(manager, session);
    }

}
