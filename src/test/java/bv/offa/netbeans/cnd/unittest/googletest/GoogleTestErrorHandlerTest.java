/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2019  offa
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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.FailureInfo;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.createCurrentTestCase;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestMatcher.hasError;
import java.util.regex.Matcher;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.mock;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

@Tag("Test-Framework")
@Tag("GoogleTest")
public class GoogleTestErrorHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.GOOGLETEST;
    private GoogleTestErrorHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @BeforeEach
    public void setUp()
    {
        handler = new GoogleTestErrorHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }


    @Test
    public void parseDataFailure()
    {
        Matcher m = checkedMatch(handler, "test/Example.cpp:38: Failure");
        assertEquals("test/Example.cpp", m.group(1));
        assertEquals("38", m.group(2));
    }

    @Test
    public void updateUISetsFailureInfo()
    {
        CndTestCase testCase = createCurrentTestCase("TestSuite", "testCase", FRAMEWORK, session);
        checkedMatch(handler, "test/Example.cpp:38: Failure");
        handler.updateUI(manager, session);
        assertThat(testCase, hasError());
        FailureInfo failure = testCase.getFailureInfo();
        assertEquals("test/Example.cpp", failure.getFile());
        assertEquals(38, failure.getLine());
        assertEquals("test/Example.cpp:38", testCase.getTrouble().getStackTrace()[0]);
    }

}
