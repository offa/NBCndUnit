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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.ManagerAdapter;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import static bv.offa.netbeans.cnd.unittest.testhelper.Helper.checkedMatch;
import static com.google.common.truth.Truth.assertThat;
import static bv.offa.netbeans.cnd.unittest.testhelper.TestCaseSubject.assertThat;
import java.util.regex.Matcher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import static org.mockito.Mockito.*;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

@Tag("Test-Framework")
@Tag("CppUTest")
public class CppUTestTimeHandlerTest
{
    private static final TestFramework FRAMEWORK = TestFramework.CPPUTEST;
    private TestSessionInformation info;
    private CppUTestTimeHandler handler;
    private TestSession session;
    private ManagerAdapter manager;


    @BeforeEach
    public void setUp()
    {
        info = new TestSessionInformation();
        handler = new CppUTestTimeHandler(info);
        session = mock(TestSession.class);
        manager = mock(ManagerAdapter.class);
    }

    @Test
    public void parsesDataTime()
    {
        assertThat(checkedMatch(handler, " - 0 ms").group(1)).isEqualTo("0");

        Matcher m = handler.match(" - 123 ms");
        assertThat(m.matches()).isTrue();
        assertThat(m.group(1)).isEqualTo("123");
    }

    @Test
    public void updateUIUpdatesTime()
    {
        checkedMatch(handler, " - 123 ms");
        CndTestCase testCase = new CndTestCase("testCase", FRAMEWORK, session);
        when(session.getCurrentTestCase()).thenReturn(testCase);
        handler.updateUI(manager, session);
        assertThat(info.getTimeTotal()).isEqualTo(123L);
        assertThat(testCase).isTime(123l);
    }

}
