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
    void SetUp()
    {
        // Setup ...
    }

</#if>
<#if generateTeardown == true >
    void TearDown()
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