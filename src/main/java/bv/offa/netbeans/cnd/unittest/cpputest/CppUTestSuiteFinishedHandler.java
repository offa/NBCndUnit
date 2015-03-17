package bv.offa.netbeans.cnd.unittest.cpputest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code CppUTestSuiteFinishedHandler} handles the test end.
 */
class CppUTestSuiteFinishedHandler extends TestRecognizerHandler
{
    private final TestSessionInformation info;


    public CppUTestSuiteFinishedHandler(TestSessionInformation info)
    {
        super("(\u001B\\[[;\\d]*m)?(Errors|OK) \\([0-9]+?.+?\\)(\u001B\\[[;\\d]*m)?$", true); //NOI18N
        this.info = info;
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
        mngr.displayReport(ts, ts.getReport(info.getTimeTotal()));
        mngr.sessionFinished(ts);
        info.setTimeTotal(0L);
    }

}
