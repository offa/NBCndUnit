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

import java.util.List;
import org.openide.WizardDescriptor;
import org.openide.util.HelpCtx;

/**
 * The class {@code NewTestSuiteWizardPanel1} implements a wizard panel for
 * test mains.
 *
 * @author offa
 */
public class NewTestSuiteWizardPanel1 extends AbstractWizardPanel
{
    protected static final String PROP_TEST_GENERATE_SETUP = "PROP_GENERATE_SETUP";
    protected static final String PROP_TEST_GENERATE_TEARDOWN = "PROP_GENERATE_TEARDOWN";
    protected static final String PROP_TEST_GENERATE_TESTCASES = "PROP_GENERATE_TESTCASES";
    protected static final String PROP_TEST_TESTCASE_NAMES = "PROP_TESTCASE_NAMES";
    protected static final String PROP_TEST_TESTSUITE_NAME = "PROP_TESTSUITE_NAME";
    protected static final String PROP_TEST_ENABLE_MODERNCPP = "PROP_TESTSUITE_ENABLE_MODERNCPP";
    private NewTestSuiteVisualPanel1 component;
    private WizardDescriptor wizard;


    /**
     * Gets the visual component.
     *
     * @return  Component
     */
    @Override
    public NewTestSuiteVisualPanel1 getComponent()
    {
        if( component == null )
        {
            component = new NewTestSuiteVisualPanel1();
            component.addChangeListener(this);
        }
        return component;
    }


    /**
     * Help for this panel. When the panel is active, this is used as the help
     * for the wizard dialog.
     *
     * @return  The help or {@code null} if no help is supplied
     */
    @Override
    public HelpCtx getHelp()
    {
        return HelpCtx.DEFAULT_HELP;
    }


    /**
     * Tests whether the panel meets all conditions to be valid. If so, the
     * panel is finished and it is safe to proceed to the next one.
     *
     * <p><b>Note:</b> The "Next" (or "Finish") button will only be enabled if
     * this method returns {@code true}.</p>
     *
     * @return  Returns {@code true} if valid, or {@code false} otherwise
     */
    @Override
    public boolean isValid()
    {
        setErrorMessage(null);

        final String suiteName = getTestSuiteNameFromVisualComponent();
        final List<String> testCases = getTestCaseNamesFromVisualComponent();
        final boolean genTestCases = getGenerateTestCasesFromVisualComponent();

        if( genTestCases == true )
        {
            if( checkTestCases(testCases) == false )
            {
                return false;
            }
        }

        return checkSuiteName(suiteName);
    }


    /**
     * Reads the settings from the wizard descriptor.
     *
     * @param wiz   Wizard descriptor
     */
    @Override
    public void readSettings(WizardDescriptor wiz)
    {
        this.wizard = wiz;
    }


    /**
     * Stores the settings to the wizard descriptor.
     *
     * @param wiz   Wizard descriptor
     */
    @Override
    public void storeSettings(WizardDescriptor wiz)
    {
        if( isNextOption(wiz.getValue()) == true )
        {
            wiz.putProperty(PROP_TEST_GENERATE_SETUP, getGenerateSetupFromVisualPanel());
            wiz.putProperty(PROP_TEST_GENERATE_TEARDOWN, getGenerateTeardownFromVisualPanel());
            wiz.putProperty(PROP_TEST_GENERATE_TESTCASES, getGenerateTestCasesFromVisualComponent());
            wiz.putProperty(PROP_TEST_ENABLE_MODERNCPP, getEnableModernCppFromVisualComponent());
            wiz.putProperty(PROP_TEST_TESTCASE_NAMES, getTestCaseNamesFromVisualComponent());
            wiz.putProperty(PROP_TEST_TESTSUITE_NAME, getTestSuiteNameFromVisualComponent());
            wiz.putProperty(PROP_CONFIGURE_CUSTOM_PROJECT, getConfigureCustomProjectFromVisualPanel());
        }
    }


    /**
     * Sets the error message. If the current wizard descriptor is {@code null}
     * this method does nothing.
     *
     * @param msg   Message
     */
    protected void setErrorMessage(String msg)
    {
        if( wizard != null )
        {
            wizard.putProperty(WizardDescriptor.PROP_ERROR_MESSAGE, msg);
        }
    }


    /**
     * Sets the visual component.
     *
     * @param comp  Component
     */
    void setComponent(NewTestSuiteVisualPanel1 comp)
    {
        this.component = comp;
    }


    /**
     * Returns the setup generation setting from the visual component.
     *
     * @return  Setting
     */
    private boolean getGenerateSetupFromVisualPanel()
    {
        return getComponent().getGenerateSetup();
    }


    /**
     * Returns the teardown generation setting from the visual component.
     *
     * @return  Setting
     */
    private boolean getGenerateTeardownFromVisualPanel()
    {
        return getComponent().getGenerateTeardown();
    }


    /**
     * Returns the test case names from the visual component.
     *
     * @return  Test case names
     */
    private List<String> getTestCaseNamesFromVisualComponent()
    {
        return getComponent().getTestCaseNames();
    }


    /**
     * Returns the test suite name from the visual component.
     *
     * @return  Test suite name
     */
    private String getTestSuiteNameFromVisualComponent()
    {
        return getComponent().getTestSuiteName();
    }



    /**
     * Returns whether to configure a custom project from the visual component.
     *
     * @return  Configure Setting
     */
    private boolean getConfigureCustomProjectFromVisualPanel()
    {
        return getComponent().getConfigureCustomProject();
    }


    /**
     * Returns the test case generation setting from the visual component.
     *
     * @return  Setting
     */
    private boolean getGenerateTestCasesFromVisualComponent()
    {
        return getComponent().getGenerateTestCases();
    }


    /**
     * Returns the <i>modern C++</i> setting from the visual component.
     *
     * @return  Setting
     */
    private boolean getEnableModernCppFromVisualComponent()
    {
        return getComponent().getEnableModernCpp();
    }


    /**
     * Checks if {@code testCases} are valid test cases. If not,
     * an error is set.
     *
     * @param testCases     Test cases
     * @return              Returns <tt>true</tt> if valid or <tt>false</tt>
     *                      otherwise
     */
    private boolean checkTestCases(List<String> testCases)
    {
        if( testCases.isEmpty() == true )
        {
            setErrorMessage(getMessageFromBundle("MSG_Err_Empty_TestCase_Name"));
            return false;
        }

        if( WizardUtils.isValidIdentifier(testCases.get(0)) == false )
        {
            setErrorMessage(getMessageFromBundle("MSG_Err_Invalid_TestCase_Name"));
            return false;
        }
        
        return true;
    }


    /**
     * Checks if {@suiteName} is a valid test suite name. If not,
     * an error is set.
     *
     * @param testCases     Test suiten name
     * @return              Returns <tt>true</tt> if valid or <tt>false</tt>
     *                      otherwise
     */
    private boolean checkSuiteName(String suiteName)
    {
        if( suiteName.isEmpty() == true )
        {
            setErrorMessage(getMessageFromBundle("MSG_Err_Empty_TestSuite_Name"));
            return false;
        }
        else if( WizardUtils.isValidIdentifier(suiteName) == false )
        {
            setErrorMessage(getMessageFromBundle("MSG_Err_Invalid_TestSuite_Name"));
            return false;
        }
        else
        {
            return true;
        }
    }

}
