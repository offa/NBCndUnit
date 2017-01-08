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

package bv.offa.netbeans.cnd.unittest.cpputest.teamcity;

import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactoryProvider;
import org.openide.util.lookup.ServiceProvider;

/**
 * The class {@code CppUTestTCTestHandlerFactoryProvider} implements a service
 * that provides test handler for <i>CppUTest (TeamCity)</i> unit tests.
 *
 * @author offa
 */
@ServiceProvider(service = TestHandlerFactoryProvider.class)
public class CppUTestTCTestHandlerFactoryProvider implements TestHandlerFactoryProvider
{
    
    /**
     * Creates a test handler factory.
     *
     * @return  Test handler factory
     */
    @Override
    public TestHandlerFactory getFactory()
    {
        return new CppUTestTCTestHandlerFactory();
    }

}
