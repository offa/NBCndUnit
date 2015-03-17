package bv.offa.netbeans.cnd.unittest.googletest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;

    
/**
 * The class {@code GoogleTestSuiteStartedHandler} handles the start of a
 * test suite.
 */
class GoogleTestSuiteStartedHandler extends TestRecognizerHandler
{

    public GoogleTestSuiteStartedHandler()
    {
        super("^.*?\\[[-]{10}\\].*? [0-9]+? tests?? from ([^ ]+?)$", true); //NOI18N
    }



    /**
     * Updates the ui and test states.
     * 
     * @param mngr  Manager
     * @param ts    Test session
     */
    @Override
    public void updateUI(Manager mngr, TestSession ts)
    {
        final String suiteName = matcher.group(1);
        TestSuite currentSuite = ts.getCurrentSuite();

        if( currentSuite == null )
        {
            mngr.testStarted(ts);
            currentSuite = new TestSuite(suiteName);
            ts.addSuite(currentSuite);
            mngr.displaySuiteRunning(ts, currentSuite);
        }
        else if( currentSuite.getName().equals(suiteName) == false )
        {
            mngr.displayReport(ts, ts.getReport(0));

            TestSuite suite = new TestSuite(suiteName);
            ts.addSuite(suite);
            mngr.displaySuiteRunning(ts, suite);
        }
        else
        {
            /* Empty */
        }
    }
}
    
    
