# [NBCndUnit](https://github.com/offa/NBCndUnit)

NBCndUnit is a [NetBeans](https://netbeans.org) plugin for C/C++ unit testing.

It integrates the [CppUTest](https://cpputest.github.io/) unit testing framework into the NetBeans test runner (as JUnit does).


## Requirements

 - [**CppUTest**](https://cpputest.github.io/) - *testing framework*
 - **NetBeans 8**

*The plugin has not been tested with earlier versions of NetBeans yet.*


## Getting started

 1. Create a new NetBeans C/C++ project
 1. Add CppUTest library and headers to the test settings
 1. Write tests and it's main (make sure *verbose* output is set)
 1. Test project

The file `examples/cpputest/AllTests.cpp` contains a boilerplate for a test main.

*For more informations about writing tests, please see the [CppUTest Manual](https://cpputest.github.io/manual.html)*


## Examples

An example test suite (`Example.cpp`) and the test main (`AllTests.cpp`) is located in `examples/cpputest`.


## Running tests

The tests are run as usual using the Test button.

The *Test Results* window will show the result of the tests.


### Important note

CppUTest does not show test details per default, therefore the *verbose mode* must be set.

This can be done through the commandline arguments of the tests main:

**`AllTests.cpp`**

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

This file is also included in `examples/cpputest`.


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
