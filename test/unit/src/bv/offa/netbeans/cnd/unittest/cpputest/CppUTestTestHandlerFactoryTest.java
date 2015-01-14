/*
 * NBCndUnit - C/C++ unit tests for NetBeans..
 * Copyright (C) 2015  offa
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

import bv.offa.netbeans.cnd.unittest.cpputest.CppUTestTestHandlerFactory.CppUTestErrorHandler;
import bv.offa.netbeans.cnd.unittest.cpputest.CppUTestTestHandlerFactory.CppUTestHandler;
import bv.offa.netbeans.cnd.unittest.cpputest.CppUTestTestHandlerFactory.CppUTestSuiteFinishedHandler;
import bv.offa.netbeans.cnd.unittest.cpputest.CppUTestTestHandlerFactory.TestSessionInformation;
import java.util.regex.Matcher;
import org.junit.Test;
import static org.junit.Assert.*;

public class CppUTestTestHandlerFactoryTest
{
    private static final TestSessionInformation DONT_CARE_INFO = new TestSessionInformation();

    
    @Test
    public void testCppUTestHandlerErrorFreeMatching()
    {
        final String input = "TEST(SuiteName, testCase1) - 0 ms\n"
                + "TEST(SuiteName, testCase2) - 8 ms\n"
                + "TEST(SuiteName, testCase3) - 123 ms\n"
                + "TEST(SuiteName, testCase4) - 10242 ms\n"
                + "TEST(SuiteName, testCase5) - 5 ms\n"
                + "TEST(SuiteName, testCase6) - 0 ms\n";

        final String lines[] = input.split("\n");
        CppUTestHandler handler = new CppUTestHandler(DONT_CARE_INFO);

        for( String line : lines )
        {
            assertTrue(handler.matches(line));

            Matcher m = handler.match(line);

            assertTrue(m.find());
            assertFalse(m.find());
            assertEquals(4, m.groupCount());
        }

        assertTrue(handler.matches("TEST(SuiteName, testCase)"));

        assertFalse(handler.matches("TEST(SuiteName, testCase) - 1"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) - a"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) - abc ms"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) - ms"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) -  ms"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) - 11 ms "));
        assertFalse(handler.matches("TEST(SuiteName, )"));
        assertFalse(handler.matches("TEST(SuiteName, testCase, wrong) - 5 ms"));
        assertFalse(handler.matches("TEST(SuiteName, testCase) - 5 ms - 7 ms"));
    }

    @Test
    public void testCppUTestHandlerErrorFreeParsing()
    {
        final String input = "TEST(SuiteName, testCase1) - 0 ms\n"
                + "TEST(SuiteName2, testCase1) - 8 ms\n"
                + "TEST(SuiteName2, testCase2) - 1234567890 ms\n";

        final String lines[] = input.split("\n");
        CppUTestHandler handler = new CppUTestHandler(DONT_CARE_INFO);

        Matcher m = handler.match(lines[0]);
        assertTrue(m.find());
        assertEquals(4, m.groupCount());
        assertEquals("TEST(SuiteName, testCase1) - 0 ms", m.group());
        assertEquals("SuiteName", m.group(1));
        assertEquals("testCase1", m.group(2));
        assertEquals(" - 0 ms", m.group(3));
        assertEquals("0", m.group(4));

        m = handler.match(lines[1]);
        assertTrue(m.find());
        assertEquals(4, m.groupCount());
        assertEquals("SuiteName2", m.group(1));
        assertEquals("testCase1", m.group(2));
        long time = Long.valueOf(m.group(4));
        assertEquals(8L, time);

        m = handler.match(lines[2]);
        assertTrue(m.find());
        assertEquals(4, m.groupCount());
        assertEquals("SuiteName2", m.group(1));
        assertEquals("testCase2", m.group(2));
        time = Long.valueOf(m.group(4));
        assertEquals(1234567890L, time);
    }

    @Test
    public void testCppUTestHandlerErrorMatching()
    {
        final String input = "TEST(SuiteName, testName) - 1 ms\n"
                + "TEST(SuiteName, testName2)\n"
                + "test/SuiteName.cpp:40: error: Failure in TEST(SuiteName, testName2)\n"
                + "	expected <3.07>\n"
                + "	but was  <4.09> threshold used was <0.1>\n"
                + "\n"
                + " - 2 ms\n"
                + "TEST(SuiteName, testName3) - 3 ms";

        final String lines[] = input.split("\n");
        CppUTestHandler handler = new CppUTestHandler(DONT_CARE_INFO);
        CppUTestSuiteFinishedHandler finishedHandler = new CppUTestSuiteFinishedHandler(DONT_CARE_INFO);

        assertTrue(handler.matches(lines[0]));
        assertTrue(handler.matches(lines[1]));
        assertFalse(handler.matches(lines[2]));
        assertFalse(handler.matches(lines[3]));
        assertFalse(handler.matches(lines[4]));
        assertFalse(handler.matches(lines[5]));
        assertFalse(handler.matches(lines[6]));
        assertTrue(handler.matches(lines[7]));

        for( String line : lines )
        {
            assertFalse(finishedHandler.matches(line));
        }
    }

    @Test
    public void testCppUTestHandlerErrorParsing()
    {
        final String input = "TEST(SuiteName, testName) - 1 ms\n"
                + "TEST(SuiteName, testName2)\n"
                + "test/SuiteName.cpp:40: error: Failure in TEST(SuiteName, testName2)\n"
                + "	expected <3.07>\n"
                + "	but was  <4.09> threshold used was <0.1>\n"
                + "\n"
                + " - 2 ms\n"
                + "TEST(SuiteName, testName3) - 3 ms";

        final String lines[] = input.split("\n");
        CppUTestHandler handler = new CppUTestHandler(DONT_CARE_INFO);

        Matcher m = handler.match(lines[0]);
        assertTrue(m.find());
        assertNotNull(m.group(3));

        m = handler.match(lines[1]);
        assertTrue(m.find());
        assertNull(m.group(3));

        m = handler.match(lines[7]);
        assertTrue(m.find());
        assertNotNull(m.group(3));
    }

    @Test
    public void testCppUTestFinishedHandler()
    {
        final String inputOk = "OK (8 tests, 8 ran, 7 checks, 0 ignored, 0 filtered out, 123 ms)";
        final String inputError = "Errors (2 failures, 8 tests, 8 ran, 7 checks, 0 ignored, 0 filtered out, 124 ms)";

        CppUTestSuiteFinishedHandler finishedHandler
                = new CppUTestSuiteFinishedHandler(DONT_CARE_INFO);

        assertTrue(finishedHandler.matches(inputOk));
        assertTrue(finishedHandler.matches(inputError));

        assertFalse(finishedHandler.matches("OK"));
        assertFalse(finishedHandler.matches("OK ("));
        assertFalse(finishedHandler.matches("OK ( )"));

        assertFalse(finishedHandler.matches("Errors"));
        assertFalse(finishedHandler.matches("Errors ("));
        assertFalse(finishedHandler.matches("Errors ( )"));
    }

    @Test
    public void testCppUTestErrorHandler()
    {
        final String input = "TEST(SuiteName, testName)\n"
                + "test/SuiteName.cpp:37: error: "
                + "Failure in TEST(SuiteName, testName)\n"
                + "	expected <abc>\n"
                + "	but was  <def>\n"
                + "	difference starts at position 0 at: <          def       >\n"
                + "	                                               ^\n"
                + "\n"
                + " - 0 ms";
        
        final String lines[] = input.split("\n");
        CppUTestErrorHandler handler = new CppUTestErrorHandler(DONT_CARE_INFO);
        
        assertFalse(handler.matches(lines[0]));
        assertTrue(handler.matches(lines[1]));
        
        Matcher m = handler.match(lines[1]);
        assertTrue(m.find());
        
        assertEquals(4, m.groupCount());
        assertEquals("test/SuiteName.cpp", m.group(1));
        assertEquals("37", m.group(2));
        assertEquals("SuiteName", m.group(3));
        assertEquals("testName", m.group(4));
    }

    @Test
    public void testSuiteTime()
    {
        final String input = "TEST(SuiteName, testCase1) - 17005 ms\n"
                + "TEST(SuiteName2, testCase1) - 8 ms\n"
                + "TEST(SuiteName2, testCase2) - 25 ms\n";

        final String lines[] = input.split("\n");
        CppUTestHandler handler = new CppUTestHandler(DONT_CARE_INFO);
        final long expected = 17005 + 8 + 25;
        long time = 0L;

        for( String line : lines )
        {
            Matcher m = handler.match(line);
            assertTrue(m.find());
            time += Long.valueOf(m.group(4));
        }

        assertEquals(expected, time);
    }

    @Test
    public void testWithEscapeColorCode()
    {
        final String errorMsg = "Errors (1 failures, 9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 123 ms)";
        final String errorMsgColor = "\u001B[31;1m" + errorMsg;
        final String errorMsgColor2 = errorMsgColor + "\u001B[m";

        final String okMsg = "OK (9 tests, 9 ran, 7 checks, 0 ignored, 0 filtered out, 124 ms)";
        final String okMsgColor = "\u001B[32;1m" + okMsg;
        final String okMsgColor2 = okMsg + "\u001B[m";

        CppUTestSuiteFinishedHandler handler = new CppUTestSuiteFinishedHandler(DONT_CARE_INFO);

        assertTrue(handler.matches(okMsg));
        assertTrue(handler.matches(okMsgColor));
        assertTrue(handler.matches(okMsgColor2));

        assertTrue(handler.matches(errorMsg));
        assertTrue(handler.matches(errorMsgColor));
        assertTrue(handler.matches(errorMsgColor2));
    }

}
