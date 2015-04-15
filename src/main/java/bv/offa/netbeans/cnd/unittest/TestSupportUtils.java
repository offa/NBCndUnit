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

package bv.offa.netbeans.cnd.unittest;

import bv.offa.netbeans.cnd.unittest.ui.TestRunnerUINodeFactory;
import java.lang.reflect.Field;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.api.model.CsmDeclaration;
import org.netbeans.modules.cnd.api.model.CsmModelAccessor;
import org.netbeans.modules.cnd.api.model.CsmProject;
import org.netbeans.modules.cnd.modelutil.CsmUtilities;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.openide.util.RequestProcessor;

public final class TestSupportUtils
{
    private static final RequestProcessor threadPool = new RequestProcessor("NBCndUnit Utility Processor", 1);
    private static final Logger logger = Logger.getLogger(TestSupportUtils.class.getName());
    
    private TestSupportUtils()
    {
        /* Empty */
    }

    
    public static void enableNodeFactory(TestSession ts)
    {
        if( ts.getNodeFactory() instanceof TestRunnerUINodeFactory == false )
        {
            try
            {
                Field nodeFactory = ts.getClass().getDeclaredField("nodeFactory");
                nodeFactory.setAccessible(true);
                nodeFactory.set(ts, new TestRunnerUINodeFactory());
            }
            catch( NoSuchFieldException ex )
            {
                logger.log(Level.WARNING, "Unable to set Node Factory", ex);
            }
            catch( SecurityException ex )
            {
                logger.log(Level.WARNING, "Unable to set Node Factory", ex);
            }
            catch( IllegalArgumentException ex )
            {
                logger.log(Level.WARNING, "Unable to set Node Factory", ex);
            }
            catch( IllegalAccessException ex )
            {
                logger.log(Level.WARNING, "Unable to set Node Factory", ex);
            }
        }
    }
    
    
    public static void goToSourceOfTestCase(final Project project, final Testcase testCase)
    {
        threadPool.submit(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                final String uniqueDeclaration = "C:" + getTargetDeclaratonName(testCase);
                CsmProject csmProject = CsmModelAccessor.getModel().getProject(project);
                CsmDeclaration decl = csmProject.findDeclaration(uniqueDeclaration);
                
                if( decl != null )
                {
                    return CsmUtilities.openSource(decl);
                }
                else
                {
                    logger.log(Level.INFO, "No declaration found for '{0}' ({1})", 
                            new Object[] { uniqueDeclaration, getLoggerParamInfo() });
                }
                
                return Boolean.FALSE;
            }
            
            private String getLoggerParamInfo()
            {
                return "Testcase: " + testCase.getClassName() 
                        + "::" + testCase.getName() 
                        + ", Project: " 
                        + project.getProjectDirectory().getName();
            }
        });
    }
    
    private static String getTargetDeclaratonName(Testcase testcase)
    {
        // CppUTest only atm.
        return "TEST_" + testcase.getClassName() + "_" + testcase.getName() + "_Test";
    }
}
