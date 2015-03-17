package bv.offa.netbeans.cnd.unittest.cpputest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.Status;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.TestSuite;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;


/**
 * The class {@code CppUTestHandler} handles the test output.
 */
class CppUTestTestHandler extends TestRecognizerHandler
{
    static final String CPPUTEST = "CppUTest"; //NOI18N
    private final TestSessionInformation info;


    public CppUTestTestHandler(TestSessionInformation info)
    {
        super("^(IGNORE_)??TEST\\(([^, ]+?), ([^, ]+?)\\)( \\- ([0-9]+?) ms)?$", true); //NOI18N
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
        final String suiteName = matcher.group(2);
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
            mngr.displayReport(ts, ts.getReport(info.getTimeTotal()));
            info.setTimeTotal(0L);

            TestSuite suite = new TestSuite(suiteName);
            ts.addSuite(suite);
            mngr.displaySuiteRunning(ts, suite);
        }
        else
        {
            /* Empty */
        }

        Testcase testcase = new Testcase(matcher.group(3), CPPUTEST, ts);
        testcase.setClassName(suiteName);

        if( matcher.group(1) != null )
        {
            testcase.setStatus(Status.SKIPPED);
        }
        else if( matcher.group(4) == null )
        {
            testcase.setTrouble(new Trouble(false));
        }
        else
        {
            long testTime = Long.valueOf(matcher.group(5));
            testcase.setTimeMillis(testTime);
            info.addTime(testTime);
        }

        ts.addTestCase(testcase);
    }
}
