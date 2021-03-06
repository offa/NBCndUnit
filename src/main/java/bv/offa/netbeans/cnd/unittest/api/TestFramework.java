/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2021  offa
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

/**
 * The enum {@code TestFramework} represents test frameworks.
 *
 * @author offa
 */
public enum TestFramework
{
    /** CppUTest. */
    CPPUTEST("CppUTest"),
    /** GoogleTest / GoogleMock. */
    GOOGLETEST("GoogleTest"),
    /** LibunittestC++. */
    LIBUNITTESTCPP("LibunittestCpp");


    private final String name;


    TestFramework(String name)
    {
        this.name = name;
    }


    /**
     * Returns the name of the framework.
     *
     * @return  Name
     */
    public String getName()
    {
        return name;
    }
}
