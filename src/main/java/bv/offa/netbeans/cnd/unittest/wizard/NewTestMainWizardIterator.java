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

package bv.offa.netbeans.cnd.unittest.wizard;

import java.util.HashMap;
import java.util.Map;
import org.netbeans.api.templates.TemplateRegistration;
import org.netbeans.api.templates.TemplateRegistrations;
import org.openide.util.NbBundle.Messages;

/**
 * The class {@code NewTestMainWizardIterator} implements a wizard iterator for
 * test mains.
 * 
 * @author offa
 */
@TemplateRegistrations({
    @TemplateRegistration(position = 11,
                        folder = "UnitTests",
                        displayName = "#CppUTestMainWizardIterator_displayName", 
                        iconBase = "bv/offa/netbeans/cnd/unittest/wizard/cpputest_main.png", 
                        content = "../templates/CppUTestTestMain.cpp",
                        description = "../templates/CppUTest_Main_Description.html",
                        scriptEngine = "freemarker",
                        targetName = "#CppUTestMainWizardIterator_targetName",
                        category = { "cpp-types", "c-types" }),
    @TemplateRegistration(position = 31,
                        folder = "UnitTests",
                        displayName = "#LibunittestCppMainWizardIterator_displayName", 
                        iconBase = "bv/offa/netbeans/cnd/unittest/wizard/libunittestcpp_main.png", 
                        content = "../templates/LibunittestCppTestMain.cpp",
                        description = "../templates/LibunittestCpp_Main_Description.html",
                        scriptEngine = "freemarker",
                        targetName = "#LibunittestCppMainWizardIterator_targetName",
                        category = { "cpp-types", "c-types" }) })
@Messages({ "CppUTestMainWizardIterator_displayName=CppUTest Main",
            "CppUTestMainWizardIterator_targetName=AllTests",
            "LibunittestCppMainWizardIterator_displayName=Libunittest C++ Main",
            "LibunittestCppMainWizardIterator_targetName=AllTests" })
public final class NewTestMainWizardIterator extends AbstractWizardIterator
{
    public NewTestMainWizardIterator()
    {
        super(new NewTestMainWizardPanel1());
    }
    
    
    
    /**
     * Creates a map of template parameters.
     * 
     * @return  Template parameter (parameter name / value)
     */
    @Override
    protected Map<String, Object> getTemplateParameters()
    {
        boolean enableVerbose = (Boolean) wizard.getProperty(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_VERBOSE);
        boolean enableColor = (Boolean) wizard.getProperty(NewTestMainWizardPanel1.PROP_MAIN_ENABLE_COLOR);
        
        Map<String, Object> args = new HashMap<>();
        args.put("enableVerbose", enableVerbose);
        args.put("enableColor", enableColor);
        
        return args;
    }
    
}
