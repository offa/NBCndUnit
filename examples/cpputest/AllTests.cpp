
#include <CppUTest/CommandLineTestRunner.h>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v"); // Verbose output (mandatory!)
    args.push_back("-c"); // Colored outupt (optional)

    return CommandLineTestRunner::RunAllTests(args.size(), &args[0]);
}

