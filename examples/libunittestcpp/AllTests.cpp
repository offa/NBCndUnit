
#include <libunittest/main.hpp>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v"); // Verbose output (mandatory!)

    return unittest::process(args.size(), const_cast<char**> (&args[0]));
}

