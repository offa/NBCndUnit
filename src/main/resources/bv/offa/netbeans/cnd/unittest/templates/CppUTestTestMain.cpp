/*
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <CppUTest/CommandLineTestRunner.h>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
<#if enableModernCpp == true >
    args.emplace_back("-v"); // Verbose output (mandatory!)
<#else>
    args.push_back("-v"); // Verbose output (mandatory!)
</#if>
<#if enableColor == true >
    <#if enableModernCpp == true >
    args.emplace_back("-c"); // Colored output (optional)
    <#else>
    args.push_back("-c"); // Colored output (optional)
    </#if>
</#if>

<#if enableColor == true >
    return RUN_ALL_TESTS(args.size(), args.data());
<#else>
    return RUN_ALL_TESTS(args.size(), &args[0]);
</#if>
}
