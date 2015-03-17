package bv.offa.netbeans.cnd.unittest.googletest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;

/**
 * The class {@code GoogleTestSessionFinishedHandler} handles the finish of
 * a test session.
 */
class GoogleTestSessionFinishedHandler extends TestRecognizerHandler
{

    public GoogleTestSessionFinishedHandler()
    {
        super("^.*?\\[[=]{10}\\].*? [0-9]+? tests?? from [0-9]+? test cases?? ran\\. \\(([0-9]+?) ms total\\)$", true); //NOI18N
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
        final long time = Long.parseLong(matcher.group(1));
        mngr.displayReport(ts, ts.getReport(time));
        mngr.sessionFinished(ts);
    }
}

    
