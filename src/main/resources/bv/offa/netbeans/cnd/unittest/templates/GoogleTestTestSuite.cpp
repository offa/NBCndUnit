/*
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <gtest/gtest.h>

class ${suiteName} : public testing::Test
{
protected:
<#if generateSetup == true >
    <#if enableModernCpp == true>
    void SetUp() override
    <#else>
    void SetUp()
    </#if>
    {
        // Setup ...
    }

</#if>
<#if generateTeardown == true >
    <#if enableModernCpp == true>
    void TearDown() override
    <#else>
    void TearDown()
    </#if>
    {
        // Teardown ...
    }

</#if>
};

<#if generateTestCases == true >
<#list testCases as testCase>
TEST_F(${suiteName}, ${testCase})
{
    FAIL();
}

</#list>
</#if>