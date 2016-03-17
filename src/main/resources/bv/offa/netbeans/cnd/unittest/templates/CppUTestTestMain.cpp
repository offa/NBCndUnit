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
    args.push_back("-v"); // Verbose output (mandatory!)
    <#if enableColor == true >
    args.push_back("-c"); // Colored output (optional)
    </#if>
    
    return RUN_ALL_TESTS(args.size(), &args[0]);
}
