/*
 * NBCndUnit - C/C++ unit tests for NetBeans.
 * Copyright (C) 2015-2016  offa
 *
 * This file is part of NBCndUnit.
 *
 * NBCndUnit is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * NBCndUnit is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with NBCndUnit.  If not, see <http://www.gnu.org/licenses/>.
 */

package bv.offa.netbeans.cnd.unittest.cpputest;

/**
 * The class {@code TestSessionInformation} holds data for sharing between
 * handler instances.
 *
 * <p><i>This class is not intended for usage outside the test handlers.</i></p>
 *
 * @author offa
 */
public class TestSessionInformation
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
    public void setTimeTotal(long timeTotal)
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
