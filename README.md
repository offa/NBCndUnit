# [NBCndUnit](https://github.com/offa/NBCndUnit)

[![CI](https://github.com/offa/NBCndUnit/workflows/ci/badge.svg)](https://github.com/offa/NBCndUnit/actions)
[![GitHub release](https://img.shields.io/github/release/offa/NBCndUnit.svg)](https://github.com/offa/NBCndUnit/releases)
[![License](https://img.shields.io/badge/license-GPLv3-yellow.svg)](LICENSE)
[![NetBeans](https://img.shields.io/badge/netbeans-8.2-lightgrey.svg)](http://plugins.netbeans.org/plugin/57174/nbcndunit)
![Java](https://img.shields.io/badge/java-1.8-green.svg)

**NBCndUnit** is a [***NetBeans***](https://netbeans.org) plugin for ***C/C++ unit testing***. It integrates the [_**CppUTest**_](https://cpputest.github.io/), [_**GoogleTest** (gtest) / **GoogleMock** (gmock)_](https://github.com/google/googletest) and [_**libunittest C++**_](http://libunittest.sourceforge.net/) unit testing frameworks.


# Requirements

 - [**NetBeans 8.2**](https://netbeans.org)
 - **Testing framework(s)**


# Supported C/C++ unit testing frameworks

 - [CppUTest](https://cpputest.github.io/)
 - [GoogleTest (gtest) / GoogleMock (gmock)](https://github.com/google/googletest)
 - [libunittest C++](http://libunittest.sourceforge.net/)


# Installation

The Plugin is available through the **Plugin Manager** (*Tools → Plugins → Available Plugins*).

As an alternative, it’s also possible to download the *NBM* manually from the [**NetBeans Plugin Portal**](http://plugins.netbeans.org/plugin/57174/nbcndunit).

#### [`Signature Info`](https://github.com/offa/NBCndUnit/wiki/NBM-Signature)


# Update

Updates are delivered as usual by the **NetBeans Updater**.




# Getting started

 1. **Create** a new NetBeans C/C++ **project**
 1. **Add** unit testing **framework** (binaries and headers) to the test settings
 1. **Write** tests
 1. **Run** them

**Note:** Some frameworks require ***verbose*** output.


## Creating new Tests

New tests can be created either *manually* or using the *new unit test wizard* – located in the ***Unit Tests*** file category.


## Examples

Example test suites are available in the `examples` directory.


## Running tests

The tests are run as usual using the **Test button**.

The ***Test Results window*** shows the result of the tests.


## Enabling verbose mode

*CppUTest* and *libunittest C++* do not show test details per default, therefore the *verbose mode* must be set.

Test mains created using the new file wizard already have this mode enabled. For existing tests the examples below can be used.

At this point it's also possible to add further options (eg. test filter).

### CppUTest

```cpp

#include <CppUTest/CommandLineTestRunner.h>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v"); // Set verbose mode
    args.push_back("-c"); // Set color output (Optional)

    return RUN_ALL_TESTS(args.size(), &args[0]);
}
```


### libunittest C++

```cpp

#include <libunittest/main.hpp>
#include <vector>

int main(int argc, char** argv)
{
    std::vector<const char*> args(argv, argv + argc);
    args.push_back("-v"); // Set verbose mode

    return unittest::process(args.size(), const_cast<char**>(&args[0]));
}

```


# Using existing Projects

NetBeans projects created from ***existing projects*** (eg. using custom Makefiles, CMake, …) do *not* have Test Support enabled by default.

To enable it, there are ***two Make-Targets*** and a ***configuration update*** required. For more information on this, please see [***Wiki → Using exsiting projects***](https://github.com/offa/NBCndUnit/wiki/Using-existing-projects).

_**Note:** This fature is currently **experimental**!_


## Project requirements

The project must have these Make-Targets:

| Target            |                          |
|-------------------|--------------------------|
| **`build-tests`** | builds the tests, executed before running the tests (can be empty) |
| **`test`**        | ***executes*** the tests *(verbose output)* |

***Note:*** CMake has `make test` reserved, please see Wiki for a workaround.


## Enable Test Support

In addition, the Netbeans Project configuration must contain a Test Files Folder. The project can be upgraded using the ***New File Wizzard*** for Unit Test / Test Main files:

**`New Files → Unit Tests → <select any>`** and *enable* **`Configure custom Project`**

A *Test Files* folder is added to the NetBeans project – not visible at file level.

This step is needed only once per project, though doing this more than once has no effect.



# License

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
