/* 
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <libunittest/all.hpp>

using namespace unittest::assertions;

struct ${suiteName}Fixture
{
<#if generateSetup == true >
    ${suiteName}Fixture()
    {
        // Setup ...
    }

</#if>
<#if generateTeardown == true >
    virtual ~${suiteName}Fixture() noexcept(false)
    {
        // Teardown ...
    }

</#if>
};

COLLECTION(${suiteName})
{
<#if generateTestCases == true >
<#list testCases as testCase>
    TEST_FIXTURE(${suiteName}Fixture, ${testCase})
    {
        assert_true(false, "Nothing to test yet ...", SPOT);
    }
    
</#list>
</#if>
}
