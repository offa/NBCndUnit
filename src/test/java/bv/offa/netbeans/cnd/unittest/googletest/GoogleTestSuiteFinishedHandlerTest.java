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

import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import java.util.regex.Matcher;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

@Tag("Test-Framework")
@Tag("GoogleTest")
public class GoogleTestSuiteFinishedHandlerTest
{
    private GoogleTestSuiteFinishedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;


    @BeforeEach
    public void setUp()
    {
        handler = new GoogleTestSuiteFinishedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataSuccessfulTestSuite()
    {
        Matcher m = checkedMatch(handler, "[----------] 3 tests from TestSuite (259 ms total)");
        assertEquals("TestSuite", m.group(1));
        assertEquals("259", m.group(2));
    }

    @Test
    public void parseDataSingleTestSuite()
    {
        Matcher m = checkedMatch(handler, "[----------] 1 test from TestSuite (123 ms total)");
        assertEquals("TestSuite", m.group(1));
        assertEquals("123", m.group(2));
    }

    @Test
    public void updateUIHasNoInteraction()
    {
        handler.updateUI(manager, session);
    }

}
