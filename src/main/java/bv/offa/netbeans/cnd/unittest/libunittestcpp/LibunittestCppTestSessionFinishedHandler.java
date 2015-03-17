package bv.offa.netbeans.cnd.unittest.libunittestcpp;

import static bv.offa.netbeans.cnd.unittest.libunittestcpp.LibunittestCppTestHandlerFactory.parseSecTimeToMillis;
import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code LibunittestCppTestSessionFinishedHandler} handles the 
 * test session end.
 */
class LibunittestCppTestSessionFinishedHandler extends TestRecognizerHandler
{

    public LibunittestCppTestSessionFinishedHandler()
    {
        super("^Ran [0-9]+? tests in ([0-9].+?)s$", true); //NOI18N
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
        final long time = parseSecTimeToMillis(matcher.group(1));
        mngr.displayReport(ts, ts.getReport(time));
        mngr.sessionFinished(ts);
    }

}
