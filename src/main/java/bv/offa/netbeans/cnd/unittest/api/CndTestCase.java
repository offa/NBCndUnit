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

import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

/**
 * The class {@code CndTestCase} extends {@link Testcase Testcase} with
 * additional information.
 * 
 * @author offa
 */
public class CndTestCase extends Testcase
{
    private final TestFramework framework;
    
    
    public CndTestCase(String name, TestFramework framework, TestSession session)
    {
        super(name, framework.getName(), session);
        this.framework = framework;
    }

    
    /**
     * Returns the framework.
     * 
     * @return  Framework
     */
    public TestFramework getFramework()
    {
        return framework;
    }
    
}
