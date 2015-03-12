/* 
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <libunittest/all.hpp>

using namespace unittest::assertions;

COLLECTION(${suiteName})
{
<#if generateTestCases == true >
<#list testCases as testCase>
    TEST(${testCase})
    {
        assert_true(false, "Nothing to test yet ...", SPOT);
    }
    
</#list>
</#if>
}
