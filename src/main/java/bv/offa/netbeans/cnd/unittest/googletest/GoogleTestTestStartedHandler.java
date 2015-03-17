package bv.offa.netbeans.cnd.unittest.googletest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;

/**
 * The class {@code GoogleTestTestStartedHandler} handles the start of
 * a test case.
 */
class GoogleTestTestStartedHandler extends TestRecognizerHandler
{
    private static final String GOOGLETEST = "GoogleTest"; //NOI18N
    
    
    public GoogleTestTestStartedHandler()
    {
        super("^.*?\\[ RUN      \\].*? (.+?)\\.(.+?)$", true); //NOI18N
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
        final Testcase testcase = new Testcase(matcher.group(2), GOOGLETEST, ts);
        testcase.setClassName(matcher.group(1));
        ts.addTestCase(testcase);
    }

}
