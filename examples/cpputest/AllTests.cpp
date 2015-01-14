
#include <vector>
#include <CppUTest/CommandLineTestRunner.h>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v");
    args.push_back("-c");
    
    return CommandLineTestRunner::RunAllTests(args.size(), &args[0]);
}
