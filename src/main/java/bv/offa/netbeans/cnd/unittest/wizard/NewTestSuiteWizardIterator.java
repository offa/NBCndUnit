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

package bv.offa.netbeans.cnd.unittest.wizard;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;
import org.openide.util.NbBundle.Messages;

/**
 * The class {@code NewTestSuiteWizardIterator} implements a wizard iterator for
 * test mains.
 *
 * @author offa
 */
@TemplateRegistrations({
    @TemplateRegistration(position = 10,
                        folder = "UnitTests",
                        displayName = "#CppUTestWizardIterator_displayName",
                        iconBase = "bv/offa/netbeans/cnd/unittest/wizard/cpputest_suite.png",
                        content = "../templates/CppUTestTestSuite.cpp",
                        description = "../templates/CppUTest_Suite_Description.html",
                        scriptEngine = "freemarker",
                        targetName = "#CppUTestWizardIterator_targetName",
                        category = { "cpp-types", "c-types" }),
    @TemplateRegistration(position = 20,
                        folder = "UnitTests",
                        displayName = "#GoogleTestWizardIterator_displayName",
                        iconBase = "bv/offa/netbeans/cnd/unittest/wizard/googletest_suite.png",
                        content = "../templates/GoogleTestTestSuite.cpp",
                        description = "../templates/GoogleTest_Suite_Description.html",
                        scriptEngine = "freemarker",
                        targetName = "#GoogleTestWizardIterator_targetName",
                        category = { "cpp-types", "c-types" }),
    @TemplateRegistration(position = 30,
                        folder = "UnitTests",
                        displayName = "#LibunittestCppWizardIterator_displayName",
                        iconBase = "bv/offa/netbeans/cnd/unittest/wizard/libunittestcpp_suite.png",
                        content = "../templates/LibunittestCppTestSuite.cpp",
                        description = "../templates/LibunittestCpp_Suite_Description.html",
                        scriptEngine = "freemarker",
                        targetName = "#LibunittestCppWizardIterator_targetName",
                        category = { "cpp-types", "c-types" }) })
@Messages({ "CppUTestWizardIterator_displayName=CppUTest Test Suite",
            "CppUTestWizardIterator_targetName=TestSuite",
            "GoogleTestWizardIterator_displayName=GoogleTest Test Suite",
            "GoogleTestWizardIterator_targetName=TestSuite",
            "LibunittestCppWizardIterator_displayName=Libunittest C++ Test Suite",
            "LibunittestCppWizardIterator_targetName=TestSuite" })
public final class NewTestSuiteWizardIterator extends AbstractWizardIterator
{
    public NewTestSuiteWizardIterator()
    {
        super(new NewTestSuiteWizardPanel1());
    }



    /**
     * Creates a map of template parameters.
     *
     * @return  Template parameter (parameter name / value)
     */
    @Override
    protected Map<String, Object> getTemplateParameters()
    {
        boolean generateSetup = (Boolean) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_SETUP);
        boolean generateTeardown = (Boolean) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TEARDOWN);
        boolean generateTestCases = (Boolean) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_GENERATE_TESTCASES);
        boolean enableModernCpp = (Boolean) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_ENABLE_MODERNCPP);
        @SuppressWarnings("unchecked")
        List<String> testCases = (List<String>) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTCASE_NAMES);
        String testSuite = (String) wizard.getProperty(NewTestSuiteWizardPanel1.PROP_TEST_TESTSUITE_NAME);

        Map<String, Object> args = new HashMap<>();
        args.put("suiteName", testSuite);
        args.put("generateSetup", generateSetup);
        args.put("generateTeardown", generateTeardown);
        args.put("enableModernCpp", enableModernCpp);
        args.put("testCases", testCases);
        args.put("generateTestCases", generateTestCases);

        return args;
    }

}
