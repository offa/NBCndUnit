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
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
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
    
    public static void goToSourceOfTestSuite(Project project, TestSuite testSuite)
    {
        final String uniqueDecl = getUniqueDeclaratonName(testSuite);
        goToDeclaration(project, uniqueDecl);
    }
    
    public static void goToSourceOfTestCase(Project project, Testcase testCase)
    {
        final String uniqueDecl = getUniqueDeclaratonName(testCase);
        goToDeclaration(project, uniqueDecl);
    }
    
    private static void goToDeclaration(final Project project, final String uniqueDeclaration)
    {
        threadPool.submit(new Callable<Boolean>()
        {
            @Override
            public Boolean call() throws Exception
            {
                
                CsmProject csmProject = CsmModelAccessor.getModel().getProject(project);
                CsmDeclaration decl = csmProject.findDeclaration(uniqueDeclaration);
                
                if( decl != null )
                {
                    return CsmUtilities.openSource(decl);
                }
                else
                {
                    logger.log(Level.INFO, "No declaration found for '{0}'", 
                            uniqueDeclaration);
                }
                
                return Boolean.FALSE;
            }
        });
    }
    
    
    
    private static String getUniqueDeclaratonName(Testcase testCase)
    {
        // CppUTest only atm.
        return "C:TEST_" + testCase.getClassName() + "_" + testCase.getName() + "_Test";
    }
    
    private static String getUniqueDeclaratonName(TestSuite testSuite)
    {
        // CppUTest only atm.
        return "S:TEST_GROUP_CppUTestGroup" + testSuite.getName();
    }
}
