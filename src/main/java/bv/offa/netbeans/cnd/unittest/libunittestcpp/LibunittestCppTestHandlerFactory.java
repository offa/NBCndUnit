/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015  offa
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

package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import bv.offa.netbeans.cnd.unittest.ui.TestRunnerUINodeFactory;
import java.util.ArrayList;
import java.util.List;
import org.netbeans.modules.cnd.testrunner.spi.TestHandlerFactory;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;

/**
 * The class {@code LibunittestCppTestHandlerFactory} implements a factory for
 * <i>libunittestc++</i> test handler.
 * 
 * @author offa
 */
public class LibunittestCppTestHandlerFactory implements TestHandlerFactory
{
    
    /**
     * Creates handlers for the unit test output.
     * 
     * @return  Test output handler
     */
    @Override
    public List<TestRecognizerHandler> createHandlers()
    {
        Manager.getInstance().setNodeFactory(new TestRunnerUINodeFactory());
        
        List<TestRecognizerHandler> testHandler = new ArrayList<TestRecognizerHandler>();
        testHandler.add(new LibunittestCppTestFinishedHandler());
        testHandler.add(new LibunittestCppTestSessionFinishedHandler());
        
        return testHandler;
    }
    
    
    /**
     * Returns whether a summary is printed.
     * 
     * @return  Always {@code true}
     */
    @Override
    public boolean printSummary()
    {
        return true;
    }
    
    
    /**
     * Parses the input string - containing a seconds time - to milliseconds.
     * The value is rounded.
     * 
     * @param str   Input string (sec)
     * @return      Time in ms or {@code 0L} if an invalid or negative time is
     *              passed
     */
    static long parseSecTimeToMillis(String str)
    {
        long result = 0L;
        
        try
        {
            final double value = Double.parseDouble(str) * 1000.0;
            
            if( Math.signum(value) > 0 )
            {
                result = Math.round(value);
            }
            
        }
        catch( NumberFormatException ex )
        {
            result = 0L;
        }
        
        return result;
    }
    
}
