package bv.offa.netbeans.cnd.unittest.googletest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code GoogleTestSuiteFinishedHandler} handles the finish of a
 * test suite.
 */
class GoogleTestSuiteFinishedHandler extends TestRecognizerHandler
{
    public GoogleTestSuiteFinishedHandler()
    {
        super("^.*?\\[[-]{10}\\].*? [0-9]+? tests?? from ([^ ]+?) \\(([0-9]+?) ms total\\)$", true); //NOI18N
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
        /*
         * Disabled since this causes an assert failure if a trouble is set.
         */
    }
}
