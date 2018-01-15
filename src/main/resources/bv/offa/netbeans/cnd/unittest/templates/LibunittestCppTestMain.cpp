/*
 * File:   ${nameAndExt}
 * Author: ${user}
 *
 * Created on ${date}, ${time}
 */

#include <libunittest/main.hpp>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
<#if enableModernCpp == true >
    args.emplace_back("-v"); // Verbose output (mandatory!)
<#else>
    args.push_back("-v"); // Verbose output (mandatory!)
</#if>

<#if enableModernCpp == true >
    return unittest::process(args.size(), const_cast<char**>(args.data()));
<#else>
    return unittest::process(args.size(), const_cast<char**>(&args[0]));
</#if>
}