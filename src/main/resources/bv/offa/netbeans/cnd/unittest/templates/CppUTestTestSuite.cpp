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
    <#if enableModernCpp == true>
    void setup() override
    <#else>
    void setup()
    </#if>
    {
        // Setup ...
    }

</#if>
<#if generateTeardown == true >
    <#if enableModernCpp == true>
    void teardown() override
    <#else>
    void teardown()
    </#if>
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