/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2018  offa
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
import bv.offa.netbeans.cnd.unittest.api.FailureInfo;
import java.util.concurrent.Callable;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.netbeans.api.project.Project;
import org.netbeans.modules.cnd.api.model.CsmDeclaration;
import org.netbeans.modules.cnd.api.model.CsmFile;
import org.netbeans.modules.cnd.api.model.CsmModelAccessor;
import org.netbeans.modules.cnd.api.model.CsmProject;
import org.netbeans.modules.cnd.modelutil.CsmUtilities;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.openide.util.RequestProcessor;

/**
 * The class {@code TestSupportUtils} provides methods to work with tests.
 *
 * @author offa
 */
public final class TestSupportUtils
{
    private static final RequestProcessor POOL = new RequestProcessor("NBCndUnit Utility Processor", 1);
    private static final Logger LOGGER = Logger.getLogger(TestSupportUtils.class.getName());

    private TestSupportUtils()
    {
        /* Empty */
    }



    /**
     * Parses the input string - containing a seconds time - to milliseconds.
     * The value is rounded.
     *
     * @param str   Input string (sec)
     * @return      Time in ms or {@code 0L} if an invalid or negative time is
     *               passed
     */
    public static long parseTimeSecToMillis(String str)
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
     * Executes a Go-To-Source to the given failure. If the jump
     * target isn't available, this method does nothing.
     *
     * <p>The execution is done in a task; the method doesn't block.</p>
     *
     * @param project       Project
     * @param failure       Failure
     */
    public static void goToSourceOfFailure(final Project project, final FailureInfo failure)
    {
        POOL.submit(() ->
        {
            CsmProject csmProject = CsmModelAccessor.getModel().getProject(project);
            final String fileName = failure.getFile();
            final int line = failure.getLine();

            for( CsmFile f : csmProject.getSourceFiles() )
            {
                if( f.getAbsolutePath().toString().endsWith(fileName) == true )
                {
                    return CsmUtilities.openSource(f, line, 0);
                }
            }

            LOGGER.log(Level.INFO, "No source found for {0}:{1}", new Object[] { fileName, line });
            return Boolean.FALSE;
        });
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
        POOL.submit(() ->
        {
            CsmProject csmProject = CsmModelAccessor.getModel().getProject(project);
            CsmDeclaration decl = csmProject.findDeclaration(uniqueDeclaration);

            if( decl != null )
            {
                return CsmUtilities.openSource(decl);
            }
            else
            {
                LOGGER.log(Level.INFO, "No declaration found for {0}", uniqueDeclaration);
            }

            return Boolean.FALSE;
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
                return "C:" +  enabled + testCase.getClassName()
                        + "_" + testCase.getName() + "_Test";
            case GOOGLETEST:
                return "C:" + removeGTestParameter(testCase.getClassName())
                        + "_" + testCase.getName() + "_Test";
            case LIBUNITTESTCPP:
                return "S:" + testCase.getClassName() + "::"
                        + removeLibUnittestScope(testCase.getName());
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
                return "C:" + removeGTestParameter(testSuite.getName());
            case LIBUNITTESTCPP:
                return "S:" + testSuite.getName() + "::__testcollection_child__";
            default:
                throw new IllegalArgumentException("Unsupported framework: "
                        + testSuite.getFramework().getName());
        }
    }


    /**
     * Removes parameter strings from gtest suite name.
     *
     * @param suiteName     Suite name
     * @return              Cleared suite name
     */
    private static String removeGTestParameter(String suiteName)
    {
        if( suiteName.contains("/") == true )
        {
            return suiteName.replaceFirst(".*?/", "");
        }

        return suiteName;
    }


    /**
     * Removes scope strings from libunittestcpp test case name.
     *
     * @param testCase      Test case name
     * @return              Cleared test case name
     */
    private static String removeLibUnittestScope(String testCase)
    {
        final int sepPos = testCase.indexOf("::");

        if( sepPos != -1 )
        {
            return testCase.substring(0, sepPos);
        }

        return testCase;
    }
}
