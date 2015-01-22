# [NBCndUnit](https://github.com/offa/NBCndUnit)

NBCndUnit is a [NetBeans](https://netbeans.org) plugin for C/C++ unit testing.

It integrates the [CppUTest](https://cpputest.github.io/) and [Google Test](https://code.google.com/p/googletest/) unit testing frameworks.


## Requirements

 - [**NetBeans 8**](https://netbeans.org)
 - **Testing framework** (*at least one*)
  - [CppUTest](https://cpputest.github.io/)
  - [Google Test](https://code.google.com/p/googletest/)

*(The plugin has not been tested with earlier versions of NetBeans yet.)*


## Getting started

 1. Create a new NetBeans C/C++ project
 1. Add CppUTest / Google Test library and headers to the test settings
 1. Write tests *(CppUTest only: in addition a main with *verbose* output enabled)*
 1. Test project


*For more informations about writing tests, please see the manuals:*

 - [CppUTest manual](https://cpputest.github.io/manual.html)
 - [Google Test manual](https://code.google.com/p/googletest/w/list)


## Examples

Example test suites are available in the `examples` direcotry.


## Running tests

The tests are run as usual using the Test button.

The *Test Results* window will show the result of the tests.


## CppUTest

CppUTest does not show test details per default, therefore the *verbose mode* must be set.

This can be done through the commandline arguments of the tests main:

**AllTests.cpp**

```cpp
#include <vector>
#include <CppUTest/CommandLineTestRunner.h>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc); // Insert all arguments
    args.push_back("-v"); // Set verbose mode
    args.push_back("-c"); // Set color output (OPTIONAL)
    
    // Run all tests
    return CommandLineTestRunner::RunAllTests(args.size(), &args[0]);
}
```
The file `examples/cpputest/AllTests.cpp` contains a boilerplate for a test main.


## Google Test

*Nothing yet.*


## License

**GNU General Public License (GPL)**

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <http://www.gnu.org/licenses/>.
