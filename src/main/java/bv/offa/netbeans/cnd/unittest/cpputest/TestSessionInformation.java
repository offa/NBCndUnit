package bv.offa.netbeans.cnd.unittest.cpputest;

/**
 * The class {@code TestSessionInformation} holds data for sharing between
 * handler instances.
 * 
 * <p>
 * <i>This class is not intended for usage outside the test handlers.</i>
 * </p>
 */
class TestSessionInformation
{
    private long timeTotal;

    public TestSessionInformation()
    {
        this.timeTotal = 0L;
    }



    /**
     * Returns the total time.
     *
     * @return Total time
     */
    public long getTimeTotal()
    {
        return timeTotal;
    }


    /**
     * Sets the total time.
     *
     * @param timeTotal New totoal time
     */
    void setTimeTotal(long timeTotal)
    {
        this.timeTotal = timeTotal;
    }


    /**
     * Adds the {@code time} to the total time.
     *
     * @param time Time
     */
    public void addTime(long time)
    {
        this.timeTotal += time;
    }

}
