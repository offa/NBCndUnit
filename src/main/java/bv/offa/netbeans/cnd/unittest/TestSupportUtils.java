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

import bv.offa.netbeans.cnd.unittest.api.CndTestCase;
import bv.offa.netbeans.cnd.unittest.api.CndTestSuite;
import bv.offa.netbeans.cnd.unittest.ui.TestRunnerUINodeFactory;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.api.model.CsmDeclaration;
import org.netbeans.modules.cnd.api.model.CsmModelAccessor;
import org.netbeans.modules.cnd.api.model.CsmProject;
import org.netbeans.modules.cnd.modelutil.CsmUtilities;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.ui.api.Manager;
import org.openide.util.RequestProcessor;

/**
 * The class {@code TestSupportUtils} provides methods to work with tests.
 * 
 * @author offa
 */
public final class TestSupportUtils
{
    private static final RequestProcessor threadPool = new RequestProcessor("NBCndUnit Utility Processor", 1);
    private static final Logger logger = Logger.getLogger(TestSupportUtils.class.getName());
    
    private TestSupportUtils()
    {
        /* Empty */
    }

    
    /**
     * Tests whether the correct node factory instance is set; if not, an
     * exception is thrown.
     * 
     * <p>It's safe to call this method with a {@code mngr} that's {@code null};
     * in that case it does nothing.</p>
     * 
     * @param mngr  Manager (can be {@code null})
     * @exception   IllegalStateException - if there's a different node factory
     *              set
     */
    public static void assertNodeFactory(Manager mngr)
    {
        if( mngr == null )
        {
            return;
        }
        
        if( mngr.getNodeFactory() instanceof TestRunnerUINodeFactory == false )
        {
            throw new IllegalStateException("Wrong node factory set (required: " 
                    + TestRunnerUINodeFactory.class.getName() 
                    + ", current: " + ( mngr.getNodeFactory() == null 
                                        ? null 
                                        : mngr.getNodeFactory().getClass().getName() )
                    + ")");
        }
    }
    
    
    /**
     * Enables a {@link TestRunnerUINodeFactory TestRunnerUINodeFactory} in the
     * {@link Manager Manager}. This is done by replacing the previous one.
     * 
     * <p>If there's already a instance of {@code TestRunnerUINodeFactory},
     * this method does nothing.</p>
     * 
     * @param mngr  Manager
     * @exception   RuntimeException - logs and rethrows previous exceptions
     */
    public static void enableNodeFactory(Manager mngr)
    {
        if( mngr.getNodeFactory() instanceof TestRunnerUINodeFactory == false )
        {
            mngr.setNodeFactory(new TestRunnerUINodeFactory());
        }
    }
    
    
    /**
     * Executes a Go-To-Source to the given TestSuite. If the jump target isn't 
     * available, this method does nothing.
     * 
     * <p>The execution is done in a task; the method doesn't block.</p>
     * 
     * @param project       Project
     * @param testSuite     TestSuite
     */
    public static void goToSourceOfTestSuite(Project project, CndTestSuite testSuite)
    {
        final String uniqueDecl = getUniqueDeclaratonName(testSuite);
        goToDeclaration(project, uniqueDecl);
    }
    
    
    /**
     * Executes a Go-To-Source to the given TestCase. If the jump target isn't 
     * available, this method does nothing.
     * 
     * <p>The execution is done in a task; the method doesn't block.</p>
     * 
     * @param project   Project
     * @param testCase  TestCase
     */
    public static void goToSourceOfTestCase(Project project, CndTestCase testCase)
    {
        final String uniqueDecl = getUniqueDeclaratonName(testCase);
        goToDeclaration(project, uniqueDecl);
    }
    
    
    /**
     * Executes a Go-To-Source to the given (unique) declaration. If the jump
     * target isn't available, this method does nothing.
     * 
     * <p>The execution is done in a task; the method doesn't block.</p>
     * 
     * @param project               Project
     * @param uniqueDeclaration     Unique declaration
     */
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
                    logger.log(Level.INFO, "No declaration found for {0}", uniqueDeclaration);
                }
                
                return Boolean.FALSE;
            }
        });
    }
    
    
    /**
     * Returns the unique declaration name for the TestCase.
     * 
     * @param testCase  TestCase
     * @return          Unique declaration name
     * @exception       IllegalArgumentException - if it's a TestCase of a
     *                  unsupported test framework.
     */
    static String getUniqueDeclaratonName(CndTestCase testCase)
    {
        switch(testCase.getFramework())
        {
            case CPPUTEST:
                final String enabled = ( testCase.getStatus() == Status.SKIPPED ? "IGNORE" : "TEST_" );
                return "C:" +  enabled + testCase.getClassName() + "_" + testCase.getName() + "_Test";
            case GOOGLETEST:
                return "C:" + testCase.getClassName() + "_" + testCase.getName() + "_Test";
            case LIBUNITTESTCPP:
                String testName = testCase.getName();
                int sepPos = testName.indexOf("::");
                
                if( sepPos != -1 )
                {
                    testName = testName.substring(0, sepPos);
                }
                
                return "S:" + testCase.getClassName() + "::" + testName;
            default:
                throw new IllegalArgumentException("Unsupported framework: " 
                        + testCase.getFramework().getName());
        }
    }
    
    
    /**
     * Returns the unique declaration name for the TestSuite.
     * 
     * @param testSuite TestSuite
     * @return          Unique declaration name
     * @exception       IllegalArgumentException - if it's a TestCase of a
     *                  unsupported test framework.
     */
    static String getUniqueDeclaratonName(CndTestSuite testSuite)
    {
        switch(testSuite.getFramework())
        {
            case CPPUTEST:
                return "S:TEST_GROUP_CppUTestGroup" + testSuite.getName();
            case GOOGLETEST:
                return "C:" + testSuite.getName();
            case LIBUNITTESTCPP:
                return "S:" + testSuite.getName() + "::__testcollection_child__";
            default:
                throw new IllegalArgumentException("Unsupported framework: " 
                        + testSuite.getFramework().getName());
        }
    }
}
