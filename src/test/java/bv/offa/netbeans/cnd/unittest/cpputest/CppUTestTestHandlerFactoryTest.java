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

package bv.offa.netbeans.cnd.unittest.cpputest;

import bv.offa.netbeans.cnd.unittest.api.CndTestHandler;
import bv.offa.netbeans.cnd.unittest.api.TestFramework;
import bv.offa.netbeans.cnd.unittest.ui.TestRunnerUINodeFactory;
import static org.junit.Assert.*;
import org.junit.Before;
import org.junit.Test;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

public class CppUTestTestHandlerFactoryTest
{
    private TestHandlerFactory factory;


    @Before
    public void setUp()
    {
        factory = new CppUTestTestHandlerFactory();
    }

    @Test
    public void printSummary()
    {
        assertTrue(factory.printSummary());
    }

    @Test
    public void createHandlersContainsHandlers()
    {
        assertEquals(4, factory.createHandlers().size());
    }

    @Test
    public void factorySetsNodeFactory()
    {
        factory.createHandlers();
        assertTrue(Manager.getInstance().getNodeFactory() instanceof TestRunnerUINodeFactory);
    }

    @Test
    public void allHandlersForFramework()
    {
        for( TestRecognizerHandler h : factory.createHandlers() )
        {
            assertEquals(TestFramework.CPPUTEST, ((CndTestHandler) h).getTestFramework());
        }
    }

}
