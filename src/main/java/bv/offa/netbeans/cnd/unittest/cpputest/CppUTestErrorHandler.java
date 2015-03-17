package bv.offa.netbeans.cnd.unittest.cpputest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;

/**
 * The class {@code CppUTestErrorHandler} handles test errors and their
 * information.
 */
class CppUTestErrorHandler extends TestRecognizerHandler
{
    private final TestSessionInformation info;


    public CppUTestErrorHandler(TestSessionInformation info)
    {
        super("^(.+?)\\:([0-9]+?)\\: error\\: Failure in TEST\\(([^, ]+?), ([^, ]+?)\\)$", true); //NOI18N
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
        Testcase tc = ts.getCurrentTestCase();

        if( tc != null && tc.getClassName().equals(matcher.group(3))
                && tc.getName().equals(matcher.group(4)) )
        {
            final String location = matcher.group(1) + ":" + matcher.group(2);
            tc.setLocation(location);

            Trouble t = tc.getTrouble();

            if( t == null )
            {
                t = new Trouble(true);
            }

            t.setError(true);
            t.setStackTrace(new String[] { location });
            tc.setTrouble(t);
        }
    }

}
