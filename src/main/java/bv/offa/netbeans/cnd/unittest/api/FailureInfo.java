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

package bv.offa.netbeans.cnd.unittest.api;

/**
 * The class {@code FailureInfo} contains information about failed tests. This
 * is necessary because {@link org.netbeans.modules.gsf.testrunner.api.Trouble Trouble}
 * can't be extended with further details of a failed test.
 * 
 * @author offa
 */
public class FailureInfo
{
    private final String file;
    private final int line;
    
    public FailureInfo(String file, int line)
    {
        this.file = file;
        this.line = line;
    }

    
    /**
     * Returns the file of failure.
     * 
     * @return      File
     */
    public String getFile()
    {
        return file;
    }

    
    /**
     * Returns the line of failure.
     * 
     * @return      Line
     */
    public int getLine()
    {
        return line;
    }
    
}
