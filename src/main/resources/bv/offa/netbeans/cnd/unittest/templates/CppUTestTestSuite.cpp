/* 
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <CppUTest/TestHarness.h>

TEST_GROUP(${suiteName})
{
<#if generateSetup == true >
    void setup()
    {
        // Setup ...
    }

</#if>
<#if generateTeardown == true >
    void teardown()
    {
        // Teardown ...
    }

</#if>
};

<#if generateTestCases == true >
<#list testCases as testCase>
TEST(${suiteName}, ${testCase})
{
    FAIL("Nothing to test yet ...");
}

</#list>
</#if>