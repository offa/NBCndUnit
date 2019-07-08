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

import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static bv.offa.netbeans.cnd.unittest.testhelper.MockArgumentMatcher.isTestOfFramework;
import static com.google.common.truth.Truth.assertThat;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

@Tag("Test-Framework")
@Tag("GoogleTest")
public class GoogleTestTestStartedHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.GOOGLETEST;
    private GoogleTestTestStartedHandler handler;
    private TestSession session;
    private ManagerAdapter manager;

    @BeforeEach
    public void setUp()
    {
        handler = new GoogleTestTestStartedHandler();
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parseDataTestCase()
    {
        Matcher m = checkedMatch(handler, "[ RUN      ] TestSuite.testCase");
        assertThat(m.group(1)).isEqualTo("TestSuite");
        assertThat(m.group(2)).isEqualTo("testCase");
    }

    @Test
    public void parseDataTestCaseParameterized()
    {
        Matcher m = checkedMatch(handler, "[ RUN      ] withParameterImpl/TestSuite.testCase/0");
        assertThat(m.group(1)).isEqualTo("withParameterImpl/TestSuite");
        assertThat(m.group(2)).isEqualTo("testCase");
    }

    @Test
    public void updateUIAddsTestCase()
    {
        checkedMatch(handler, "[ RUN      ] TestSuite.testCase");
        handler.updateUI(manager, session);
        verify(session).addTestCase(argThat(isTestOfFramework("TestSuite", "testCase", FRAMEWORK)));
    }

}
