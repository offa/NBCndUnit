
package bv.offa.netbeans.cnd.unittest.googletest;

import org.netbeans.modules.cnd.testrunner.spi.TestRecognizerHandler;
import org.netbeans.modules.gsf.testrunner.api.Manager;
import org.netbeans.modules.gsf.testrunner.api.TestSession;
import org.netbeans.modules.gsf.testrunner.api.Testcase;
import org.netbeans.modules.gsf.testrunner.api.Trouble;


/**
 * The class {@code GoogleTestTestFinishedHandler} handles the finish of a
 * test case.
 */
class GoogleTestTestFinishedHandler extends TestRecognizerHandler
{
    private static final String MSG_OK = "     OK"; //NOI18N
    private static final String MSG_FAILED = "FAILED "; //NOI18N


    public GoogleTestTestFinishedHandler()
    {
        super("^.*?\\[  (     OK|FAILED ) \\].*? (.+?)\\.(.+?) \\(([0-9]+?) ms\\)$", true); //NOI18N
    }



    /**
     * Updates the ui and test states.
     * 
     * @param mngr  Manager
     * @param ts    Test session
     * @exception IllegalStateException If the handler gets into an
     *                                  illegal state or parses unknown
     *                                  output values
     */
    @Override
    public void updateUI(Manager mngr, TestSession ts)
    {
        final Testcase testCase = ts.getCurrentTestCase();

        if( testCase != null && testCase.getClassName().equals(matcher.group(2)) 
                && testCase.getName().equals(matcher.group(3)) )
        {
            long time = Long.valueOf(matcher.group(4));
            testCase.setTimeMillis(time);

            final String location = matcher.group(2) + ":" + matcher.group(3);
            testCase.setLocation(location);

            final String result = matcher.group(1);

            if( result.equals(MSG_OK) == true )
            {
                // Testcase ok
            }
            else if( result.equals(MSG_FAILED) == true )
            {
                Trouble trouble = testCase.getTrouble();

                if( trouble == null )
                {
                    trouble = new Trouble(true);
                }

                trouble.setError(true);
                trouble.setStackTrace(new String[] { location });
                testCase.setTrouble(trouble);
            }
            else
            {
                throw new IllegalStateException("Unknown result: <" + result + ">"); //NOI18N
            }
        }
        else
        {
            throw new IllegalStateException("No test found for: " 
                    + matcher.group(2) + ":" + matcher.group(3));
        }
    }

}
