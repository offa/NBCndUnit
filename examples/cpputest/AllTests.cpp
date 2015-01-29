
#include <CppUTest/CommandLineTestRunner.h>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v"); // Enable verbose output
    args.push_back("-c"); // Enable colored output (optional)
    
    return CommandLineTestRunner::RunAllTests(args.size(), &args[0]);
}

